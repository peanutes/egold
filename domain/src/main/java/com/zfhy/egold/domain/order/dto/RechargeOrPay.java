package com.zfhy.egold.domain.order.dto;


public enum RechargeOrPay {
    RECHARGE(2, "充值"),
    PAY(1, "支付"),
    WITHDRAW_GOLD_PAY(3, "提金支付"),
    MALL_PAY(4, "商城支付"),;


    private int code;
    private String Label;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    RechargeOrPay(int code, String label) {
        this.code = code;
        Label = label;
    }
}
