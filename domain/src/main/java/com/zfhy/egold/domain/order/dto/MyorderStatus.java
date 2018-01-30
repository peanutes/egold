package com.zfhy.egold.domain.order.dto;


public enum MyorderStatus {
    PROCESSING(1, "进行中"),
    FINISH(2, "已完成"),
    EXPIRED(3, "已过期"),;



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

    MyorderStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
