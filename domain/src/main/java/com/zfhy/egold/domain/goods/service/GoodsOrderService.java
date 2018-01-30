package com.zfhy.egold.domain.goods.service;

import com.zfhy.egold.domain.goods.dto.GoodsOrderDto;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.order.dto.OrderStatus;

import java.util.List;
import java.util.Map;



public interface GoodsOrderService  extends Service<GoodsOrder> {

    GoodsOrderDto submitOrder(Integer id, Integer skuId, Integer num, Double balanceAmount, Integer addressId, String payPwd, String terminalId, Integer invoiceId);

    void updatePaySuccessStatus(Integer id);

    GoodsOrder findByOrderSn(String orderSn);

    void payFail(GoodsOrder goodsOrder);

    void updateStatusByOrderSn(String paySn, OrderStatus newStatus, OrderStatus oldStatus);

    List<GoodsOrder> findExpired(Integer payExpiredSec);

    void expiredOrder(GoodsOrder goodsOrder);

    GoodsOrderDto submitMallOrder(Integer id, Integer skuId, Integer num, Double balanceAmount, Integer addressId, String dealPwd, String terminalId, Integer invoiceId);

    List<GoodsOrder> list(Map<String, String> params);
}
