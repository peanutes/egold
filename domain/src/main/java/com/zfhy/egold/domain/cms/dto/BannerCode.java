package com.zfhy.egold.domain.cms.dto;

import com.google.common.collect.Maps;

import java.util.Map;


public enum BannerCode {
    APP_HOME_BANNER("app首页banner"),
    APP_ACTIVITY_BANNER("app活动banner"),

    PC_HOME_BANNER("PC首页banner"),
    PC_ACTIVITY_BANNER("PC活动banner"),

    WAP_HOME_BANNER("WAP首页banner"),
    WAP_ACTIVITY_BANNER("WAP活动banner");

    private String bannerTypeName;

    public static Map<String, String> getAllBanner() {
        Map<String, String> map = Maps.newHashMap();
        for (BannerCode bannerCode : BannerCode.values()) {
            map.put(bannerCode.name(), bannerCode.getBannerTypeName());
        }
        return map;
    }

    BannerCode(String bannerTypeName) {
        this.bannerTypeName = bannerTypeName;
    }

    public String getBannerTypeName() {
        return bannerTypeName;
    }

    public void setBannerTypeName(String bannerTypeName) {
        this.bannerTypeName = bannerTypeName;
    }
}
