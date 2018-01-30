package com.zfhy.egold.domain.order.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.order.entity.TempOrder;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TempOrderMapper extends Mapper<TempOrder> {
    @Select("select * from order_temp_order o where o.status=0 and o.create_date < DATE_SUB(NOW(), INTERVAL #{param1} SECOND)")
    List<TempOrder> findExpired(Integer tempOrderExpiredSec);

    @Update("update order_temp_order set status=5 where id=#{param1} and status=0")
    void expired(Integer id);
}
