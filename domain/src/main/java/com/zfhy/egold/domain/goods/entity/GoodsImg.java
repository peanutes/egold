package com.zfhy.egold.domain.goods.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "goods_goods_img")
public class GoodsImg {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "spu_id")
    private Integer spuId;

    
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

    
    public Integer getSpuId() {
        return spuId;
    }

    
    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
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
