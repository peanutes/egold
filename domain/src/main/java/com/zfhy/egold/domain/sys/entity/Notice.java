package com.zfhy.egold.domain.sys.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Table(name = "sys_notice")
public class Notice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "notice_title")
    private String noticeTitle;

    
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    @Column(name = "notice_time")
    private Date noticeTime;

    
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    @Column(name = "invalid_time")
    private Date invalidTime;

    
    @Column(name = "notice_status")
    private Integer noticeStatus;

    
    @Column(name = "admin_id")
    private Integer adminId;

    
    @Column(name = "article_id")
    private Integer articleId;

    @Transient
    private String articleContent;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    @Column(name = "notice_content")
    private String noticeContent;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getNoticeTitle() {
        return noticeTitle;
    }

    
    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    
    public Date getNoticeTime() {
        return noticeTime;
    }

    
    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    
    public Date getInvalidTime() {
        return invalidTime;
    }

    
    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    
    public Integer getNoticeStatus() {
        return noticeStatus;
    }

    
    public void setNoticeStatus(Integer noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    
    public Integer getAdminId() {
        return adminId;
    }

    
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
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

    
    public String getNoticeContent() {
        return noticeContent;
    }

    
    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
}
