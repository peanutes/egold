package com.zfhy.egold.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_myorder_attr")
public class MyorderAttr {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "myorder_id")
    private Integer myorderId;

    
    @Column(name = "attr_name")
    private String attrName;

    
    @Column(name = "attr_value")
    private String attrValue;

    
    private Integer sort;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getMyorderId() {
        return myorderId;
    }

    
    public void setMyorderId(Integer myorderId) {
        this.myorderId = myorderId;
    }

    
    public String getAttrName() {
        return attrName;
    }

    
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    
    public String getAttrValue() {
        return attrValue;
    }

    
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    
    public Integer getSort() {
        return sort;
    }

    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
