package com.zfhy.egold.domain.sys.entity;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "sys_admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "login_account")
    private String loginAccount;

    
    private String name;

    
    private String password;

    
    private String status;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;


    @Transient
    private List<Integer> roleIds = Lists.newArrayList();

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getLoginAccount() {
        return loginAccount;
    }

    
    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public String getPassword() {
        return password;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
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
