package com.zfhy.egold.domain.gold.service;

import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.entity.DailyPrice;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface DailyPriceService  extends Service<DailyPrice> {

    List<PriceDto> dailyPrices();
}
