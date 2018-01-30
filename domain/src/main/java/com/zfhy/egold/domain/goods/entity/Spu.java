package com.zfhy.egold.domain.goods.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "goods_spu")
public class Spu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "goods_type_id")
    private Integer goodsTypeId;

    
    @Column(name = "goods_type_name")
    private String goodsTypeName;

    
    @Column(name = "goods_name")
    private String goodsName;

    
    @Column(name = "purity_des")
    private String purityDes;

    
    @Column(name = "weight_des")
    private String weightDes;

    
    @Column(name = "size_des")
    private String sizeDes;

    
    @Column(name = "fee_des")
    private String feeDes;

    
    @Column(name = "img_url")
    private String imgUrl;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;






    @Column(name = "price_range")
    private String priceRange;

    private Integer sort;


    private Integer status;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }


    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getGoodsTypeId() {
        return goodsTypeId;
    }

    
    public void setGoodsTypeId(Integer goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    
    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    
    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    
    public String getGoodsName() {
        return goodsName;
    }

    
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    
    public String getPurityDes() {
        return purityDes;
    }

    
    public void setPurityDes(String purityDes) {
        this.purityDes = purityDes;
    }

    
    public String getWeightDes() {
        return weightDes;
    }

    
    public void setWeightDes(String weightDes) {
        this.weightDes = weightDes;
    }

    
    public String getSizeDes() {
        return sizeDes;
    }

    
    public void setSizeDes(String sizeDes) {
        this.sizeDes = sizeDes;
    }

    
    public String getFeeDes() {
        return feeDes;
    }

    
    public void setFeeDes(String feeDes) {
        this.feeDes = feeDes;
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
