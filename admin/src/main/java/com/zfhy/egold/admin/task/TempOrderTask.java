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
@ScheduledJob(orderNum = 1,name = "临时订单超时", cron = "0/30 * * * * ?", method = "tempOrderTimeOut",autoStartup = true)
public class TempOrderTask {

    @Autowired
    private TempOrderService tempOrderService;
    @Autowired
    private DictService dictService;


    public void tempOrderTimeOut() {
        Integer tempOrderExpiredSec = dictService.findIntByType(DictType.ORDER_EXPIRED_TIME);

        List<TempOrder> orderList = this.tempOrderService.findExpired(tempOrderExpiredSec);

        orderList.stream().forEach(e -> {
            try {
                tempOrderService.expiredOrder(e);
            } catch (Throwable throwable) {
                log.error("订单过期定期任务失败", throwable);
            }
        });

    }
}
