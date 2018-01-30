package com.zfhy.egold.domain.goods.service;

import com.zfhy.egold.domain.goods.dto.GoodsPropertyDto;
import com.zfhy.egold.domain.goods.entity.GoodsProperty;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface GoodsPropertyService  extends Service<GoodsProperty> {

    List<GoodsPropertyDto> findBySpuId(Integer spuId);

    void saveOrUpdate(List<GoodsProperty> exists, List<GoodsProperty> notExists, Integer spuId);
}
