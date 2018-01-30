package com.zfhy.egold.domain.order.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.order.entity.Myorder;
import org.apache.ibatis.annotations.Update;

public interface MyorderMapper extends Mapper<Myorder> {
    @Update("update order_myorder set myorder_status=2 where relate_order_id=#{param1}")
    void finish(String relateOrderId);

}
