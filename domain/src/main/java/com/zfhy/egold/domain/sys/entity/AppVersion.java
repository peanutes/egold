package com.zfhy.egold.domain.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_app_version")
public class AppVersion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "app_name")
    private String appName;

    
    @Column(name = "version_code")
    private String versionCode;

    
    @Column(name = "version_name")
    private String versionName;

    
    @Column(name = "apk_url")
    private String apkUrl;

    
    @Column(name = "change_log")
    private String changeLog;

    
    @Column(name = "app_type")
    private String appType;

    
    @Column(name = "update_tips")
    private String updateTips;

    
    @Column(name = "upgrade_time")
    private Date upgradeTime;

    
    @Column(name = "create_time")
    private Date createTime;

    
    @Column(name = "force_update")
    private String forceUpdate;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getAppName() {
        return appName;
    }

    
    public void setAppName(String appName) {
        this.appName = appName;
    }

    
    public String getVersionCode() {
        return versionCode;
    }

    
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    
    public String getVersionName() {
        return versionName;
    }

    
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    
    public String getApkUrl() {
        return apkUrl;
    }

    
    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    
    public String getChangeLog() {
        return changeLog;
    }

    
    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    
    public String getAppType() {
        return appType;
    }

    
    public void setAppType(String appType) {
        this.appType = appType;
    }

    
    public String getUpdateTips() {
        return updateTips;
    }

    
    public void setUpdateTips(String updateTips) {
        this.updateTips = updateTips;
    }

    
    public Date getUpgradeTime() {
        return upgradeTime;
    }

    
    public void setUpgradeTime(Date upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    
    public Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    
    public String getForceUpdate() {
        return forceUpdate;
    }

    
    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}
