package com.zfhy.egold.domain.invest.dto;


public enum RiseFallInvestStatus {
    PROCESSING(0, "招标中"),
    FULL_SCALE(1, "满标"),
    FINISH(2, "已回款"),;

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

    RiseFallInvestStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
