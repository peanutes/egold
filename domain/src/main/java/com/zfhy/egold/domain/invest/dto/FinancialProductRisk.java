package com.zfhy.egold.domain.invest.dto;


public enum FinancialProductRisk {
    HIGH("高"),
    MIDDLE("中"),
    STEADY("稳健"),
    LOW("低");

    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    FinancialProductRisk(String label) {
        this.label = label;
    }
}
