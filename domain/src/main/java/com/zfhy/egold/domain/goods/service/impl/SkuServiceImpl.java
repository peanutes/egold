package com.zfhy.egold.domain.goods.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.goods.dao.SkuMapper;
import com.zfhy.egold.domain.goods.dto.*;
import com.zfhy.egold.domain.goods.entity.GoodsDetail;
import com.zfhy.egold.domain.goods.entity.Sku;
import com.zfhy.egold.domain.goods.entity.Spu;
import com.zfhy.egold.domain.goods.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class SkuServiceImpl extends AbstractService<Sku> implements SkuService{
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuService spuService;

    @Autowired
    private GoodsImgService goodsImgService;
    @Autowired
    private GoodsPropertyService goodsPropertyService;
    @Autowired
    private GoodsDetailService goodsDetailService;

    @Override
    public List<Sku> findBySpuId(Integer spuId) {
        Condition condition = new Condition(Sku.class);
        condition.createCriteria()
                .andEqualTo("spuId", spuId);

        condition.setOrderByClause(" spec asc");

        return this.findByCondition(condition);
    }

    @Override
    public List<SkuDto> skuList(SkuQueryOrderBy skuQueryOrderBy) {
        return this.skuMapper.skuList(skuQueryOrderBy.getOrderBy());
    }

    @Override
    public SkuDetailDto getSkuDetail(Integer skuId) {
        Sku sku = this.findById(skuId);
        if (Objects.isNull(sku)) {
            throw new BusException("您好，获取不到商品信息");
        }

        Integer spuId = sku.getSpuId();
        Spu spu = this.spuService.findById(spuId);

        if (Objects.isNull(spu)) {
            throw new BusException("您好，找不到商品信息");
        }

        GoodsDetail goodsDetail = this.goodsDetailService.findBy("spuId", spuId);

        List<GoodsPropertyDto> goodsPropertyDtoList = this.goodsPropertyService.findBySpuId(spuId);

        List<GoodsImgDto> goodsImgDtoList = this.goodsImgService.findBySpuId(spuId);

        return SkuDetailDto.builder()
                .goodsDetailDto(new GoodsDetailDto().convertFrom(goodsDetail))
                .goodsImgDtoList(goodsImgDtoList)
                .goodsName(spu.getGoodsName())
                .goodsPropertyDtoList(goodsPropertyDtoList)
                .price(sku.getPrice())
                .stock(sku.getStock())
                .spec(sku.getSpec())
                .id(sku.getId())
                .build();


    }

    @Override
    public List<PcHomeSkuDto> pcHomeSkuDto() {
        return this.skuMapper.pcHomeSkuDto();
    }

    @Override
    public void saveOrUpdate(List<Sku> exists, List<Sku> notExists, Integer spuId) {
        if (CollectionUtils.isEmpty(exists) && CollectionUtils.isEmpty(notExists)) {
            this.skuMapper.deleteBySpuId(spuId);
        }


        if (CollectionUtils.isNotEmpty(exists)) {
            List<Integer> idList = exists.stream().map(Sku::getId).collect(Collectors.toList());

            this.skuMapper.batchDeleteNotExists(spuId, idList);

            for (Sku sku : exists) {
                this.update(sku);
            }

        }


        if (CollectionUtils.isNotEmpty(notExists)) {
            this.save(notExists);
        }
    }
}
