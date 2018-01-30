package com.zfhy.egold.domain.order.service.impl;

import com.zfhy.egold.domain.order.dao.PaymentLogMapper;
import com.zfhy.egold.domain.order.dto.PaymentLogStatus;
import com.zfhy.egold.domain.order.entity.PaymentLog;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.order.service.PaymentLogService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class PaymentLogServiceImpl extends AbstractService<PaymentLog> implements PaymentLogService{
    @Autowired
    private PaymentLogMapper paymentLogMapper;

    @Override
    public void updatePaymentStatus(String payRequestNo, PaymentLogStatus status, String errorMsg) {
        PaymentLog paymentLog = this.findBy("payRequestNo", payRequestNo);
        if (Objects.nonNull(paymentLog) && paymentLog.getStatus() != PaymentLogStatus.PAY_SUCCESS.getCode()) {
            paymentLog.setStatus(status.getCode());
            paymentLog.setErrroMsg(errorMsg);
            this.update(paymentLog);
        }
    }

    @Override
    public PaymentLog findByRequestNo(String requestNo) {
        return this.findBy("payRequestNo", requestNo);

    }

    @Override
    public List<PaymentLog> list(Map<String, String> params) {
        return this.paymentLogMapper.list(params);
    }

    
    @Override
    public void updateWaitCallback(String payRequestNo, PaymentLogStatus waitCallback, String errorMsg) {
        int count = this.paymentLogMapper.updateWaitCallBack(payRequestNo, waitCallback.getCode(), errorMsg);


    }
}
