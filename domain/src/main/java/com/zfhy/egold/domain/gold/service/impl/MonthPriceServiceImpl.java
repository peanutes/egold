package com.zfhy.egold.domain.gold.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.gold.dao.MonthPriceMapper;
import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.entity.MonthPrice;
import com.zfhy.egold.domain.gold.service.MonthPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class MonthPriceServiceImpl extends AbstractService<MonthPrice> implements MonthPriceService{
    @Autowired
    private MonthPriceMapper monthPriceMapper;


    @Override
    public List<PriceDto> monthPrices() {

        List<MonthPrice> monthPrices = this.monthPriceMapper.monthPrices();


        return monthPrices.stream().map(monthPrice -> {
            PriceDto priceDto = new PriceDto();
            priceDto.setCreateDate(DateUtil.toString(monthPrice.getCreateDate(), DateUtil.YYYY_MM_DD_HH_MM_SS));
            priceDto.setPrice(monthPrice.getPrice());
            return priceDto;
        }).collect(Collectors.toList());
    }

}
