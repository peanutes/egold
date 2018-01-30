package com.zfhy.egold.domain.invest.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Table(name = "invest_financial_product")
public class FinancialProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "product_name")
    private String productName;

    
    @Column(name = "annual_rate")
    private Double annualRate;

    
    @Column(name = "annual_rate_max")
    private Double annualRateMax;

    
    @Column(name = "min_amount")
    private Double minAmount;

    
    @Column(name = "max_amount")
    private Double maxAmount;

    
    private Double price;

    
    @Column(name = "market_price")
    private Double marketPrice;

    
    private Integer term;

    
    @Column(name = "product_type")
    private String productType;

    /* 1:新手标 0：普通标 **/
    @Column(name = "new_user")
    private String newUser;

    
    @Column(name = "gold_type")
    private String goldType;

    
    @Column(name = "buy_fee")
    private Double buyFee;

    
    @Column(name = "sale_fee")
    private Double saleFee;

    
    @Column(name = "income_struct")
    private String incomeStruct;

    
    @Column(name = "interest_desc")
    private String interestDesc;

    
    @Column(name = "interest_cal")
    private String interestCal;

    
    @Column(name = "risk_grade")
    private String riskGrade;

    /* {@link com.zfhy.egold.domain.invest.dto.ProductStatus} **/
    private String status;

    
    @Column(name = "key_point")
    private String keyPoint;

    
    private Integer sort;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;


    
    @Column(name = "min_amount_desc")
    private String minAmountDesc;


    
    @Column(name = "buy_sale_desc")
    private String buySaleDesc;



    
    @Column(name = "term_desc")
    private String termDesc;



    
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    @Column(name = "dead_line_date")
    private Date deadLineDate;


    @Column(name = "rise_or_fall")
    private int riseOrFall;


    @Column(name = "aim_amount")
    private Double aimAmount;

    @Column(name = "had_invest_amount")
    private Double hadInvestAmount;

    @Column(name = "invest_status")
    private Integer investStatus;



    @Column(name = "version")
    private Integer version;

    @DateTimeFormat(pattern = "yy-MM-dd")
    @Column(name = "interest_begin_date")
    private Date interestBeginDate;


    @Column(name = "expired_deal_desc")
    private String expiredDealDesc;


    @Column(name = "deadline_price")
    private Double deadlinePrice;


    @Column(name = "close_price")
    private Double closePrice;

    public Date getInterestBeginDate() {
        return interestBeginDate;
    }

    public void setInterestBeginDate(Date interestBeginDate) {
        this.interestBeginDate = interestBeginDate;
    }


    public String getExpiredDealDesc() {
        return expiredDealDesc;
    }

    public void setExpiredDealDesc(String expiredDealDesc) {
        this.expiredDealDesc = expiredDealDesc;
    }

    public Double getHadInvestAmount() {
        return hadInvestAmount;
    }

    public void setHadInvestAmount(Double hadInvestAmount) {
        this.hadInvestAmount = hadInvestAmount;
    }

    public Integer getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(Integer investStatus) {
        this.investStatus = investStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
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

    
    public Double getAnnualRateMax() {
        return annualRateMax;
    }

    
    public void setAnnualRateMax(Double annualRateMax) {
        this.annualRateMax = annualRateMax;
    }

    
    public Double getMinAmount() {
        return minAmount;
    }

    
    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    
    public Double getMaxAmount() {
        return maxAmount;
    }

    
    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    
    public Double getPrice() {
        return price;
    }

    
    public void setPrice(Double price) {
        this.price = price;
    }

    
    public Double getMarketPrice() {
        return marketPrice;
    }

    
    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    
    public Integer getTerm() {
        return term;
    }

    
    public void setTerm(Integer term) {
        this.term = term;
    }

    
    public String getProductType() {
        return productType;
    }

    
    public void setProductType(String productType) {
        this.productType = productType;
    }

    
    public String getNewUser() {
        return newUser;
    }

    
    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    
    public String getGoldType() {
        return goldType;
    }

    
    public void setGoldType(String goldType) {
        this.goldType = goldType;
    }

    
    public Double getBuyFee() {
        return buyFee;
    }

    
    public void setBuyFee(Double buyFee) {
        this.buyFee = buyFee;
    }

    
    public Double getSaleFee() {
        return saleFee;
    }

    
    public void setSaleFee(Double saleFee) {
        this.saleFee = saleFee;
    }

    
    public String getIncomeStruct() {
        return incomeStruct;
    }

    
    public void setIncomeStruct(String incomeStruct) {
        this.incomeStruct = incomeStruct;
    }

    
    public String getInterestDesc() {
        return interestDesc;
    }

    
    public void setInterestDesc(String interestDesc) {
        this.interestDesc = interestDesc;
    }

    
    public String getInterestCal() {
        return interestCal;
    }

    
    public void setInterestCal(String interestCal) {
        this.interestCal = interestCal;
    }

    
    public String getRiskGrade() {
        return riskGrade;
    }

    
    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getKeyPoint() {
        return keyPoint;
    }

    
    public void setKeyPoint(String keyPoint) {
        this.keyPoint = keyPoint;
    }

    
    public Integer getSort() {
        return sort;
    }

    
    public void setSort(Integer sort) {
        this.sort = sort;
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

    public String getMinAmountDesc() {
        return minAmountDesc;
    }

    public void setMinAmountDesc(String minAmountDesc) {
        this.minAmountDesc = minAmountDesc;
    }

    public String getBuySaleDesc() {
        return buySaleDesc;
    }

    public void setBuySaleDesc(String buySaleDesc) {
        this.buySaleDesc = buySaleDesc;
    }

    public String getTermDesc() {
        return termDesc;
    }

    public void setTermDesc(String termDesc) {
        this.termDesc = termDesc;
    }

    public Date getDeadLineDate() {
        return deadLineDate;
    }

    public void setDeadLineDate(Date deadLineDate) {
        this.deadLineDate = deadLineDate;
    }

    public int getRiseOrFall() {
        return riseOrFall;
    }

    public void setRiseOrFall(int riseOrFall) {
        this.riseOrFall = riseOrFall;
    }

    public Double getAimAmount() {
        return aimAmount;
    }

    public void setAimAmount(Double aimAmount) {
        this.aimAmount = aimAmount;
    }

    public Double getDeadlinePrice() {
        return deadlinePrice;
    }

    public void setDeadlinePrice(Double deadlinePrice) {
        this.deadlinePrice = deadlinePrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }
}
