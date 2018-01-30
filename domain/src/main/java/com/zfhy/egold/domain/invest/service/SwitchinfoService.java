package com.zfhy.egold.domain.invest.service;

import com.zfhy.egold.domain.invest.entity.Switchinfo;
import com.zfhy.egold.common.core.Service;



public interface SwitchinfoService extends Service<Switchinfo> {
    void saveSwitchinfo(Integer memberId, Integer orderId, Double weight);

}
