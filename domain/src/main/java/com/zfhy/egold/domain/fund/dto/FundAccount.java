package com.zfhy.egold.domain.fund.dto;


public enum FundAccount {
    CASH("现金"),
    CURRENT("活期金"),
    TERM("定期金"),
    INVEST("投资账户");


    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    FundAccount(String label) {
        this.label = label;
    }
}
