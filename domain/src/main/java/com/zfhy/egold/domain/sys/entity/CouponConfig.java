package com.zfhy.egold.domain.sys.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "sys_coupon_config")
public class CouponConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 使用场景(1：新用户注册，2：邀请好友绑卡-邀请人, 
            3：邀请好友绑卡-被新邀请人，4：邀请好友首投-邀请人,
            5：邀请好友全年返利-邀请人，6首投，7.实物奖励)
     */
    @Column(name = "use_scene")
    private Integer useScene;

    
    private Integer type;

    
    @Column(name = "coupon_amount")
    private Double couponAmount;

    
    @Column(name = "effect_day")
    private Integer effectDay;

    
    @Column(name = "invest_amount_min")
    private Double investAmountMin;

    
    @Column(name = "invest_amount_min_desc")
    private String investAmountMinDesc;

    
    @Column(name = "invest_deadline_min")
    private Integer investDeadlineMin;

    
    @Column(name = "invest_deadline_min_desc")
    private String investDeadlineMinDesc;

    
    @Column(name = "condition_invest_amount")
    private Double conditionInvestAmount;

    
    @Column(name = "condition_des")
    private String conditionDes;

    
    @Column(name = "product_type")
    private String productType;

    
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

    /**
     * 获取使用场景(1：新用户注册，2：邀请好友绑卡-邀请人, 
            3：邀请好友绑卡-被新邀请人，4：邀请好友首投-邀请人,
            5：邀请好友全年返利-邀请人，6首投，7.实物奖励)
     *
     * @return use_scene - 使用场景(1：新用户注册，2：邀请好友绑卡-邀请人, 
            3：邀请好友绑卡-被新邀请人，4：邀请好友首投-邀请人,
            5：邀请好友全年返利-邀请人，6首投，7.实物奖励)
     */
    public Integer getUseScene() {
        return useScene;
    }

    /**
     * 设置使用场景(1：新用户注册，2：邀请好友绑卡-邀请人, 
            3：邀请好友绑卡-被新邀请人，4：邀请好友首投-邀请人,
            5：邀请好友全年返利-邀请人，6首投，7.实物奖励)
     *
     * @param useScene 使用场景(1：新用户注册，2：邀请好友绑卡-邀请人, 
            3：邀请好友绑卡-被新邀请人，4：邀请好友首投-邀请人,
            5：邀请好友全年返利-邀请人，6首投，7.实物奖励)
     */
    public void setUseScene(Integer useScene) {
        this.useScene = useScene;
    }

    
    public Integer getType() {
        return type;
    }

    
    public void setType(Integer type) {
        this.type = type;
    }

    
    public Double getCouponAmount() {
        return couponAmount;
    }

    
    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    
    public Integer getEffectDay() {
        return effectDay;
    }

    
    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    
    public Double getInvestAmountMin() {
        return investAmountMin;
    }

    
    public void setInvestAmountMin(Double investAmountMin) {
        this.investAmountMin = investAmountMin;
    }

    
    public String getInvestAmountMinDesc() {
        return investAmountMinDesc;
    }

    
    public void setInvestAmountMinDesc(String investAmountMinDesc) {
        this.investAmountMinDesc = investAmountMinDesc;
    }

    
    public Integer getInvestDeadlineMin() {
        return investDeadlineMin;
    }

    
    public void setInvestDeadlineMin(Integer investDeadlineMin) {
        this.investDeadlineMin = investDeadlineMin;
    }

    
    public String getInvestDeadlineMinDesc() {
        return investDeadlineMinDesc;
    }

    
    public void setInvestDeadlineMinDesc(String investDeadlineMinDesc) {
        this.investDeadlineMinDesc = investDeadlineMinDesc;
    }

    
    public Double getConditionInvestAmount() {
        return conditionInvestAmount;
    }

    
    public void setConditionInvestAmount(Double conditionInvestAmount) {
        this.conditionInvestAmount = conditionInvestAmount;
    }

    
    public String getConditionDes() {
        return conditionDes;
    }

    
    public void setConditionDes(String conditionDes) {
        this.conditionDes = conditionDes;
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
}
