package com.zfhy.egold.domain.gold.service.impl;

import com.google.common.collect.Lists;
import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.dto.PriceSerialDto;
import com.zfhy.egold.domain.gold.service.DailyPriceService;
import com.zfhy.egold.domain.gold.service.MonthPriceService;
import com.zfhy.egold.domain.gold.service.PriceService;
import com.zfhy.egold.domain.gold.service.WeekPriceService;
import com.zfhy.egold.domain.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@Transactional
@Slf4j
public class PriceServiceImpl implements PriceService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private WeekPriceService weekPriceService;

    @Autowired
    private MonthPriceService monthPriceService;

    @Autowired
    private DailyPriceService dailyPriceService;





    private PriceSerialDto getPriceSerialDto(List<PriceDto> priceDtoList) {

        priceDtoList.sort((p1, p2) -> p1.getCreateDate().compareTo(p2.getCreateDate()));

        List<String> times = Lists.newArrayListWithCapacity(priceDtoList.size());
        List<Double> prices = Lists.newArrayListWithCapacity(priceDtoList.size());
        Double max = Double.MIN_VALUE;
        Double min = Double.MAX_VALUE;



        for (PriceDto priceDto : priceDtoList) {

            if (Objects.isNull(priceDto)) {
                continue;
            }

            Double price = priceDto.getPrice();
            if (Objects.isNull(price) || DoubleUtil.equal(price, 0D)) {
                continue;
            }

            times.add(priceDto.getCreateDate());
            prices.add(price);


            if (price > max) {
                max = price;
            }
            if (price < min) {
                min = price;
            }

        }


        PriceSerialDto priceSerialDto = new PriceSerialDto();
        priceSerialDto.setTimes(times);
        priceSerialDto.setPrices(prices);
        priceSerialDto.setMaxPrice(max);
        priceSerialDto.setMinPrice(min);
        return priceSerialDto;
    }



    @Cacheable("RedisServiceImpl_hourGoldPrices")
    @CacheDuration(duration = 60)
    @Override
    public PriceSerialDto hourGoldPrices() {
        List<PriceDto> priceDtoList = this.redisService.hourGoldPrices();
        return getPriceSerialDto(priceDtoList);
    }

    @Cacheable("WeekPriceServiceImpl_weekPrices")
    @CacheDuration(duration = 1800L)
    @Override
    public PriceSerialDto weekGoldPrices() {
        List<PriceDto> priceDtoList = this.weekPriceService.weekPrices();

        return getPriceSerialDto(priceDtoList);

    }

    @Cacheable("MonthPriceServiceImpl_monthPrices")
    @CacheDuration(duration = 7200L)
    @Override
    public PriceSerialDto monthPriceService() {
        List<PriceDto> priceDtoList = this.monthPriceService.monthPrices();

        return getPriceSerialDto(priceDtoList);
    }

    @CacheDuration(duration = 43200)
    @Cacheable("DailyPriceServiceImpl_dailyPrices")
    @Override
    public PriceSerialDto dailyPrices() {
        List<PriceDto> priceDtoList = this.dailyPriceService.dailyPrices();

        return getPriceSerialDto(priceDtoList);
    }

}
