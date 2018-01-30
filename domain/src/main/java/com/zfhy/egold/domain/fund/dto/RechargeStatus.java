package com.zfhy.egold.domain.fund.dto;


public enum RechargeStatus {
    CONFIRMED_PAY("1", "已发起支付，待短信验证"),
    PAY_PROCESSING("2", "支付处理中"),
    PAY_SUCCESS("3", "支付成功"),
    PAY_FAIL("4", "支付失败"),
    EXPIRED_PAY("5", "支付超时");

    private String status;
    private String label;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    RechargeStatus(String status, String label) {
        this.status = status;
        this.label = label;
    }
}
