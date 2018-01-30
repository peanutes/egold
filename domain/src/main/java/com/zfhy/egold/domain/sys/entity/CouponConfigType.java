package com.zfhy.egold.domain.sys.entity;


public enum CouponConfigType {
    CASH_COUPON(1, "现金红包"),
    CASH_AWARD(3, "现金奖励"),
    DISCOUNT_COUPON(2, "加息卷");


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

    CouponConfigType(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
