package com.zfhy.egold.domain.fund.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Table(name = "fund_withdraw")
public class Withdraw {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "withdraw_amount")
    private Double withdrawAmount;

    
    @Column(name = "pay_amount")
    private Double payAmount;

    
    @Column(name = "withdraw_account")
    private String withdrawAccount;

    
    private String mobile;

    
    @Column(name = "real_name")
    private String realName;

    
    @Column(name = "withdraw_fee")
    private Double withdrawFee;

    
    private String status;

    
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    @Column(name = "apply_time")
    private Date applyTime;

    
    @Column(name = "bank_card_id")
    private Integer bankCardId;

    
    @Column(name = "admin_id")
    private Integer adminId;

    
    private String operator;

    
    @Column(name = "auditor_time")
    private Date auditorTime;

    
    private String ip;

    
    private Integer version;

    
    private String province;

    
    private String city;

    
    @Column(name = "bank_no")
    private String bankNo;

    
    @Column(name = "bank_name")
    private String bankName;

    
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

    
    public Double getWithdrawAmount() {
        return withdrawAmount;
    }

    
    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    
    public Double getPayAmount() {
        return payAmount;
    }

    
    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    
    public String getWithdrawAccount() {
        return withdrawAccount;
    }

    
    public void setWithdrawAccount(String withdrawAccount) {
        this.withdrawAccount = withdrawAccount;
    }

    
    public String getMobile() {
        return mobile;
    }

    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    public String getRealName() {
        return realName;
    }

    
    public void setRealName(String realName) {
        this.realName = realName;
    }

    
    public Double getWithdrawFee() {
        return withdrawFee;
    }

    
    public void setWithdrawFee(Double withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    

    public Date getApplyTime() {
        return applyTime;
    }

    
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    
    public Integer getBankCardId() {
        return bankCardId;
    }

    
    public void setBankCardId(Integer bankCardId) {
        this.bankCardId = bankCardId;
    }

    
    public Integer getAdminId() {
        return adminId;
    }

    
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    
    public String getOperator() {
        return operator;
    }

    
    public void setOperator(String operator) {
        this.operator = operator;
    }

    
    public Date getAuditorTime() {
        return auditorTime;
    }

    
    public void setAuditorTime(Date auditorTime) {
        this.auditorTime = auditorTime;
    }

    
    public String getIp() {
        return ip;
    }

    
    public void setIp(String ip) {
        this.ip = ip;
    }

    
    public Integer getVersion() {
        return version;
    }

    
    public void setVersion(Integer version) {
        this.version = version;
    }

    
    public String getProvince() {
        return province;
    }

    
    public void setProvince(String province) {
        this.province = province;
    }

    
    public String getCity() {
        return city;
    }

    
    public void setCity(String city) {
        this.city = city;
    }

    
    public String getBankNo() {
        return bankNo;
    }

    
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    
    public String getBankName() {
        return bankName;
    }

    
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
