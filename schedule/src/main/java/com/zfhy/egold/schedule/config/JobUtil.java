package com.zfhy.egold.schedule.config;


public final class JobUtil {
	private static Integer id = 0;

	public static synchronized Integer generateJobId() {
		return ++id;
	}
}
