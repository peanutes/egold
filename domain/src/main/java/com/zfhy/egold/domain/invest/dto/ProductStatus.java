package com.zfhy.egold.domain.invest.dto;


public enum ProductStatus {

    DRAF("2", "草稿"),
    OFF_SELL("0", "停售"),
    ON_SELL("1", "在售"),;



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

    ProductStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }
    }
