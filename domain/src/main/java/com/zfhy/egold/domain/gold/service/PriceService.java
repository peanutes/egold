package com.zfhy.egold.domain.gold.service;

import com.zfhy.egold.domain.gold.dto.PriceSerialDto;


public interface PriceService {
    PriceSerialDto hourGoldPrices();

    PriceSerialDto weekGoldPrices();

    PriceSerialDto monthPriceService();

    PriceSerialDto dailyPrices();
}
