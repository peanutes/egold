package com.zfhy.egold.domain.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_activity")
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    private String name;

    
    private Integer type;

    
    @Column(name = "begin_date")
    private Date beginDate;

    
    @Column(name = "end_date")
    private Date endDate;

    
    private String href;

    
    @Column(name = "img_url")
    private String imgUrl;

    
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

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public Integer getType() {
        return type;
    }

    
    public void setType(Integer type) {
        this.type = type;
    }

    
    public Date getBeginDate() {
        return beginDate;
    }

    
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    
    public Date getEndDate() {
        return endDate;
    }

    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    
    public String getHref() {
        return href;
    }

    
    public void setHref(String href) {
        this.href = href;
    }

    
    public String getImgUrl() {
        return imgUrl;
    }

    
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
