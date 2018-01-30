package com.zfhy.egold.domain.goods.dto;


public enum SpuStatus {
    DRAF(0, "草稿"),
    ON(1, "上架"),
    OFF(2, "下架"),;



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

    SpuStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
    }
