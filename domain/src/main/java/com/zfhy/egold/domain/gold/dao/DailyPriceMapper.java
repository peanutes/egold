package com.zfhy.egold.domain.gold.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.gold.entity.DailyPrice;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DailyPriceMapper extends Mapper<DailyPrice> {
    @Select("select * from gold_daily_price dp where dp.create_date >= DATE_SUB(CURDATE(), INTERVAL 365 DAY) ")
    List<DailyPrice> dailyPrices();
}
