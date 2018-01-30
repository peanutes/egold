package com.zfhy.egold.domain.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_dict")
public class Dict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    private String type;

    
    @Column(name = "label_name")
    private String labelName;

    
    private String value;

    
    private String des;

    
    private Integer sort;

    
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

    
    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }

    
    public String getLabelName() {
        return labelName;
    }

    
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    
    public String getValue() {
        return value;
    }

    
    public void setValue(String value) {
        this.value = value;
    }

    
    public String getDes() {
        return des;
    }

    
    public void setDes(String des) {
        this.des = des;
    }

    
    public Integer getSort() {
        return sort;
    }

    
    public void setSort(Integer sort) {
        this.sort = sort;
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
