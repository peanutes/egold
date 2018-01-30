package com.zfhy.egold.domain.goods.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "goods_goods_property")
public class GoodsProperty {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "spu_id")
    private Integer spuId;

    
    @Column(name = "property_name")
    private String propertyName;

    
    @Column(name = "property_value")
    private String propertyValue;

    
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

    
    public Integer getSpuId() {
        return spuId;
    }

    
    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
    }

    
    public String getPropertyName() {
        return propertyName;
    }

    
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    
    public String getPropertyValue() {
        return propertyValue;
    }

    
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
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
