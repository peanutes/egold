package com.zfhy.egold.domain.sys.entity;

import javax.persistence.*;

@Table(name = "sys_city")
public class City {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    private Integer pid;

    
    private String name;

    
    private String pingyin;

    
    private String level;

    
    private Integer sort;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getPid() {
        return pid;
    }

    
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public String getPingyin() {
        return pingyin;
    }

    
    public void setPingyin(String pingyin) {
        this.pingyin = pingyin;
    }

    
    public String getLevel() {
        return level;
    }

    
    public void setLevel(String level) {
        this.level = level;
    }

    
    public Integer getSort() {
        return sort;
    }

    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
