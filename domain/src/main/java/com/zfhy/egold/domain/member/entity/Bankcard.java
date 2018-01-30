package com.zfhy.egold.domain.member.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "member_bankcard")
public class Bankcard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "bank_account")
    private String bankAccount;

    
    @Column(name = "bank_card")
    private String bankCard;

    
    @Column(name = "bank_name")
    private String bankName;


    
    @Column(name = "bank_code")
    private String bankCode;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    @Column(name = "bank_logo")
    private String bankLogo;


    @Column(name = "bind_mobile")
    private String bindMobile;

    
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

    
    public String getBankAccount() {
        return bankAccount;
    }

    
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
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


    
    public String getBankCode() {
        return bankCode;
    }

    
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    
    public Date     getCreateDate() {
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

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(String bindMobile) {
        this.bindMobile = bindMobile;
    }
}
