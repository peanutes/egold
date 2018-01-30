package com.zfhy.egold.domain.order.dto;


public enum PaymentLogStatus {
    WAIT_SMS_VALIDATE(0, "待短信验证"),
    WAIT_CALLBACK(1, "待支付回调"),
    PAY_SUCCESS(2, "支付成功"),
    PAY_FAIL(3, "支付失败"),
    VALIDATE_SMS_FAIL(4, "短信验证失败"),;

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

    PaymentLogStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
