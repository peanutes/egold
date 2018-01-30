package com.zfhy.egold.domain.sys.dto;


public enum MailStatus {
    UN_READ(1, "未读"),
    DELETED(2, "删除"),
    HAD_READ(3, "已读"),;



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

    MailStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
    }
