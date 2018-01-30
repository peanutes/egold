package com.zfhy.egold.domain.goods.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.goods.dao.GoodsPropertyMapper;
import com.zfhy.egold.domain.goods.dto.GoodsPropertyDto;
import com.zfhy.egold.domain.goods.entity.GoodsProperty;
import com.zfhy.egold.domain.goods.service.GoodsPropertyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class GoodsPropertyServiceImpl extends AbstractService<GoodsProperty> implements GoodsPropertyService{
    @Autowired
    private GoodsPropertyMapper goodsPropertyMapper;

    @Override
    public List<GoodsPropertyDto> findBySpuId(Integer spuId) {

        Condition condition = new Condition(GoodsProperty.class);
        condition.createCriteria()
                .andEqualTo("spuId", spuId);

        List<GoodsProperty> goodsPropertyList = this.findByCondition(condition);
        return goodsPropertyList.stream().map(e -> new GoodsPropertyDto().convertFrom(e)).collect(Collectors.toList());

    }

    @Override
    public void saveOrUpdate(List<GoodsProperty> exists, List<GoodsProperty> notExists, Integer spuId) {
        if (CollectionUtils.isEmpty(exists) && CollectionUtils.isEmpty(notExists)) {
            this.goodsPropertyMapper.deleteBySpuId(spuId);
        }


        if (CollectionUtils.isNotEmpty(exists)) {
            List<Integer> idList = exists.stream().map(GoodsProperty::getId).collect(Collectors.toList());

            this.goodsPropertyMapper.batchDeleteNotExists(spuId, idList);

            for (GoodsProperty goodsProperty : exists) {
                this.update(goodsProperty);
            }

        }


        if (CollectionUtils.isNotEmpty(notExists)) {
            this.save(notExists);
        }
    }
}
