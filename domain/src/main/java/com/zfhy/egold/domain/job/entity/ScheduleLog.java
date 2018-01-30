package com.zfhy.egold.domain.job.entity;

import java.util.Date;

/**
 */
public class ScheduleLog {
	private Integer id;
	private String uuid;

	/**
	 * 0：忽略 1：正在执行 2：执行成功 3：执行错误
	 */
	private Integer status;
	private Date beginDate;
	private Date endDate;
	private Long runDuration;
	private String errorMsg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getRunDuration() {
		return runDuration;
	}

	public void setRunDuration(Long runDuration) {
		this.runDuration = runDuration;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
