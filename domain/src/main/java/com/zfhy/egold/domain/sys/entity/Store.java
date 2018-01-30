package com.zfhy.egold.domain.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_store")
public class Store {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "store_name")
    private String storeName;

    
    @Column(name = "store_addr")
    private String storeAddr;

    
    private String tel;

    
    private String img;

    
    @Column(name = "can_store")
    private Integer canStore;

    
    @Column(name = "can_withdraw")
    private Integer canWithdraw;

    
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

    
    public String getStoreName() {
        return storeName;
    }

    
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    
    public String getStoreAddr() {
        return storeAddr;
    }

    
    public void setStoreAddr(String storeAddr) {
        this.storeAddr = storeAddr;
    }

    
    public String getTel() {
        return tel;
    }

    
    public void setTel(String tel) {
        this.tel = tel;
    }

    
    public String getImg() {
        return img;
    }

    
    public void setImg(String img) {
        this.img = img;
    }

    
    public Integer getCanStore() {
        return canStore;
    }

    
    public void setCanStore(Integer canStore) {
        this.canStore = canStore;
    }

    
    public Integer getCanWithdraw() {
        return canWithdraw;
    }

    
    public void setCanWithdraw(Integer canWithdraw) {
        this.canWithdraw = canWithdraw;
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
