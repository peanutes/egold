package com.zfhy.egold.domain.fund.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import org.apache.ibatis.annotations.Select;

public interface WithdrawMapper extends Mapper<Withdraw> {
    @Select("select sum(withdraw_amount) from fund_withdraw f where f.member_id = #{param1} and f.status = 1")
    Double findOnWithdraw(Integer memberId);
}
