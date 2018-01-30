package com.zfhy.egold.domain.fund.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "fund_withdraw_gold")
public class WithdrawGold {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "apply_time")
    private Date applyTime;

    
    @Column(name = "withdraw_type")
    private String withdrawType;

    
    @Column(name = "withdraw_gold_weight")
    private Double withdrawGoldWeight;

    
    @Column(name = "withdraw_time")
    private Date withdrawTime;

    
    @Column(name = "deliver_no")
    private String deliverNo;

    
    @Column(name = "store_id")
    private Integer storeId;

    
    @Column(name = "store_name")
    private String storeName;

    
    @Column(name = "admin_id")
    private Integer adminId;

    
    private String operator;

    
    private String status;

    
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

    
    public Date getApplyTime() {
        return applyTime;
    }

    
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    
    public String getWithdrawType() {
        return withdrawType;
    }

    
    public void setWithdrawType(String withdrawType) {
        this.withdrawType = withdrawType;
    }

    
    public Double getWithdrawGoldWeight() {
        return withdrawGoldWeight;
    }

    
    public void setWithdrawGoldWeight(Double withdrawGoldWeight) {
        this.withdrawGoldWeight = withdrawGoldWeight;
    }

    
    public Date getWithdrawTime() {
        return withdrawTime;
    }

    
    public void setWithdrawTime(Date withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    
    public String getDeliverNo() {
        return deliverNo;
    }

    
    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }

    
    public Integer getStoreId() {
        return storeId;
    }

    
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    
    public String getStoreName() {
        return storeName;
    }

    
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    
    public Integer getAdminId() {
        return adminId;
    }

    
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    
    public String getOperator() {
        return operator;
    }

    
    public void setOperator(String operator) {
        this.operator = operator;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
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
