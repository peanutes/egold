package com.zfhy.egold.domain.order.service.impl;

import com.zfhy.egold.domain.order.dao.TempOrderMapper;
import com.zfhy.egold.domain.order.entity.TempOrder;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.order.service.TempOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class TempOrderServiceImpl extends AbstractService<TempOrder> implements TempOrderService{
    @Autowired
    private TempOrderMapper tempOrderMapper;

    @Override
    public List<TempOrder> findExpired(Integer tempOrderExpiredSec) {
        return tempOrderMapper.findExpired(tempOrderExpiredSec);
    }

    @Override
    public void expiredOrder(TempOrder tempOrder) {
        this.tempOrderMapper.expired(tempOrder.getId());

    }

    @Override
    public void modifyOrderAmount(String orderSn, Double amount) {
        TempOrder tempOrder = this.findBy("orderSn", orderSn);

        if (Objects.isNull(tempOrder)) {
            return;
        }

        tempOrder.setShouldPayAmount(amount);

        this.update(tempOrder);

    }
}
