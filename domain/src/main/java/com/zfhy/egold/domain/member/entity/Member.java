package com.zfhy.egold.domain.member.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "member_member")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    private String account;

    
    @Column(name = "mobile_phone")
    private String mobilePhone;

    
    private String email;

    
    private String password;

    
    @Column(name = "deal_pwd")
    private String dealPwd;

    
    private String salt;

    /* 1:禁用 **/
    private String enable;

    
    @Column(name = "head_img")
    private String headImg;

    
    private Integer referee;

    
    @Column(name = "real_name_valid")
    private Integer realNameValid;

    
    @Column(name = "real_name")
    private String realName;


    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "bankcard_bind")
    private Integer bankcardBind;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    @Column(name = "bind_wechat")
    private Integer bindWechat;


    @Column(name = "terminal_type")
    private String terminalType;

    
    @Column(name = "terminal_id")
    private String terminalId;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getAccount() {
        return account;
    }



    
    public void setAccount(String account) {
        this.account = account;
    }

    
    public String getMobilePhone() {
        return mobilePhone;
    }

    
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getPassword() {
        return password;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }

    
    public String getDealPwd() {
        return dealPwd;
    }

    
    public void setDealPwd(String dealPwd) {
        this.dealPwd = dealPwd;
    }

    
    public String getSalt() {
        return salt;
    }

    
    public void setSalt(String salt) {
        this.salt = salt;
    }

    
    public String getEnable() {
        return enable;
    }

    
    public void setEnable(String enable) {
        this.enable = enable;
    }

    
    public String getHeadImg() {
        return headImg;
    }

    
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    
    public Integer getReferee() {
        return referee;
    }

    
    public void setReferee(Integer referee) {
        this.referee = referee;
    }

    
    public Integer getRealNameValid() {
        return realNameValid;
    }

    
    public void setRealNameValid(Integer realNameValid) {
        this.realNameValid = realNameValid;
    }

    
    public Date getCreateDate() {
        return createDate;
    }

    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    
    public Integer getBankcardBind() {
        return bankcardBind;
    }

    
    public void setBankcardBind(Integer bankcardBind) {
        this.bankcardBind = bankcardBind;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }


    public Integer getBindWechat() {
        return bindWechat;
    }

    public void setBindWechat(Integer bindWechat) {
        this.bindWechat = bindWechat;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
