package com.zfhy.egold.domain.goods.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface GoodsOrderMapper extends Mapper<GoodsOrder> {
    @Update("update goods_goods_order set status=3 where id=#{param1} and status not in(3, 4, 5)")
    int paySuccess(Integer id);

    @Update("update goods_goods_order set status=#{param2} where id=#{param1} and status not in(3, 4, 5)")
    int payFail(Integer id, int status);

    @Update("update goods_goods_order set status=#{param2} where order_sn=#{param1} and status=#{param3}")
    void updateStatusByOrderSn(String orderSn, int newStatus, int oldStatus);

    @Select("select * from goods_goods_order o where o.status in (1,2) and o.create_date < DATE_SUB(NOW(), INTERVAL #{expiredTime} SECOND)")
    List<GoodsOrder> findExpired(Integer payExpiredSec);

    @Update("update goods_goods_order set status=#{param3}, version=version+1 where id=#{param1} and version=#{param2}")
    int updateExpiredStatus(Integer id, Integer version, int status);

    List<GoodsOrder> list(Map<String, String> params);
}
