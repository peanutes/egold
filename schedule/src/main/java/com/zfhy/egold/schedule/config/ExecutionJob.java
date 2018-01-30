package com.zfhy.egold.schedule.config;

import java.lang.reflect.InvocationTargetException;


public interface ExecutionJob {
	
	/**
	 * 
	 * @throws Exception
	 */
	public void execute() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
