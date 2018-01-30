package com.zfhy.egold.domain.order.dto;


public enum MyorderType {
    BUY_GOLD(1, "买金"),
    SALE_GOLD(2, "卖金"),
    STORE_GOLD(3, "存金"),
    WITHDRAW_GOLD(4, "提金"),
    RECHARGE(5, "充值"),
    WITHDRAW(6, "提现"),
    GOLD_MALL(7, "黄金商城"),
    RISE_FALL(8, "看涨跌"),
    GOLD_SWITCH(9, "定期金转活期金");


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

    MyorderType(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
