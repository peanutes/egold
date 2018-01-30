package com.zfhy.egold.schedule.config;


public enum JobStatus {
	
	WAIT_RUN("等待执行"), RUNNING("正在执行"), SUCCESS("执行完成"), FAILURE("执行失败"), INIT("未执行");

	private final String status;

	private JobStatus(String status) {
		this.status = status;
	}

	public String toString() {
		return status;
	}
}
