package com.zfhy.egold.domain.invest.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import org.apache.ibatis.annotations.Select;

public interface SoldDetailMapper extends Mapper<SoldDetail> {
    @Select("select sum(ifnull(d.profit_or_loss, 0)) from invest_sold_detail d where d.member_id = #{param1}")
    Double historyBalance(Integer memberId);


}
