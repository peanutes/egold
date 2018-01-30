package com.zfhy.egold.domain.fund.dto;


public enum WithdrawStatus {

    CONFIRMED("1", "待审核"),
    PAYED("2", "审核通过"),
    DENY("3", "审核不通过");

    private String code;
    private String label;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    WithdrawStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
