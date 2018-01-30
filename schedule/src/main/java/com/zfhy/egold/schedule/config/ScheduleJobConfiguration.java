package com.zfhy.egold.schedule.config;

import com.google.common.collect.Lists;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.job.dao.JobMapper;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
@ConditionalOnExpression("${schedule.enabled}")
@ConditionalOnBean(annotation = ScheduledJob.class)
public class ScheduleJobConfiguration {
	
	private static final ProxyJobFactory JOB_FACTORY = new ProxyJobFactory();

	private static final Logger LOG = LoggerFactory.getLogger(ScheduleJobConfiguration.class);

	public static final String JOB_GROUP = "NGINF_JOB";

	public static final String JOB_PROXY_KEY = "jobProxy";

	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private JobMapper jobMapper;

	@Bean
	public Runnable createManagedSchedulerFactoryBean() throws ParseException {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();

		Map<String, Object> jobMaps = beanFactory.getBeansWithAnnotation(ScheduledJob.class);

		Object[] jobs = jobMaps.values().toArray();

//		Object[] orderJobs = validateJobs(jobs);

		Executor taskExecutor = Executors.newFixedThreadPool(jobMaps.size());

		for (int i = 0; i < jobMaps.size(); i++) {
			Object obj = jobs[i];

			ScheduledJob job = ClassUtils.getUserClass(obj).getAnnotation(ScheduledJob.class);

			JobProxy jobProxy = new JobProxy();
			jobProxy.setJob(new ExecutionJobImpl(obj, job.method()));
			jobProxy.setCron(job.cron());
			jobProxy.setName(job.name());
			jobProxy.setJobMapper(jobMapper);

			Trigger trigger = createTrigger(jobProxy);

			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ManagedSchedulerFactoryBean.class);

			beanDefinitionBuilder.addPropertyValue("taskExecutor", taskExecutor);
			beanDefinitionBuilder.addPropertyValue("triggers", trigger);
			beanDefinitionBuilder.addPropertyValue("autoStartup", job.autoStartup());
			beanDefinitionBuilder.addPropertyValue("startupDelay", job.startupDelay());
			beanDefinitionBuilder.addPropertyValue("jobFactory", JOB_FACTORY);
			beanDefinitionBuilder.addPropertyValue(ScheduleJobConfiguration.JOB_PROXY_KEY, jobProxy);

			beanFactory.registerBeanDefinition("managedSchedulerFactoryBean" + i, beanDefinitionBuilder.getBeanDefinition());

			LOG.info("初始化调度任务：{}成功，执行周期为：{}", job.name(), job.cron());
		}

		LOG.info("初始化调度任务成功，总共读取" + jobs.length + "个配置项");

		return null;
	}

	private static Object[] validateJobs(Object[] jobs) {
		Set<String> names = new HashSet<>();

		Object[] orderJobs = new Object[jobs.length];
		List<Object> objects = Lists.newArrayList();

		for (Object obj : jobs) {
			ScheduledJob job = ClassUtils.getUserClass(obj).getAnnotation(ScheduledJob.class);

			if (StringUtils.isBlank(job.name()) || StringUtils.isBlank(job.cron()) || StringUtils.isBlank(job.method())) {
				throw new BusException("name、cron、method不能为空，" + obj.getClass());
			}
			int orderNum = job.orderNum();
			if(orderNum != 0){
				orderJobs[orderNum-1] = obj;
			}else {
				objects.add(obj);
			}
			names.add(job.name());
		}

		if (names.size() != jobs.length) {
			throw new BusException("job名称配置有重复");
		}

		if(!CollectionUtils.isEmpty(objects)){
			fillJobArray(orderJobs, objects);
		}

		return orderJobs;
	}

	private static void fillJobArray(Object[] orderJobs, List<Object> objects) {
		for(Object object : objects){
            Object noOrderObj = object;
            for(int i = 0; i<orderJobs.length; i++){
                if(orderJobs[i] == null){
                    orderJobs[i] = noOrderObj;
                    break;
                }
            }
        }
	}

	private static Trigger createTrigger(JobProxy jobProxy) throws ParseException {
		String name = jobProxy.getName();
		String group = ScheduleJobConfiguration.JOB_GROUP;

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(ScheduleJobConfiguration.JOB_PROXY_KEY, jobProxy);

		JobDetailImpl jdi = new JobDetailImpl();
		jdi.setName(name);
		jdi.setGroup(group);
		jdi.setJobClass(JobProxy.class);
		jdi.setDurability(false);
		jdi.setJobDataMap(jobDataMap);

		jobDataMap.put("jobDetail", jdi);

		CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression(jobProxy.getCron());
		trigger.setJobKey(jdi.getKey());
		trigger.setName(name);
		trigger.setGroup(group);
		trigger.setJobDataMap(jobDataMap);
		trigger.setJobKey(jdi.getKey());

		return trigger;
	}
}