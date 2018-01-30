package com.zfhy.egold.domain.fund.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "fund_save_gold")
public class SaveGold {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "save_time")
    private Date saveTime;

    
    @Column(name = "gold_weight")
    private Double goldWeight;

    
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

    
    public Date getSaveTime() {
        return saveTime;
    }

    
    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    
    public Double getGoldWeight() {
        return goldWeight;
    }

    
    public void setGoldWeight(Double goldWeight) {
        this.goldWeight = goldWeight;
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
