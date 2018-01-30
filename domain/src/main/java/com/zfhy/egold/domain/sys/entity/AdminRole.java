package com.zfhy.egold.domain.sys.entity;

import javax.persistence.*;

@Table(name = "sys_admin_role")
public class AdminRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "admin_id")
    private Integer adminId;

    
    @Column(name = "role_id")
    private Integer roleId;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getAdminId() {
        return adminId;
    }

    
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    
    public Integer getRoleId() {
        return roleId;
    }

    
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
