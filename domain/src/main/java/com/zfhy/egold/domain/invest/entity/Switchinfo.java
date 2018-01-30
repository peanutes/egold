package com.zfhy.egold.domain.invest.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "invest_switchinfo")
public class Switchinfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "invest_id")
    private Integer investId;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "order_id")
    private Integer orderId;

    
    @Column(name = "gold_weight")
    private Double goldWeight;

    
    private Integer status;

    
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

    
    public Integer getInvestId() {
        return investId;
    }

    
    public void setInvestId(Integer investId) {
        this.investId = investId;
    }

    
    public Integer getMemberId() {
        return memberId;
    }

    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    
    public Integer getOrderId() {
        return orderId;
    }

    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    
    public Double getGoldWeight() {
        return goldWeight;
    }

    
    public void setGoldWeight(Double goldWeight) {
        this.goldWeight = goldWeight;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
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
