package com.zfhy.egold.domain.gold.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.gold.dao.DailyPriceMapper;
import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.entity.DailyPrice;
import com.zfhy.egold.domain.gold.service.DailyPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class DailyPriceServiceImpl extends AbstractService<DailyPrice> implements DailyPriceService{
    @Autowired
    private DailyPriceMapper dailyPriceMapper;


    @Override
    public List<PriceDto> dailyPrices() {
        List<DailyPrice> dailyPrices = this.dailyPriceMapper.dailyPrices();

        return dailyPrices.stream().map(dailyPrice -> {
            PriceDto priceDto = new PriceDto();
            priceDto.setCreateDate(DateUtil.toString(dailyPrice.getCreateDate(), DateUtil.YYYY_MM_DD_HH_MM_SS));
            priceDto.setPrice(dailyPrice.getPrice());
            return priceDto;
        }).collect(Collectors.toList());

    }
}
