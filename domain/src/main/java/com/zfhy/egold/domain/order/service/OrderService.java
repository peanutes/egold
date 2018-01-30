package com.zfhy.egold.domain.order.service;

import com.zfhy.egold.domain.order.dto.OrderDto;
import com.zfhy.egold.domain.order.dto.OrderStatus;
import com.zfhy.egold.domain.order.dto.OrderSubmitDto;
import com.zfhy.egold.domain.order.dto.OrderSubmitResultDto;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.report.dto.ProductStatisticsDto;

import java.util.List;
import java.util.Map;



public interface OrderService  extends Service<Order> {

    OrderDto submitOrder(OrderSubmitDto orderSubmitDto, String terminalType);

    OrderDto switchInto(Integer memberId, Double switchGoldWeight, Integer discountCouponId, Integer productId, String terminalType);

    OrderSubmitResultDto confirmPay(Integer memberId, String orderSn, String payPwd, String terminalId);

    void updateStatus(Integer id, Integer version, int orderStatus);

    List<Order> findExpired(Integer expiredTime);

    void expiredOrder(Order order);

    void updateStatusByOrderSn(String orderSn, OrderStatus newStatus, OrderStatus oldStatus);

    Order findByOrderSn(String orderSn);

    List<Order> list(Map<String, String> params);

    List<ProductStatisticsDto> productBuyerStatistics();

}
