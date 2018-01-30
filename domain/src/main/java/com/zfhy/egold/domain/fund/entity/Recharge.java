package com.zfhy.egold.domain.fund.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "fund_recharge")
public class Recharge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "recharge_time")
    private Date rechargeTime;

    
    @Column(name = "recharge_type")
    private String rechargeType;

    
    @Column(name = "bank_name")
    private String bankName;

    
    @Column(name = "recharge_amount")
    private Double rechargeAmount;

    
    @Column(name = "recharge_fee")
    private Double rechargeFee;

    
    private String status;

    
    @Column(name = "pay_no")
    private String payNo;

    
    @Column(name = "pay_account")
    private String payAccount;

    
    private String ip;


    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;
    
    @Column(name = "pay_request_no")
    private String payRequestNo;



    @Transient
    private String mobile;

    
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

    
    public Date getRechargeTime() {
        return rechargeTime;
    }

    
    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    
    public String getRechargeType() {
        return rechargeType;
    }

    
    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    
    public String getBankName() {
        return bankName;
    }

    
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    
    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    
    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    
    public Double getRechargeFee() {
        return rechargeFee;
    }

    
    public void setRechargeFee(Double rechargeFee) {
        this.rechargeFee = rechargeFee;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getPayNo() {
        return payNo;
    }

    
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    
    public String getPayAccount() {
        return payAccount;
    }

    
    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    
    public String getIp() {
        return ip;
    }

    
    public void setIp(String ip) {
        this.ip = ip;
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


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPayRequestNo() {
        return payRequestNo;
    }

    public void setPayRequestNo(String payRequestNo) {
        this.payRequestNo = payRequestNo;
    }
}
