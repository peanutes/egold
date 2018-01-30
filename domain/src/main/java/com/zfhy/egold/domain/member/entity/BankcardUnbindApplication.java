package com.zfhy.egold.domain.member.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "member_bankcard_unbind_application")
public class BankcardUnbindApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "idcard_front_img")
    private String idcardFrontImg;

    
    @Column(name = "idcard_back_img")
    private String idcardBackImg;

    
    private Integer status;

    
    @Column(name = "real_name")
    private String realName;

    
    @Column(name = "origin_bank_card")
    private String originBankCard;

    
    private String mobile;

    
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

    
    public String getIdcardFrontImg() {
        return idcardFrontImg;
    }

    
    public void setIdcardFrontImg(String idcardFrontImg) {
        this.idcardFrontImg = idcardFrontImg;
    }

    
    public String getIdcardBackImg() {
        return idcardBackImg;
    }

    
    public void setIdcardBackImg(String idcardBackImg) {
        this.idcardBackImg = idcardBackImg;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
    }

    
    public String getRealName() {
        return realName;
    }

    
    public void setRealName(String realName) {
        this.realName = realName;
    }

    
    public String getOriginBankCard() {
        return originBankCard;
    }

    
    public void setOriginBankCard(String originBankCard) {
        this.originBankCard = originBankCard;
    }

    
    public String getMobile() {
        return mobile;
    }

    
    public void setMobile(String mobile) {
        this.mobile = mobile;
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
