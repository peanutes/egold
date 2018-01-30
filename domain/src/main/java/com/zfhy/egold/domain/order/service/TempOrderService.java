package com.zfhy.egold.domain.order.service;

import com.zfhy.egold.domain.order.entity.TempOrder;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface TempOrderService  extends Service<TempOrder> {

    List<TempOrder> findExpired(Integer tempOrderExpiredSec);

    void expiredOrder(TempOrder tempOrder);

    void modifyOrderAmount(String orderSn, Double amount);
}
