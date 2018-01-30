package com.zfhy.egold.domain.goods.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "goods_goods_order")
public class GoodsOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "order_sn")
    private String orderSn;

    
    @Column(name = "sup_id")
    private Integer supId;

    
    @Column(name = "sku_id")
    private Integer skuId;

    
    @Column(name = "goods_name")
    private String goodsName;

    
    @Column(name = "goods_number")
    private Integer goodsNumber;

    
    @Column(name = "order_amount")
    private Double orderAmount;

    
    private Double price;

    
    @Column(name = "express_fee")
    private Double expressFee;

    
    @Column(name = "insurance_fee")
    private Double insuranceFee;

    
    @Column(name = "balance_amount")
    private Double balanceAmount;

    
    @Column(name = "should_pay")
    private Double shouldPay;

    
    @Column(name = "address_id")
    private Integer addressId;

    
    private String address;

    
    private Integer status;

    
    private Integer version;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    @Column(name = "address_receiver")
    private String addressReceiver;

    
    @Column(name = "address_receiver_mobile")
    private String addressReceiverMobile;

    
    @Column(name = "man_made_fee")
    private Double manMadeFee;


    @Column(name = "gold_weight")
    private Double goldWeight;

    @Transient
    private String mobile;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    
    public String getOrderSn() {
        return orderSn;
    }

    
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    
    public Integer getSupId() {
        return supId;
    }

    
    public void setSupId(Integer supId) {
        this.supId = supId;
    }

    
    public Integer getSkuId() {
        return skuId;
    }

    
    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    
    public String getGoodsName() {
        return goodsName;
    }

    
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    
    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    
    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    
    public Double getOrderAmount() {
        return orderAmount;
    }

    
    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    
    public Double getPrice() {
        return price;
    }

    
    public void setPrice(Double price) {
        this.price = price;
    }

    
    public Double getExpressFee() {
        return expressFee;
    }

    
    public void setExpressFee(Double expressFee) {
        this.expressFee = expressFee;
    }

    
    public Double getInsuranceFee() {
        return insuranceFee;
    }

    
    public void setInsuranceFee(Double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    
    public Double getBalanceAmount() {
        return balanceAmount;
    }

    
    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    
    public Double getShouldPay() {
        return shouldPay;
    }

    
    public void setShouldPay(Double shouldPay) {
        this.shouldPay = shouldPay;
    }

    
    public Integer getAddressId() {
        return addressId;
    }

    
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    
    public String getAddress() {
        return address;
    }

    
    public void setAddress(String address) {
        this.address = address;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
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

    
    public String getAddressReceiver() {
        return addressReceiver;
    }

    
    public void setAddressReceiver(String addressReceiver) {
        this.addressReceiver = addressReceiver;
    }

    
    public String getAddressReceiverMobile() {
        return addressReceiverMobile;
    }

    
    public void setAddressReceiverMobile(String addressReceiverMobile) {
        this.addressReceiverMobile = addressReceiverMobile;
    }

    public Double getManMadeFee() {
        return manMadeFee;
    }

    public void setManMadeFee(Double manMadeFee) {
        this.manMadeFee = manMadeFee;
    }

    public Double getGoldWeight() {
        return goldWeight;
    }

    public void setGoldWeight(Double goldWeight) {
        this.goldWeight = goldWeight;
    }
}
