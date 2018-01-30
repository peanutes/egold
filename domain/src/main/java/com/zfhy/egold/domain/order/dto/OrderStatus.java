package com.zfhy.egold.domain.order.dto;

import java.util.Arrays;
import java.util.List;


public enum OrderStatus {


    WAIT_PAY(0,"待支付"),
    CONFIRMED_PAY(1, "已发起支付，待短信验证"),
    PAY_PROCESSING(2, "支付处理中"),
    PAY_SUCCESS(3, "支付成功"),
    PAY_FAIL(4, "支付失败"),
    EXPIRED_PAY(5, "超时");



    public static List<Integer> finishStatus(){
        return Arrays.asList(PAY_SUCCESS.getStatus(), PAY_FAIL.getStatus(), EXPIRED_PAY.getStatus());
    }

    private int status;
    private String label;

    OrderStatus(int status, String label) {
        this.status = status;
        this.label = label;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
