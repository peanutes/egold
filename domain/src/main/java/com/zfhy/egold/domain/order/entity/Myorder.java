package com.zfhy.egold.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_myorder")
public class Myorder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "relate_order_id")
    private String relateOrderId;

    
    @Column(name = "myorder_type")
    private Integer myorderType;

    
    @Column(name = "myorder_status")
    private Integer myorderStatus;

    
    @Column(name = "product_name")
    private String productName;

    
    @Column(name = "annual_rate")
    private Double annualRate;

    
    @Column(name = "order_total_amount")
    private Double orderTotalAmount;

    
    @Column(name = "current_price")
    private Double currentPrice;

    
    @Column(name = "gold_weight")
    private Double goldWeight;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    @Column(name = "withdraw_num")
    private String withdrawNum;

    private String receiver;

    @Column(name = "receiver_mobile")
    private String receiverMobile;


    @Column(name = "receiver_address")
    private String receiverAddress;

    @Column(name = "estimate_fall")
    private Integer estimateFall;


    
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

    
    public String getRelateOrderId() {
        return relateOrderId;
    }

    
    public void setRelateOrderId(String relateOrderId) {
        this.relateOrderId = relateOrderId;
    }

    
    public Integer getMyorderType() {
        return myorderType;
    }

    
    public void setMyorderType(Integer myorderType) {
        this.myorderType = myorderType;
    }

    
    public Integer getMyorderStatus() {
        return myorderStatus;
    }

    
    public void setMyorderStatus(Integer myorderStatus) {
        this.myorderStatus = myorderStatus;
    }

    
    public String getProductName() {
        return productName;
    }

    
    public void setProductName(String productName) {
        this.productName = productName;
    }

    
    public Double getAnnualRate() {
        return annualRate;
    }

    
    public void setAnnualRate(Double annualRate) {
        this.annualRate = annualRate;
    }

    
    public Double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    
    public void setOrderTotalAmount(Double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    
    public Double getCurrentPrice() {
        return currentPrice;
    }

    
    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    
    public Double getGoldWeight() {
        return goldWeight;
    }

    
    public void setGoldWeight(Double goldWeight) {
        this.goldWeight = goldWeight;
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

    public String getWithdrawNum() {
        return withdrawNum;
    }

    public void setWithdrawNum(String withdrawNum) {
        this.withdrawNum = withdrawNum;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getEstimateFall() {
        return estimateFall;
    }

    public void setEstimateFall(Integer estimateFall) {
        this.estimateFall = estimateFall;
    }
}
