package com.zfhy.egold.domain.fund.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "fund_member_fund")
public class MemberFund {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "cash_balance")
    private Double cashBalance;

    
    @Column(name = "term_gold_balance")
    private Double termGoldBalance;

    
    @Column(name = "current_gold_balance")
    private Double currentGoldBalance;

    
    @Column(name = "invest_balance")
    private Double investBalance;

    
    private Integer version;

    
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

    
    @Transient
    private Double totalAssets;


    
    @Transient
    private Double onWithdraw;

    
    @Transient
    private Double interest;


    
    @Transient
    private Double coupon;



    
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

    
    public Double getCashBalance() {
        return cashBalance;
    }

    
    public void setCashBalance(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

    
    public Double getTermGoldBalance() {
        return termGoldBalance;
    }

    
    public void setTermGoldBalance(Double termGoldBalance) {
        this.termGoldBalance = termGoldBalance;
    }

    
    public Double getCurrentGoldBalance() {
        return currentGoldBalance;
    }

    
    public void setCurrentGoldBalance(Double currentGoldBalance) {
        this.currentGoldBalance = currentGoldBalance;
    }

    
    public Double getInvestBalance() {
        return investBalance;
    }

    
    public void setInvestBalance(Double investBalance) {
        this.investBalance = investBalance;
    }

    
    public Integer getVersion() {
        return version;
    }

    
    public void setVersion(Integer version) {
        this.version = version;
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

    public Double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public Double getOnWithdraw() {
        return onWithdraw;
    }

    public void setOnWithdraw(Double onWithdraw) {
        this.onWithdraw = onWithdraw;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getCoupon() {
        return coupon;
    }

    public void setCoupon(Double coupon) {
        this.coupon = coupon;
    }
}
