package com.zfhy.egold.domain.order.service;

import com.zfhy.egold.domain.order.dto.PaymentLogStatus;
import com.zfhy.egold.domain.order.entity.PaymentLog;
import com.zfhy.egold.common.core.Service;

import java.util.List;
import java.util.Map;



public interface PaymentLogService  extends Service<PaymentLog> {

    void updatePaymentStatus(String payRequestNo, PaymentLogStatus status, String errorMsg);

    PaymentLog findByRequestNo(String requestNo);

    List<PaymentLog> list(Map<String, String> params);

    void updateWaitCallback(String payRequestNo, PaymentLogStatus waitCallback, String errorMsg);
}
