package com.zfhy.egold.domain.sys.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "sys_mail")
public class Mail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "mail_Title")
    private String mailTitle;

    
    @Column(name = "send_Time")
    private Date sendTime;

    
    private Integer sender;

    
    private Integer reciver;

    
    @Column(name = "mail_Type")
    private Integer mailType;

    
    @Column(name = "mail_Status")
    private Integer mailStatus;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    @Column(name = "mail_Content")
    private String mailContent;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getMailTitle() {
        return mailTitle;
    }

    
    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    
    public Date getSendTime() {
        return sendTime;
    }

    
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReciver() {
        return reciver;
    }

    public void setReciver(Integer reciver) {
        this.reciver = reciver;
    }

    
    public Integer getMailType() {
        return mailType;
    }

    
    public void setMailType(Integer mailType) {
        this.mailType = mailType;
    }

    
    public Integer getMailStatus() {
        return mailStatus;
    }

    
    public void setMailStatus(Integer mailStatus) {
        this.mailStatus = mailStatus;
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

    
    public String getMailContent() {
        return mailContent;
    }

    
    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }
}
