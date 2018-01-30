package com.zfhy.egold.domain.order.dto;


public enum PayType {
    YIBAO_PAY(1, "易宝支付"),
    BALANCE_PAY(2, "余额支付"),;

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

    PayType(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
