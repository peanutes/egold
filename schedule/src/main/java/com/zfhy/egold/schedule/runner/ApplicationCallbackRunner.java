package com.zfhy.egold.schedule.runner;

import com.zfhy.egold.common.util.DateUtilExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * springboot初始化之后执行，
 * 1：初始化DateSupplier
 * 2：初始化Validation
 * 3：初始化调度
 */
@Component
public class ApplicationCallbackRunner implements CommandLineRunner {
	
	@Value("${app.env}")
	private String mode;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 执行
	 */
	@Override
	public void run(String... args) throws Exception {

		//初始化调度
		ScheduleJobInitializer.doInitialization(applicationContext);
	}
}
