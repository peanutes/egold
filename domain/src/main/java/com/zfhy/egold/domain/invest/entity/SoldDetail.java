package com.zfhy.egold.domain.invest.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "invest_sold_detail")
public class SoldDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "sold_time")
    private Date soldTime;

    
    @Column(name = "gold_weight")
    private Double goldWeight;

    
    @Column(name = "product_name")
    private String productName;

    
    private Double price;

    
    @Column(name = "income_amount")
    private Double incomeAmount;

    
    @Column(name = "total_amount")
    private Double totalAmount;

    
    private Double fee;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;




    @Column(name = "estimate_fall")
    private Integer estimateFall;


    
    @Column(name = "buy_price")
    private Double buyPrice;


    
    @Column(name = "profit_or_loss")
    private Double profitOrLoss;


    
    @Column(name = "term_gold_sold")
    private Integer termGoldSold;

    public Integer getEstimateFall() {
        return estimateFall;
    }

    public void setEstimateFall(Integer estimateFall) {
        this.estimateFall = estimateFall;
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

    
    public Date getSoldTime() {
        return soldTime;
    }

    
    public void setSoldTime(Date soldTime) {
        this.soldTime = soldTime;
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

    
    public Double getPrice() {
        return price;
    }

    
    public void setPrice(Double price) {
        this.price = price;
    }

    
    public Double getIncomeAmount() {
        return incomeAmount;
    }

    
    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    
    public Double getTotalAmount() {
        return totalAmount;
    }

    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    
    public Double getFee() {
        return fee;
    }

    
    public void setFee(Double fee) {
        this.fee = fee;
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

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getProfitOrLoss() {
        return profitOrLoss;
    }

    public void setProfitOrLoss(Double profitOrLoss) {
        this.profitOrLoss = profitOrLoss;
    }

    public Integer getTermGoldSold() {
        return termGoldSold;
    }

    public void setTermGoldSold(Integer termGoldSold) {
        this.termGoldSold = termGoldSold;
    }
}
