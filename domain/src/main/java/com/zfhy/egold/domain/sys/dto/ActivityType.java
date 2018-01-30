package com.zfhy.egold.domain.sys.dto;



public enum ActivityType {
    APP_HOME_POPUP(1, "首页弹窗"),;

    private int code;
    private String label;

    ActivityType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
