package com.zfhy.egold.domain.invest.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "invest_income_detail")
public class IncomeDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "invest_id")
    private Integer investId;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    private Double income;

    
    @Column(name = "income_time")
    private Date incomeTime;

    
    @Column(name = "income_date")
    private Date incomeDate;

    
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

    
    public Integer getInvestId() {
        return investId;
    }

    
    public void setInvestId(Integer investId) {
        this.investId = investId;
    }

    
    public Integer getMemberId() {
        return memberId;
    }

    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    
    public Double getIncome() {
        return income;
    }

    
    public void setIncome(Double income) {
        this.income = income;
    }

    
    public Date getIncomeTime() {
        return incomeTime;
    }

    
    public void setIncomeTime(Date incomeTime) {
        this.incomeTime = incomeTime;
    }

    
    public Date getIncomeDate() {
        return incomeDate;
    }

    
    public void setIncomeDate(Date incomeDate) {
        this.incomeDate = incomeDate;
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
