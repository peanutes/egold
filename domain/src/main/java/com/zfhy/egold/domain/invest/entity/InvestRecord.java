package com.zfhy.egold.domain.invest.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "invest_invest_record")
public class InvestRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "product_id")
    private Integer productId;

    
    @Column(name = "product_name")
    private String productName;

    private Integer version;


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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    
    @Column(name = "product_type")

    private String productType;

    
    @Column(name = "invest_amount")
    private Double investAmount;

    
    @Column(name = "invest_gold_weight")
    private Double investGoldWeight;

    
    private Double price;


    
    private Double endPrice;

    
    @Column(name = "close_price")
    private Double closePrice;


    
    private String status;



    
    @Column(name = "effect_date")
    private Date effectDate;

    
    @Column(name = "deadline_date")
    private Date deadlineDate;
    
    @Column(name = "invest_deadline_date")
    private Date investDeadlineDate;

    
    @Column(name = "annual_rate")
    private Double annualRate;

    
    @Column(name = "additional_rate")
    private Double additionalRate;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    private String mobile;


    @Column(name = "invest_term")
    private Integer investTerm;


    @Column(name = "order_id")
    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

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

    
    public Integer getProductId() {
        return productId;
    }

    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    
    public String getProductName() {
        return productName;
    }

    
    public void setProductName(String productName) {
        this.productName = productName;
    }

    
    public Double getInvestAmount() {
        return investAmount;
    }

    
    public void setInvestAmount(Double investAmount) {
        this.investAmount = investAmount;
    }

    
    public Double getInvestGoldWeight() {
        return investGoldWeight;
    }

    
    public void setInvestGoldWeight(Double investGoldWeight) {
        this.investGoldWeight = investGoldWeight;
    }

    
    public Double getPrice() {
        return price;
    }

    
    public void setPrice(Double price) {
        this.price = price;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public Date getEffectDate() {
        return effectDate;
    }

    
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    
    public Date getDeadlineDate() {
        return deadlineDate;
    }

    
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    
    public Double getAnnualRate() {
        return annualRate;
    }

    
    public void setAnnualRate(Double annualRate) {
        this.annualRate = annualRate;
    }

    
    public Double getAdditionalRate() {
        return additionalRate;
    }

    
    public void setAdditionalRate(Double additionalRate) {
        this.additionalRate = additionalRate;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Double endPrice) {
        this.endPrice = endPrice;
    }

    public Integer getInvestTerm() {

        return investTerm;
    }

    public void setInvestTerm(Integer investTerm) {
        this.investTerm = investTerm;
    }

    public Date getInvestDeadlineDate() {
        return investDeadlineDate;
    }

    public void setInvestDeadlineDate(Date investDeadlineDate) {
        this.investDeadlineDate = investDeadlineDate;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }
}
