package com.zfhy.egold.domain.order.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.order.entity.MyorderAttr;

import java.util.List;



public interface MyorderAttrService  extends Service<MyorderAttr> {

    List<MyorderAttr> findByMyorderId(Integer myorderId);

    void batchInsert(Integer myOrderId,List<MyorderAttr> myorderAttrs);

}
