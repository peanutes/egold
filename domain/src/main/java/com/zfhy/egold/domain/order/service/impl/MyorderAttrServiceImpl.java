package com.zfhy.egold.domain.order.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.order.dao.MyorderAttrMapper;
import com.zfhy.egold.domain.order.entity.MyorderAttr;
import com.zfhy.egold.domain.order.service.MyorderAttrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;



@Service
@Transactional
@Slf4j
public class MyorderAttrServiceImpl extends AbstractService<MyorderAttr> implements MyorderAttrService{
    @Autowired
    private MyorderAttrMapper myorderAttrMapper;

    @Override
    public List<MyorderAttr> findByMyorderId(Integer myorderId) {

        Condition condition = new Condition(MyorderAttr.class);
        condition.createCriteria().andEqualTo("myorderId", myorderId);
        return this.findByCondition(condition);
    }

    @Override
    public void batchInsert(Integer myOrderId, List<MyorderAttr> myorderAttrs) {
        this.myorderAttrMapper.batchInsert(myOrderId, myorderAttrs);
    }

}
