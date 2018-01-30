package com.zfhy.egold.domain.member.dto;


public enum ChangeMobileStatus {
    WAIT_AUDIT(0, "等待审核"),
    AUDIT_PASS(0, "审核通过"),
    AUDIT_DENY(0, "审核不通过"),;



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

    ChangeMobileStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
