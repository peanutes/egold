package com.zfhy.egold.domain.invest.dto;


public enum CouponStatus {
    NON_USE(1, "未使用"),
    EXPIRED(2, "已失效"),
    HAD_USED(3, "已使用");

    private int code;
    private String label;

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

    CouponStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
