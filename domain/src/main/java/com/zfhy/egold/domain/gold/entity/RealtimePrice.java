package com.zfhy.egold.domain.gold.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "gold_realtime_price")
public class RealtimePrice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "update_time")
    private Date updateTime;

    
    @Column(name = "latest_price")
    private Double latestPrice;

    
    @Column(name = "yes_price")
    private Double yesPrice;

    
    @Column(name = "open_price")
    private Double openPrice;

    
    @Column(name = "max_price")
    private Double maxPrice;

    
    @Column(name = "min_price")
    private Double minPrice;

    
    @Column(name = "change_percent")
    private Double changePercent;

    
    @Column(name = "create_date")
    private Date createDate;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    
    public Double getLatestPrice() {
        return latestPrice;
    }

    
    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    
    public Double getYesPrice() {
        return yesPrice;
    }

    
    public void setYesPrice(Double yesPrice) {
        this.yesPrice = yesPrice;
    }

    
    public Double getOpenPrice() {
        return openPrice;
    }

    
    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    
    public Double getMaxPrice() {
        return maxPrice;
    }

    
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    
    public Double getMinPrice() {
        return minPrice;
    }

    
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    
    public Double getChangePercent() {
        return changePercent;
    }

    
    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }

    
    public Date getCreateDate() {
        return createDate;
    }

    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
