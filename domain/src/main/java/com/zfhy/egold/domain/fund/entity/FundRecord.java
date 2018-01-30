package com.zfhy.egold.domain.fund.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "fund_fund_record")
public class FundRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "operate_money")
    private Double operateMoney;

    
    @Column(name = "gold_weight")
    private Double goldWeight;

    
    @Column(name = "product_name")
    private String productName;


    
    @Column(name = "account")
    private String account;

    
    @Column(name = "operate_id")
    private Integer operateId;

    
    @Column(name = "display_label")
    private String displayLabel;

    
    @Column(name = "operate_type")
    private String operateType;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    @Column(name = "invest_price")
    private Double investPrice;

    public Double getInvestPrice() {
        return investPrice;
    }

    public void setInvestPrice(Double investPrice) {
        this.investPrice = investPrice;
    }

    
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

    
    public Double getOperateMoney() {
        return operateMoney;
    }

    
    public void setOperateMoney(Double operateMoney) {
        this.operateMoney = operateMoney;
    }

    
    public Double getGoldWeight() {
        return goldWeight;
    }

    
    public void setGoldWeight(Double goldWeight) {
        this.goldWeight = goldWeight;
    }

    
    public String getProductName() {
        return productName;
    }

    
    public void setProductName(String productName) {
        this.productName = productName;
    }

    
    public Integer getOperateId() {
        return operateId;
    }

    
    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    
    public String getDisplayLabel() {
        return displayLabel;
    }

    
    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    
    public String getOperateType() {
        return operateType;
    }

    
    public void setOperateType(String operateType) {
        this.operateType = operateType;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
