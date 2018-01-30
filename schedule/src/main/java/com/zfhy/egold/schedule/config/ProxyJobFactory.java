package com.zfhy.egold.schedule.config;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;


public class ProxyJobFactory implements JobFactory {
	
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
		return (Job) bundle.getJobDetail().getJobDataMap().get(ScheduleJobConfiguration.JOB_PROXY_KEY);
	}
}
