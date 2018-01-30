package com.zfhy.egold.domain.goods.service;

import com.zfhy.egold.domain.goods.dto.SpuType;
import com.zfhy.egold.domain.goods.entity.Spu;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface SpuService  extends Service<Spu> {

    List<Spu> findByType(SpuType spuType);
}
