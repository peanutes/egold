package com.zfhy.egold.schedule.config;


import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DateUtilExt;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

public class ManagedSchedulerFactoryBean extends SchedulerFactoryBean {
	
	private JobProxy jobProxy;

	/**
	 * 下次执行间隔时长
	 * 
	 * @return
	 */
	public String getNextInterval() {
		return getInterval(LocalDateTime.now(), getNextDate());
	}

	/**
	 * 下次执行时间
	 * 
	 * @return
	 */
	public String getNextDateTime() {
		return DateUtilExt.dateToStr(getNextDate(), DateUtilExt.LOCALDATETIME_FORMAT);
	}

	private String getInterval(LocalDateTime date1, LocalDateTime date2) {
		long seconds = DateUtilExt.getBetweenSeconds(date1, date2);
		if (seconds < 180) {
			return seconds + "秒";
		} else if (seconds >= 180 && seconds < 3600) {
			return seconds / 60 + "分" + seconds % 60 + "秒";
		} else if (seconds >= 3600 && seconds < 3600 * 24) {
			return seconds / 3600 + "小时" + (seconds % 3600) / 60 + "分钟";
		}
		return "1天后";
	}

	private LocalDateTime getNextDate() {
		try {
			GroupMatcher<TriggerKey> triggerKey = GroupMatcher.groupEndsWith(ScheduleJobConfiguration.JOB_GROUP);

			Scheduler sch = this.getScheduler();

			Set<TriggerKey> tks = sch.getTriggerKeys(triggerKey);
			if (!tks.isEmpty()) {
				return toLocalDateTime(sch.getTrigger(tks.iterator().next()).getNextFireTime());
			}
		} catch (SchedulerException e) {
			throw new BusException("",e);
		}

		return null;
	}

	private LocalDateTime toLocalDateTime(Date date){
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	public JobProxy getJobProxy() {
		return jobProxy;
	}

	public void setJobProxy(JobProxy jobProxy) {
		this.jobProxy = jobProxy;
	}
}
