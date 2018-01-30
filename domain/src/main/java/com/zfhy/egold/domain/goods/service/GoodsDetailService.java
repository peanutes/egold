package com.zfhy.egold.domain.goods.service;

import com.zfhy.egold.domain.goods.entity.GoodsDetail;
import com.zfhy.egold.common.core.Service;



public interface GoodsDetailService  extends Service<GoodsDetail> {

    GoodsDetail findBySpuId(Integer spuId);
}
