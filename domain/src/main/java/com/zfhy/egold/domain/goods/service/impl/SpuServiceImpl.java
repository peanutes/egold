package com.zfhy.egold.domain.goods.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.goods.dao.SpuMapper;
import com.zfhy.egold.domain.goods.dto.SpuStatus;
import com.zfhy.egold.domain.goods.dto.SpuType;
import com.zfhy.egold.domain.goods.entity.Spu;
import com.zfhy.egold.domain.goods.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;



@Service
@Transactional
@Slf4j
public class SpuServiceImpl extends AbstractService<Spu> implements SpuService{
    @Autowired
    private SpuMapper spuMapper;

    @Override
    public List<Spu> findByType(SpuType spuType) {

        Condition condition = new Condition(Spu.class);
        condition.createCriteria()
                .andEqualTo("goodsTypeId", spuType.getCode())
                .andEqualTo("status", SpuStatus.ON.getCode());
        condition.orderBy("sort");

        return this.findByCondition(condition);
    }
}
