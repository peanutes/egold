//package com.zfhy.egold.admin.web.schedule;
//
//import com.zfhy.egold.domain.job.dao.JobMapper;
//import com.zfhy.egold.schedule.config.ManagedSchedulerFactoryBean;
//import com.zfhy.egold.schedule.config.ScheduleJobConfiguration;
//import org.apache.commons.collections.MapUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.quartz.JobKey;
//import org.quartz.SchedulerException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * job管理控制页面
// */
//@Controller
//@RequestMapping("/job")
//public class JobController {
//
//	private final static String prefix = "schedule/job";
//
//	private Logger logger = LoggerFactory.getLogger(JobController.class);
//	@Autowired
//	private ApplicationContext applicationContext;
//
//	private static final int DEFAULT_PAGE_SIZE = 20;
//
//	private final Map<Integer, String> STATUS_MAP = new HashMap<>();
//
//	public JobController() {
//		STATUS_MAP.put(0, "忽略执行");
//		STATUS_MAP.put(1, "正在执行");
//		STATUS_MAP.put(2, "执行完成");
//		STATUS_MAP.put(3, "执行失败");
//	}
//
//	@Autowired
//	private JobMapper jobMapper;
//
//	/**
//	 *
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/list")
//	public String job(HttpServletRequest request) {
//		Map<String, ManagedSchedulerFactoryBean> result = applicationContext.getBeansOfType(ManagedSchedulerFactoryBean.class);
//		request.setAttribute("jobs", result);
//		return prefix+"/jobs";
//	}
//
//	/**
//	 *
//	 * @param id
//	 * @param page
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/log/{id}/{page:[0-9]+}")
//	public String log(@PathVariable("id") String id, @PathVariable("page") int page, HttpServletRequest request) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("id", id);
//
//		Long count = jobMapper.queryLogCount(params);
//		Long size = 0l;
//		List<Map<String, Object>> logs = Collections.emptyList();
//		if (count > 0l) {
//			size = count % DEFAULT_PAGE_SIZE == 0 ? count / DEFAULT_PAGE_SIZE : count / DEFAULT_PAGE_SIZE + 1;
//
//			int start = (page - 1) * DEFAULT_PAGE_SIZE + 1;
//			int end = page * DEFAULT_PAGE_SIZE;
//
//			params.put("start", start);
//			params.put("end", end);
//
//			logs = jobMapper.queryLogList(params);
//			logs.forEach((map) -> {
//				int status = MapUtils.getInteger(map, "status");
//				map.put("status", STATUS_MAP.get(status));
//			});
//		}
//		request.setAttribute("logs", logs);
//		request.setAttribute("count", count);
//		request.setAttribute("id", id);
//		request.setAttribute("page", page);
//		request.setAttribute("size", size);
//		return prefix+"/log";
//	}
//
//	/**
//	 *
//	 * @param id
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/start/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public String start(@PathVariable("id") String id, HttpServletRequest request) {
//		ManagedSchedulerFactoryBean msfb = getManagedSchedulerFactoryBean(id);
//		if (msfb != null && !msfb.isRunning()) {
//			msfb.start();
//			return "1";
//		}
//		return "0";
//	}
//
//	/**
//	 *
//	 * @param id
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/stop/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public String stop(@PathVariable("id") String id, HttpServletRequest request) {
//		ManagedSchedulerFactoryBean msfb = getManagedSchedulerFactoryBean(id);
//		if (msfb != null && msfb.isRunning()) {
//			msfb.stop();
//			return "1";
//		}
//		return "0";
//	}
//
//	/**
//	 *
//	 * @param id
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/run/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public String run(@PathVariable("id") String id, HttpServletRequest request) {
//		ManagedSchedulerFactoryBean msfb = getManagedSchedulerFactoryBean(id);
//		if (msfb != null) {
//			try {
//				msfb.getScheduler().triggerJob(new JobKey(msfb.getJobProxy().getName(), ScheduleJobConfiguration.JOB_GROUP));
//			} catch (SchedulerException e) {
//				logger.error(e.getMessage(), e);
//			}
//			return "1";
//
//		}
//		return "0";
//	}
//
//	/**
//	 *
//	 * @param id
//	 * @return
//	 */
//	private ManagedSchedulerFactoryBean getManagedSchedulerFactoryBean(String id) {
//		if (StringUtils.isNotBlank(id)) {
//			id = "&" + id;
//			if (applicationContext.containsBean(id)) {
//				return applicationContext.getBean(id, ManagedSchedulerFactoryBean.class);
//			}
//		}
//		return null;
//	}
//}
