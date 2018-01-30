package com.zfhy.egold.domain.goods.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.goods.dao.GoodsImgMapper;
import com.zfhy.egold.domain.goods.dto.GoodsImgDto;
import com.zfhy.egold.domain.goods.entity.GoodsImg;
import com.zfhy.egold.domain.goods.service.GoodsImgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class GoodsImgServiceImpl extends AbstractService<GoodsImg> implements GoodsImgService{
    @Autowired
    private GoodsImgMapper goodsImgMapper;

    @Override
    public List<GoodsImgDto> findBySpuId(Integer spuId) {
        Condition condition = new Condition(GoodsImg.class);
        condition.createCriteria()
                .andEqualTo("spuId", spuId);

        List<GoodsImg> goodsImgList = this.findByCondition(condition);
        return goodsImgList.stream().map(e->new GoodsImgDto().convertFrom(e)).collect(Collectors.toList());
    }
}
