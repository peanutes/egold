package com.zfhy.egold.domain.member.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "member_change_mobile_application")
public class ChangeMobileApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "idcard_hand_img")
    private String idcardHandImg;

    
    @Column(name = "idcard_front_img")
    private String idcardFrontImg;

    
    @Column(name = "idcard_back_img")
    private String idcardBackImg;

    
    @Column(name = "bank_card_img")
    private String bankCardImg;

    
    @Column(name = "new_mobile")
    private String newMobile;

    
    @Column(name = "old_mobile")
    private String oldMobile;

    
    private Integer status;

    
    private String auditor;

    
    @Column(name = "audit_date")
    private Date auditDate;

    
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

    
    public String getIdcardHandImg() {
        return idcardHandImg;
    }

    
    public void setIdcardHandImg(String idcardHandImg) {
        this.idcardHandImg = idcardHandImg;
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

    
    public String getBankCardImg() {
        return bankCardImg;
    }

    
    public void setBankCardImg(String bankCardImg) {
        this.bankCardImg = bankCardImg;
    }

    
    public String getNewMobile() {
        return newMobile;
    }

    
    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile;
    }

    
    public String getOldMobile() {
        return oldMobile;
    }

    
    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
    }

    
    public String getAuditor() {
        return auditor;
    }

    
    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    
    public Date getAuditDate() {
        return auditDate;
    }

    
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
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
