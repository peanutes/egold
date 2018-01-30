package com.zfhy.egold.domain.payment.service;

import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.order.dto.PaymentDto;
import com.zfhy.egold.domain.order.entity.Order;


public interface PaymentService {
    void paySuccess(Order order);

    void payFail(Order order);


    String payRequest(PaymentDto paymentDto, String terminalId, String callBackUrl);

    void yibaoPayCallback(String data, String encryptkey);

    void validatePaySmsCode(Integer memberId, String payRequestNo, String smsCode);

    String pcRecharge(PaymentDto paymentDto, String bankChannelId);

    void paySuccess(GoodsOrder goodsOrder);

    void yibaoWithdrawGoldPayCallback(String data, String encryptkey);

    void yibaoMallPayCallback(String data, String encryptkey);

    boolean verifyCallbackHmac(String[] strArr, String hmac);

    boolean verifyCallbackHmacSafe(String[] strArr, String hmac_safe);

    void pcRechargeCallback(String resultStatus, String rechargeAmount, String payRequestNo);
}
