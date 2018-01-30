package com.zfhy.egold.domain.gold.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.gold.dao.WeekPriceMapper;
import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.entity.WeekPrice;
import com.zfhy.egold.domain.gold.service.WeekPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class WeekPriceServiceImpl extends AbstractService<WeekPrice> implements WeekPriceService{
    @Autowired
    private WeekPriceMapper weekPriceMapper;

    @Override

    public List<PriceDto> weekPrices() {

        List<WeekPrice> weekPrices = this.weekPriceMapper.weekPrices();

        return weekPrices.stream().map(weekPrice -> {
            PriceDto priceDto = new PriceDto();
            priceDto.setPrice(weekPrice.getPrice());
            priceDto.setCreateDate(DateUtil.toString(weekPrice.getCreateDate(), DateUtil.YYYY_MM_DD_HH_MM_SS));
            return priceDto;
        }).collect(Collectors.toList());
    }

}
