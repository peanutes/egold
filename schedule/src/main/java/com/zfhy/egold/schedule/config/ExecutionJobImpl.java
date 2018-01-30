package com.zfhy.egold.schedule.config;

import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ExecutionJobImpl implements ExecutionJob {
	
	private Object target;

	private final Method method;

	public ExecutionJobImpl(Object target, String methodName) {
		this.target = target;
		this.method = getMethod(target, methodName);
	}

	public void execute() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		method.invoke(target);
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getMethod() {
		return method;
	}

	private Method getMethod(Object obj, String methodName) {
		return ClassUtils.getMethod(obj.getClass(), methodName);
	}
}
