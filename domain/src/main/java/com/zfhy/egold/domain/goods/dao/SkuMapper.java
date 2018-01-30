package com.zfhy.egold.domain.goods.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.goods.dto.PcHomeSkuDto;
import com.zfhy.egold.domain.goods.dto.SkuDto;
import com.zfhy.egold.domain.goods.entity.Sku;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SkuMapper extends Mapper<Sku> {

    @Select("select k.*, p.goods_name goodName, p.img_url listImgUrl from goods_sku k left join goods_spu p on p.id=k.spu_id where p.goods_type_id=2 and p.del_flag='0' and p.status = 1")

    List<SkuDto> skuList(String orderBy);

    @Select("select k.price, p.goods_name productName, p.img_url listImgUrl from goods_sku k, goods_spu p where k.spu_id=p.id and p.status=1 and p.del_flag='0' order by k.sort limit 0,3")
    List<PcHomeSkuDto> pcHomeSkuDto();


    void batchDeleteNotExists(Integer spuId, List<Integer> idList);

    @Delete("delete from goods_sku where spu_id=#{param1}")
    void deleteBySpuId(Integer spuId);
}
