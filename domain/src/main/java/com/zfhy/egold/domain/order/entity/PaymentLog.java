package com.zfhy.egold.domain.order.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "order_payment_log")
public class PaymentLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "pay_sn")
    private String paySn;

    
    @Column(name = "pay_request_no")
    private String payRequestNo;

    
    private Double amount;

    
    @Column(name = "recharge_or_pay")
    private Integer rechargeOrPay;

    
    private Integer status;

    
    @Column(name = "errro_msg")
    private String errroMsg;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    @Transient
    private String mobile;

    
    @Transient
    private String realName;


    @Column(name = "bank_card")
    private String bankCard;


    @Column(name = "bank_name")
    private String bankName;

    
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

    
    public String getPaySn() {
        return paySn;
    }

    
    public void setPaySn(String paySn) {
        this.paySn = paySn;
    }

    
    public String getPayRequestNo() {
        return payRequestNo;
    }

    
    public void setPayRequestNo(String payRequestNo) {
        this.payRequestNo = payRequestNo;
    }

    
    public Double getAmount() {
        return amount;
    }

    
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    
    public Integer getRechargeOrPay() {
        return rechargeOrPay;
    }

    
    public void setRechargeOrPay(Integer rechargeOrPay) {
        this.rechargeOrPay = rechargeOrPay;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
    }

    
    public String getErrroMsg() {
        return errroMsg;
    }

    
    public void setErrroMsg(String errroMsg) {
        this.errroMsg = errroMsg;
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

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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
}
