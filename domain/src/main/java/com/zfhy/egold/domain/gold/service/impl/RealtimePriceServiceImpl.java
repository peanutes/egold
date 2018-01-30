package com.zfhy.egold.domain.gold.service.impl;

import com.zfhy.egold.domain.gold.dao.RealtimePriceMapper;
import com.zfhy.egold.domain.gold.entity.RealtimePrice;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.gold.service.RealtimePriceService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class RealtimePriceServiceImpl extends AbstractService<RealtimePrice> implements RealtimePriceService{
    @Autowired
    private RealtimePriceMapper realtimePriceMapper;

}
