package com.zfhy.egold.common.constant;


public enum IdTypeConstant {
    BIND_BANK_CARD("bind_bank_card", "绑定银行卡"),
    PAY_REQUEST("pay_request", "支付请求"),
    RECHARGE_PAY_NO("recharge_pay", "充值"),
    ;


    private String prefix;
    private String label;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    IdTypeConstant(String prefix, String label) {
        this.prefix = prefix;
        this.label = label;
    }
    }
