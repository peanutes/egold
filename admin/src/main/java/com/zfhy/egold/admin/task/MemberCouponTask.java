package com.zfhy.egold.admin.task;

import com.zfhy.egold.domain.invest.service.MemberCouponService;
import com.zfhy.egold.schedule.config.ScheduledJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by LAI on 2017/12/1.
 */
@Component
@ScheduledJob(orderNum = 5,name = "红包过期定时任务（晚上执行）", cron = "0 40 1 * * ?", method = "expiredCoupon",autoStartup = true)
public class MemberCouponTask {

    @Autowired
    private MemberCouponService memberCouponService;


    public void expiredCoupon() {

        this.memberCouponService.expiredCoupon();
    }


}
