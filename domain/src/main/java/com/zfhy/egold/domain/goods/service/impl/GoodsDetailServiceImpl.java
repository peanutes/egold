package com.zfhy.egold.domain.goods.service.impl;

import com.zfhy.egold.domain.goods.dao.GoodsDetailMapper;
import com.zfhy.egold.domain.goods.entity.GoodsDetail;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.goods.service.GoodsDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class GoodsDetailServiceImpl extends AbstractService<GoodsDetail> implements GoodsDetailService{
    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    @Override
    public GoodsDetail findBySpuId(Integer spuId) {

        return this.findBy("spuId", spuId);
    }
}
