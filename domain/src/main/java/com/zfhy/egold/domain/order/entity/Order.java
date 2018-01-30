package com.zfhy.egold.domain.order.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "order_order")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "product_id")
    private Integer productId;

    
    @Column(name = "order_sn")
    private String orderSn;

    
    @Column(name = "gold_weight")
    private Double goldWeight;

    
    @Column(name = "pay_type")
    private Integer payType;

    
    @Column(name = "total_amount")
    private Double totalAmount;

    
    @Column(name = "balance_pay_amount")
    private Double balancePayAmount;

    
    @Column(name = "finance_product_type")
    private String financeProductType;



    
    @Column(name = "product_type")
    private Integer productType;

    
    @Column(name = "product_name")
    private String productName;

    
    @Column(name = "current_gold_price")
    private Double currentGoldPrice;

    
    @Column(name = "should_pay_amount")
    private Double shouldPayAmount;

    
    private Integer status;

    
    @Column(name = "cash_coupon_id")
    private Integer cashCouponId;

    
    @Column(name = "discount_coupon_id")
    private Integer discountCouponId;

    
    @Column(name = "cash_coupon_amount")
    private Double cashCouponAmount;

    
    @Column(name = "discount_coupon_rate")
    private Double discountCouponRate;

    
    @Column(name = "switch_into")
    private Integer switchInto;

    
    private Integer version;

    
    @Column(name = "request_pay_time")
    private Date requestPayTime;

    
    @Column(name = "create_date")
    private Date createDate;


    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;




    @Column(name = "terminal_id")
    private String terminalId;

    @Column(name = "terminal_type")
    private String terminalType;

    @Column(name = "estimate_fall")
    private Integer estimateFall;


    public Integer getEstimateFall() {
        return estimateFall;
    }

    public void setEstimateFall(Integer estimateFall) {
        this.estimateFall = estimateFall;
    }

    
    @Transient
    private Double annualRate;

    
    @Transient
    private Double discountCoupon;

    
    @Transient
    private Double cashCoupon;

    @Transient
    private String mobile;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
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

    
    public Integer getProductId() {
        return productId;
    }

    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    
    public String getOrderSn() {
        return orderSn;
    }

    
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    
    public Double getGoldWeight() {
        return goldWeight;
    }

    
    public void setGoldWeight(Double goldWeight) {
        this.goldWeight = goldWeight;
    }

    
    public Integer getPayType() {
        return payType;
    }

    
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    
    public Double getTotalAmount() {
        return totalAmount;
    }

    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    
    public Double getBalancePayAmount() {
        return balancePayAmount;
    }

    
    public void setBalancePayAmount(Double balancePayAmount) {
        this.balancePayAmount = balancePayAmount;
    }

    
    public Integer getProductType() {
        return productType;
    }

    
    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    
    public String getProductName() {
        return productName;
    }

    
    public void setProductName(String productName) {
        this.productName = productName;
    }

    
    public Double getCurrentGoldPrice() {
        return currentGoldPrice;
    }

    
    public void setCurrentGoldPrice(Double currentGoldPrice) {
        this.currentGoldPrice = currentGoldPrice;
    }

    
    public Double getShouldPayAmount() {
        return shouldPayAmount;
    }

    
    public void setShouldPayAmount(Double shouldPayAmount) {
        this.shouldPayAmount = shouldPayAmount;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
    }

    
    public Integer getCashCouponId() {
        return cashCouponId;
    }

    
    public void setCashCouponId(Integer cashCouponId) {
        this.cashCouponId = cashCouponId;
    }

    
    public Integer getDiscountCouponId() {
        return discountCouponId;
    }

    
    public void setDiscountCouponId(Integer discountCouponId) {
        this.discountCouponId = discountCouponId;
    }

    
    public Double getCashCouponAmount() {
        return cashCouponAmount;
    }

    
    public void setCashCouponAmount(Double cashCouponAmount) {
        this.cashCouponAmount = cashCouponAmount;
    }

    
    public Double getDiscountCouponRate() {
        return discountCouponRate;
    }

    
    public void setDiscountCouponRate(Double discountCouponRate) {
        this.discountCouponRate = discountCouponRate;
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

    public Integer getSwitchInto() {
        return switchInto;
    }

    public void setSwitchInto(Integer switchInto) {
        this.switchInto = switchInto;
    }

    public String getFinanceProductType() {
        return financeProductType;
    }

    public void setFinanceProductType(String financeProductType) {
        this.financeProductType = financeProductType;
    }

    public Date getRequestPayTime() {
        return requestPayTime;
    }

    public void setRequestPayTime(Date requestPayTime) {
        this.requestPayTime = requestPayTime;
    }


    public Double getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(Double annualRate) {
        this.annualRate = annualRate;
    }

    public Double getDiscountCoupon() {
        return discountCoupon;
    }

    public void setDiscountCoupon(Double discountCoupon) {
        this.discountCoupon = discountCoupon;
    }

    public Double getCashCoupon() {
        return cashCoupon;
    }

    public void setCashCoupon(Double cashCoupon) {
        this.cashCoupon = cashCoupon;
    }
}
