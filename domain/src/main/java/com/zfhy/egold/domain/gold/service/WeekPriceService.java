package com.zfhy.egold.domain.gold.service;

import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.entity.WeekPrice;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface WeekPriceService  extends Service<WeekPrice> {

    List<PriceDto> weekPrices();


}
