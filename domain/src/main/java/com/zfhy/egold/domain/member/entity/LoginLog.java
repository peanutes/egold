package com.zfhy.egold.domain.member.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "member_login_log")
public class LoginLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "terminal_type")
    private String terminalType;

    
    @Column(name = "terminal_id")
    private String terminalId;

    
    @Column(name = "login_time")
    private Date loginTime;

    
    @Column(name = "login_ip")
    private String loginIp;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getMemberId() {
        return memberId;
    }

    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
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

    
    public Date getLoginTime() {
        return loginTime;
    }

    
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    
    public String getLoginIp() {
        return loginIp;
    }

    
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
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
}
