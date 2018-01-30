package com.zfhy.egold.domain.fund.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import org.apache.ibatis.annotations.Select;

public interface FundRecordMapper extends Mapper<FundRecord> {

    @Select("select sum(operate_money) from fund_fund_record where member_id=#{param1} and operate_type=#{param2}")
    Double queryCommission(Integer inviterId, String fundOperateType);


}
