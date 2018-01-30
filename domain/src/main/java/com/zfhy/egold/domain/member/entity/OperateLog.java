package com.zfhy.egold.domain.member.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "member_operate_log")
public class OperateLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    private String ip;

    
    private String mobile;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "session_id")
    private String sessionId;

    
    private String token;

    
    @Column(name = "operate_method")
    private String operateMethod;

    @Column(name = "operate_method_str")
    private String operateMethodStr;

    
    @Column(name = "operate_result")
    private Integer operateResult;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "execute_time")
    private Long executeTime;

    
    @Column(name = "terminal_type")
    private String terminalType;

    
    @Column(name = "terminal_id")
    private String terminalId;

    
    @Column(name = "exception_msg")
    private String exceptionMsg;


    
    @Column(name = "operate_input")
    private String operateInput;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getIp() {
        return ip;
    }

    
    public void setIp(String ip) {
        this.ip = ip;
    }

    
    public String getMobile() {
        return mobile;
    }

    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    public Integer getMemberId() {
        return memberId;
    }

    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    
    public String getSessionId() {
        return sessionId;
    }

    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    
    public String getToken() {
        return token;
    }

    
    public void setToken(String token) {
        this.token = token;
    }

    
    public String getOperateMethod() {
        return operateMethod;
    }

    
    public void setOperateMethod(String operateMethod) {
        this.operateMethod = operateMethod;
    }

    
    public String getOperateMethodStr() {
        return operateMethodStr;
    }

    
    public void setOperateMethodStr(String operateMethodStr) {
        this.operateMethodStr = operateMethodStr;
    }

    
    public Integer getOperateResult() {
        return operateResult;
    }

    
    public void setOperateResult(Integer operateResult) {
        this.operateResult = operateResult;
    }

    
    public Date getCreateDate() {
        return createDate;
    }

    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    
    public Long getExecuteTime() {
        return executeTime;
    }

    
    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    
    public String getTerminalType() {
        return terminalType;
    }

    
    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    
    public String getTerminalId() {
        return terminalId;
    }

    
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    
    public String getOperateInput() {
        return operateInput;
    }

    
    public void setOperateInput(String operateInput) {
        this.operateInput = operateInput;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }
}
