package com.zfhy.egold.domain.gold.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.gold.entity.WeekPrice;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WeekPriceMapper extends Mapper<WeekPrice> {
    @Select("select * from gold_week_price p where p.create_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    List<WeekPrice> weekPrices();
}
