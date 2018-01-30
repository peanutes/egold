package com.zfhy.egold.domain.invest.dto;


public enum InvestRecordStatus {
    WAIT_REPAY("0", "待返还"),
    HAD_REPAY("1", "已返还"),;
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

    InvestRecordStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
