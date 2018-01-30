package com.zfhy.egold.domain.invest.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "invest_member_coupon")
public class MemberCoupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "invest_id")
    private Integer investId;

    /* 1:红包 2：优惠券 **/
    private Integer type;

    /* 1:失效 2：已使用  3： **/
    private Integer status;

    
    @Column(name = "coupon_amount")
    private Double couponAmount;

    
    @Column(name = "insert_time")
    private Date insertTime;

    
    @Column(name = "begin_time")
    private Date beginTime;

    
    @Column(name = "end_time")
    private Date endTime;

    
    @Column(name = "use_time")
    private Date useTime;

    
    @Column(name = "invest_amount")
    private Double investAmount;

    
    @Column(name = "invest_amount_min")
    private Double investAmountMin;

    /* 投资截止期限，天数 **/
    @Column(name = "invest_dealine_min")
    private Integer investDealineMin;

    
    @Column(name = "product_type")
    private String productType;


    
    @Column(name = "invest_amount_min_desc")
    private String investAmountMinDesc;


    
    @Column(name = "invest_deadline_min_desc")
    private String investDeadlineMinDesc;

    
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

    
    public Integer getMemberId() {
        return memberId;
    }

    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    
    public Integer getInvestId() {
        return investId;
    }

    
    public void setInvestId(Integer investId) {
        this.investId = investId;
    }

    
    public Integer getType() {
        return type;
    }

    
    public void setType(Integer type) {
        this.type = type;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
    }

    
    public Double getCouponAmount() {
        return couponAmount;
    }

    
    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    
    public Date getInsertTime() {
        return insertTime;
    }

    
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    
    public Date getBeginTime() {
        return beginTime;
    }

    
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    
    public Date getEndTime() {
        return endTime;
    }

    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    
    public Date getUseTime() {
        return useTime;
    }

    
    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    
    public Double getInvestAmount() {
        return investAmount;
    }

    
    public void setInvestAmount(Double investAmount) {
        this.investAmount = investAmount;
    }

    
    public Double getInvestAmountMin() {
        return investAmountMin;
    }

    
    public void setInvestAmountMin(Double investAmountMin) {
        this.investAmountMin = investAmountMin;
    }

    
    public Integer getInvestDealineMin() {
        return investDealineMin;
    }

    
    public void setInvestDealineMin(Integer investDealineMin) {
        this.investDealineMin = investDealineMin;
    }

    
    public String getProductType() {
        return productType;
    }

    
    public void setProductType(String productType) {
        this.productType = productType;
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

    public String getInvestAmountMinDesc() {
        return investAmountMinDesc;
    }

    public void setInvestAmountMinDesc(String investAmountMinDesc) {
        this.investAmountMinDesc = investAmountMinDesc;
    }

    public String getInvestDeadlineMinDesc() {
        return investDeadlineMinDesc;
    }

    public void setInvestDeadlineMinDesc(String investDeadlineMinDesc) {
        this.investDeadlineMinDesc = investDeadlineMinDesc;
    }
}
