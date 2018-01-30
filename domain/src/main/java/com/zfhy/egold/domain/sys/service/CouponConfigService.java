package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.fund.dto.FundOperateType;
import com.zfhy.egold.domain.sys.dto.CouponUseScene;
import com.zfhy.egold.domain.sys.entity.CouponConfig;
import com.zfhy.egold.common.core.Service;



public interface CouponConfigService  extends Service<CouponConfig> {

    void grant(Integer memberId, CouponUseScene useScene);


    void grantByOrderAmount(Integer memberId, CouponUseScene useScene, Double orderTotalAmount, FundOperateType fundOperateType, String awardRemark);


    void investAward(Integer referee, CouponUseScene useScene, Double totalAmount, Integer term);

}
