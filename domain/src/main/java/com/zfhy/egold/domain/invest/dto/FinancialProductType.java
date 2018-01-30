package com.zfhy.egold.domain.invest.dto;


public enum FinancialProductType {
    
    CURRENT_DEPOSIT("活期金"),
    
    TERM_DEPOSIT("定期金"),
    
    RISE_FALL("看涨跌");

    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    FinancialProductType(String label) {
        this.label = label;
    }
}
