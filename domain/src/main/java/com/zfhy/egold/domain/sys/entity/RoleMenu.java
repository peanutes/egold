package com.zfhy.egold.domain.sys.entity;

import javax.persistence.*;

@Table(name = "sys_role_menu")
public class RoleMenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "role_id")
    private Integer roleId;

    
    @Column(name = "menu_id")
    private Integer menuId;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getRoleId() {
        return roleId;
    }

    
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    
    public Integer getMenuId() {
        return menuId;
    }

    
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
