package com.zfhy.egold.domain.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_admin_log")
public class AdminLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "login_account")
    private String loginAccount;

    
    @Column(name = "operate_method")
    private String operateMethod;

    
    @Column(name = "operate_input")
    private String operateInput;

    
    @Column(name = "operate_output")
    private String operateOutput;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    private String ip;

    
    private Long time;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getLoginAccount() {
        return loginAccount;
    }

    
    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    
    public String getOperateMethod() {
        return operateMethod;
    }

    
    public void setOperateMethod(String operateMethod) {
        this.operateMethod = operateMethod;
    }

    
    public String getOperateInput() {
        return operateInput;
    }

    
    public void setOperateInput(String operateInput) {
        this.operateInput = operateInput;
    }

    
    public String getOperateOutput() {
        return operateOutput;
    }

    
    public void setOperateOutput(String operateOutput) {
        this.operateOutput = operateOutput;
    }

    
    public Date getCreateDate() {
        return createDate;
    }

    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    
    public Date getUpdateDate() {
        return updateDate;
    }

    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    
    public String getDelFlag() {
        return delFlag;
    }

    
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    
    public String getRemarks() {
        return remarks;
    }

    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    
    public String getIp() {
        return ip;
    }

    
    public void setIp(String ip) {
        this.ip = ip;
    }

    
    public Long getTime() {
        return time;
    }

    
    public void setTime(Long time) {
        this.time = time;
    }
}
