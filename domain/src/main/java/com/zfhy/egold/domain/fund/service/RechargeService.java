package com.zfhy.egold.domain.fund.service;

import com.zfhy.egold.domain.fund.dto.RechargeStatus;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.common.core.Service;

import java.util.List;
import java.util.Map;



public interface RechargeService  extends Service<Recharge> {

    String recharge(Integer memberId, Double rechargeAmount, String pwd, String dealPwd);

    void updateStatusByPayNo(String paySn, RechargeStatus newStatus, RechargeStatus oldStatus);

    Recharge findByPayNo(String paySn);

    void rechargeSuccess(Recharge recharge);


    String pcRecharge(Integer memberId, Double rechargeAmount, String bankChannelId);

    List<Recharge> list(Map<String, String> params);
}
