package com.zfhy.egold.domain.fund.dto;


public enum InvestStatus {
    ON_INVEST("0", "在投"),
    FINISH("1", "已到期"),;

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

    InvestStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
