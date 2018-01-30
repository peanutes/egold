package com.zfhy.egold.admin.task;

import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.goods.service.GoodsOrderService;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.order.entity.TempOrder;
import com.zfhy.egold.domain.order.service.OrderService;
import com.zfhy.egold.domain.order.service.TempOrderService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import com.zfhy.egold.schedule.config.ScheduledJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by LAI on 2017/10/19.
 */
//@Component("orderTask")
@Component
@Slf4j
@ScheduledJob(orderNum = 7,name = "支付订单超时", cron = "0 0/1 * * * ?", method = "orderTimeOut",autoStartup = true)
public class OrderTimeOutTask {

    @Autowired
    private OrderService orderService;
    @Autowired
    private DictService dictService;

    @Autowired
    private GoodsOrderService goodsOrderService;

    public void orderTimeOut() {

        Integer payExpiredSec = dictService.findIntByType(DictType.PAY_EXPIRED_TIME);

        List<Order> orderList = this.orderService.findExpired(payExpiredSec);

        orderList.stream().forEach(e -> {
            try {
                orderService.expiredOrder(e);
            } catch (Throwable throwable) {
                log.error("订单过期定期任务失败", throwable);
            }
        });

    }


   /* public void goodsOrderTimeOut() {

        Integer payExpiredSec = dictService.findIntByType(DictType.PAY_EXPIRED_TIME);

        List<GoodsOrder> orderList = this.goodsOrderService.findExpired(payExpiredSec);

        orderList.stream().forEach(e -> {
            try {
                goodsOrderService.expiredOrder(e);
            } catch (Throwable throwable) {
                log.error("商城订单过期定期任务失败", throwable);
            }
        });

    }*/
}
