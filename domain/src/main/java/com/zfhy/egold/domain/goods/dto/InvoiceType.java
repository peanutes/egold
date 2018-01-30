package com.zfhy.egold.domain.goods.dto;


public enum InvoiceType {
    personal(1, "个人"),
    company(2, "公司"),;


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

    InvoiceType(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
