package com.zfhy.egold.schedule.config;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduledJob {
	/**
	 * job名称
	 */
	String name();

	/**
	 * cron表达式
	 */
	String cron();

	String method();

	/**
	 * 自动启动
	 */
	boolean autoStartup() default true;

	/**
	 * 延迟多久启动（单位：秒） 0为立即启动
	 * 
	 * @return
	 */
	int startupDelay() default 0;

	/**
	 * 顺序显示
	 * @return
	 */
	int orderNum() default 0;
}
