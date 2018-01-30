package com.zfhy.egold.domain.gold.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.gold.entity.MonthPrice;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MonthPriceMapper extends Mapper<MonthPrice> {
    @Select("select * from gold_month_price mp where mp.create_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    List<MonthPrice> monthPrices();


}
