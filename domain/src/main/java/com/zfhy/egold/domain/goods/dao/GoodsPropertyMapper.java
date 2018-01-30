package com.zfhy.egold.domain.goods.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.goods.entity.GoodsProperty;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface GoodsPropertyMapper extends Mapper<GoodsProperty> {

    void batchDeleteNotExists(Integer spuId, List<Integer> idList);

    @Delete("delete from goods_goods_property where spu_id=#{param1}")
    void deleteBySpuId(Integer spuId);
}
