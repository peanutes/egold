package com.zfhy.egold.domain.cms.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "blog_content")
public class Content {
    @Id
    private Long cid;

    
    private String title;

    private String slug;

    
    private Long created;

    
    private Long modified;

    
    private String type;

    
    private String tags;

    
    private String categories;

    private Integer hits;

    
    @Column(name = "comments_num")
    private Integer commentsNum;

    
    @Column(name = "allow_comment")
    private Integer allowComment;

    
    @Column(name = "allow_ping")
    private Integer allowPing;

    
    @Column(name = "allow_feed")
    private Integer allowFeed;

    
    private Integer status;

    
    private String author;

    
    @Column(name = "gtm_create")
    private Date gtmCreate;

    
    @Column(name = "gtm_modified")
    private Date gtmModified;

    
    private String content;

    
    public Long getCid() {
        return cid;
    }

    
    public void setCid(Long cid) {
        this.cid = cid;
    }

    
    public String getTitle() {
        return title;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

    
    public String getSlug() {
        return slug;
    }

    
    public void setSlug(String slug) {
        this.slug = slug;
    }

    
    public Long getCreated() {
        return created;
    }

    
    public void setCreated(Long created) {
        this.created = created;
    }

    
    public Long getModified() {
        return modified;
    }

    
    public void setModified(Long modified) {
        this.modified = modified;
    }

    
    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }

    
    public String getTags() {
        return tags;
    }

    
    public void setTags(String tags) {
        this.tags = tags;
    }

    
    public String getCategories() {
        return categories;
    }

    
    public void setCategories(String categories) {
        this.categories = categories;
    }

    
    public Integer getHits() {
        return hits;
    }

    
    public void setHits(Integer hits) {
        this.hits = hits;
    }

    
    public Integer getCommentsNum() {
        return commentsNum;
    }

    
    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    
    public Integer getAllowComment() {
        return allowComment;
    }

    
    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

    
    public Integer getAllowPing() {
        return allowPing;
    }

    
    public void setAllowPing(Integer allowPing) {
        this.allowPing = allowPing;
    }

    
    public Integer getAllowFeed() {
        return allowFeed;
    }

    
    public void setAllowFeed(Integer allowFeed) {
        this.allowFeed = allowFeed;
    }

    
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
    }

    
    public String getAuthor() {
        return author;
    }

    
    public void setAuthor(String author) {
        this.author = author;
    }

    
    public Date getGtmCreate() {
        return gtmCreate;
    }

    
    public void setGtmCreate(Date gtmCreate) {
        this.gtmCreate = gtmCreate;
    }

    
    public Date getGtmModified() {
        return gtmModified;
    }

    
    public void setGtmModified(Date gtmModified) {
        this.gtmModified = gtmModified;
    }

    
    public String getContent() {
        return content;
    }

    
    public void setContent(String content) {
        this.content = content;
    }
}
