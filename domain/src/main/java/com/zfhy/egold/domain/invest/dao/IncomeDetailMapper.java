package com.zfhy.egold.domain.invest.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.invest.dto.IncomeDailyDto;
import com.zfhy.egold.domain.invest.entity.IncomeDetail;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IncomeDetailMapper extends Mapper<IncomeDetail> {
    @Select("select sum(i.income) from invest_income_detail i where i.member_id = #{memberId} and (to_days(now()) - to_days(i.income_time) < 1)")
    Double findYesterdayIncome(Integer memberId);

    @Select("select sum(ifnull(i.income, 0)) from invest_income_detail i where i.member_id = #{memberId}")
    Double findAllIncome(Integer memberId);

    @Select("select income_date incomeDate,   sum(income) incomeSum from invest_income_detail where member_id = #{memberId} group by income_date ")
    List<IncomeDailyDto> dailyIncom(Integer memberId);

    @Select("select sum(income) from invest_income_detail where member_id=#{param1}")
    Double sumByMemberId(Integer memberId);

    @Select("select  sum(i.income) from invest_income_detail i, invest_invest_record ir where ir.id=i.invest_id and i.member_id=#{param1} and ir.product_type='RISE_FALL'")
    Double sumInvestIncome(Integer memberId);


}
