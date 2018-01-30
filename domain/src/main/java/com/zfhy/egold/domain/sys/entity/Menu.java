package com.zfhy.egold.domain.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "parent_id")
    private Integer parentId;

    
    @Column(name = "menu_name")
    private String menuName;

    
    private String type;

    
    private Integer sort;

    
    private String href;

    
    private String icon;

    
    @Column(name = "is_show")
    private String isShow;

    
    private String permission;

    
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

    
    public Integer getParentId() {
        return parentId;
    }

    
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    
    public String getMenuName() {
        return menuName;
    }

    
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    
    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }

    
    public Integer getSort() {
        return sort;
    }

    
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    
    public String getHref() {
        return href;
    }

    
    public void setHref(String href) {
        this.href = href;
    }

    
    public String getIcon() {
        return icon;
    }

    
    public void setIcon(String icon) {
        this.icon = icon;
    }

    
    public String getIsShow() {
        return isShow;
    }

    
    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    
    public String getPermission() {
        return permission;
    }

    
    public void setPermission(String permission) {
        this.permission = permission;
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
