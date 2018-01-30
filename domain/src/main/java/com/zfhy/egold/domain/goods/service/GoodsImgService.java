package com.zfhy.egold.domain.goods.service;

import com.zfhy.egold.domain.goods.dto.GoodsImgDto;
import com.zfhy.egold.domain.goods.entity.GoodsImg;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface GoodsImgService  extends Service<GoodsImg> {

    List<GoodsImgDto> findBySpuId(Integer spuId);
}
