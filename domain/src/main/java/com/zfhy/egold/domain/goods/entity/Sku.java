package com.zfhy.egold.domain.goods.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "goods_sku")
public class Sku {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "spu_id")
    private Integer spuId;

    
    private Double spec;

    
    private Integer stock;

    
    private Double fee;

    
    private Integer version;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;


    
    private String barcode;

    
    private String remarks;


    private Double price;

    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    
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

    
    public Double getSpec() {
        return spec;
    }

    
    public void setSpec(Double spec) {
        this.spec = spec;
    }

    
    public Integer getStock() {
        return stock;
    }

    
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    
    public Double getFee() {
        return fee;
    }

    
    public void setFee(Double fee) {
        this.fee = fee;
    }

    
    public Integer getVersion() {
        return version;
    }

    
    public void setVersion(Integer version) {
        this.version = version;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
