package com.zfhy.egold.domain.fund.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.fund.dto.RechargeStatus;
import com.zfhy.egold.domain.fund.entity.Recharge;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface RechargeMapper extends Mapper<Recharge> {

    @Update("update fund_recharge set status=#{param2} where pay_no=#{param1} and status=#{param3}")
    void updateStatusByPayNo(String paySn, RechargeStatus newStatus, RechargeStatus oldStatus);

    @Update("update fund_recharge set status=#{param2} where id=#{param1} and status in(1, 2)")
    int rechargeSuccess(Integer id, String status);

    List<Recharge> list(Map<String, String> params);
}
