package com.zfhy.egold.domain.member.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "member_huanxin_user")
public class HuanxinUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    @Column(name = "huanxin_user_id")
    private String huanxinUserId;

    
    @Column(name = "huangxin_pass")
    private String huangxinPass;

    
    private String des;

    
    private Integer sort;

    
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

    
    public Integer getMemberId() {
        return memberId;
    }

    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    
    public String getHuanxinUserId() {
        return huanxinUserId;
    }

    
    public void setHuanxinUserId(String huanxinUserId) {
        this.huanxinUserId = huanxinUserId;
    }

    
    public String getHuangxinPass() {
        return huangxinPass;
    }

    
    public void setHuangxinPass(String huangxinPass) {
        this.huangxinPass = huangxinPass;
    }

    
    public String getDes() {
        return des;
    }

    
    public void setDes(String des) {
        this.des = des;
    }

    
    public Integer getSort() {
        return sort;
    }

    
    public void setSort(Integer sort) {
        this.sort = sort;
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
