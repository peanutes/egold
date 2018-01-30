package com.zfhy.egold.domain.member.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "member_bind_bankcard_log")
public class BindBankcardLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "request_no")
    private String requestNo;

    
    @Column(name = "card_no")
    private String cardNo;

    
    @Column(name = "real_name")
    private String realName;

    
    @Column(name = "idcard_no")
    private String idcardNo;

    
    private Integer status;

    
    private String mobile;

    
    @Column(name = "error_msg")
    private String errorMsg;

    
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

    
    public String getRequestNo() {
        return requestNo;
    }

    
    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    
    public String getCardNo() {
        return cardNo;
    }

    
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    
    public String getRealName() {
        return realName;
    }

    
    public void setRealName(String realName) {
        this.realName = realName;
    }

    
    public String getIdcardNo() {
        return idcardNo;
    }

    
    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
    }

    
    public String getMobile() {
        return mobile;
    }

    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    public String getErrorMsg() {
        return errorMsg;
    }

    
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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
