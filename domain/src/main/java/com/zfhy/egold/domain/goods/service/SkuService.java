package com.zfhy.egold.domain.goods.service;

import com.zfhy.egold.domain.goods.dto.PcHomeSkuDto;
import com.zfhy.egold.domain.goods.dto.SkuDetailDto;
import com.zfhy.egold.domain.goods.dto.SkuDto;
import com.zfhy.egold.domain.goods.dto.SkuQueryOrderBy;
import com.zfhy.egold.domain.goods.entity.Sku;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface SkuService  extends Service<Sku> {

    List<Sku> findBySpuId(Integer spuId);

    List<SkuDto> skuList(SkuQueryOrderBy skuQueryOrderBy);

    SkuDetailDto getSkuDetail(Integer skuId);

    List<PcHomeSkuDto> pcHomeSkuDto();

    void saveOrUpdate(List<Sku> exists, List<Sku> notExists, Integer spuId);
}
