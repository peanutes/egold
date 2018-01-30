package com.zfhy.egold.domain.invest.dto;


public enum RiseOrFall {
    RISE(1, "看涨"),
    FALL(0, "看跌"),;

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

    RiseOrFall(int code, String label) {

        this.code = code;
        this.label = label;
    }
}
