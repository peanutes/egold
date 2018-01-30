package com.zfhy.egold.schedule.config;

import com.zfhy.egold.domain.job.dao.JobMapper;
import com.zfhy.egold.domain.job.entity.ScheduleJob;
import com.zfhy.egold.domain.job.entity.ScheduleLog;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class JobProxy implements Job {
	
	private static final Logger LOG = LoggerFactory.getLogger(JobProxy.class);

	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private JobMapper jobMapper;

	private Integer jobId = JobUtil.generateJobId();

	// job当前运行状态
	private String currentStatus = JobStatus.WAIT_RUN.toString();

	// job上一次运行状态
	private String prevStatus = JobStatus.INIT.toString();

	// 最后一次运行的时间
	private String lastExecTime = "";

	// job上一次运行时长
	private Long prevTimespent = 0l;

	// 总共执行次数
	private Integer count = 0;

	// 失败的次数
	private Integer failCount = 0;

	private String name;

	private String uuid;

	private String cron;

	private ExecutionJob job;

	public ExecutionJob getJob() {
		return job;
	}

	private String now() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(FORMAT);
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = jobMapper.queryScheduleJob(uuid);
		if (scheduleJob == null) {
			LOG.error("批量任务{}，数据丢失，忽略执行任务", name);
			return;
		} else if (scheduleJob.getStatus() == 1) {
			ScheduleLog log = createScheduleLog();
			log.setStatus(0);
			log.setErrorMsg("批量任务正在执行，忽略此次执行");
			//todo
			jobMapper.addJobLog(log);
			LOG.warn("批量任务{}正在执行，忽略此次执行", name);
			return;
		}

		scheduleJob.setStatus(1);
		jobMapper.updateJobStatus(scheduleJob);

		ScheduleLog log = createScheduleLog();
		log.setStatus(1);
		jobMapper.addJobLog(log);

		count++;
		lastExecTime = now();
		try {
			currentStatus = JobStatus.RUNNING.toString();

			long start = System.currentTimeMillis();

			job.execute();

			long end = System.currentTimeMillis();

			prevTimespent = end - start;

			prevStatus = JobStatus.SUCCESS.toString();

			log.setStatus(2);
			log.setRunDuration(prevTimespent);
		} catch (Exception e) {
			log.setStatus(3);
			log.setErrorMsg(e.getCause() == null ? StringUtils.left(e.getMessage(),300) : StringUtils.left(e.getCause().getMessage(),300));

			failCount++;
			LOG.error(e.getMessage(), e);
			prevStatus = JobStatus.FAILURE.toString();
		}

		currentStatus = JobStatus.WAIT_RUN.toString();

		scheduleJob.setStatus(0);
		jobMapper.updateJobStatus(scheduleJob);

		jobMapper.updateJobLog(log);
	}

	private ScheduleLog createScheduleLog() {
		ScheduleLog log = new ScheduleLog();
		log.setUuid(Base64.getEncoder().encodeToString(name.getBytes()));
		return log;
	}

	public JobMapper getJobMapper() {
		return jobMapper;
	}

	public void setJobMapper(JobMapper jobMapper) {
		this.jobMapper = jobMapper;
	}

	public void setJob(ExecutionJob job) {
		this.job = job;
	}

	public String getCron() {
		return cron;
	}

	public String getUuid() {
		return uuid;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public String getPrevStatus() {
		return prevStatus;
	}

	public Long getPrevTimespent() {
		return prevTimespent;
	}

	public Integer getCount() {
		return count;
	}

	public void setName(String name) {
		this.name = name;
		this.uuid = Base64.getEncoder().encodeToString(name.getBytes());
	}

	public String getName() {
		return name;
	}

	public Integer getJobId() {
		return jobId;
	}

	public String getLastExecTime() {
		return lastExecTime;
	}

	public Integer getFailCount() {
		return failCount;
	}
}
