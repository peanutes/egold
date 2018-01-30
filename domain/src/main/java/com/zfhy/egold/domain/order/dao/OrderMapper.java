package com.zfhy.egold.domain.order.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.report.dto.ProductStatisticsDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends Mapper<Order> {
    @Update("update order_order set status=#{param3},version=version+1 where id=#{param1} and version=#{param2} and status in (1,2)")
    int updateStatus(Integer id, Integer version, int orderStatus);

    @Update("update order_order set status=1, request_pay_time=now() where id=#{id} and status=0")
    void confirm(Integer id);

    @Select("select * from order_order o where o.status in (1,2) and o.request_pay_time < DATE_SUB(NOW(), INTERVAL #{expiredTime} SECOND)")
    List<Order> findExpired(Integer expiredTime);



    @Update("update order_order set status=#{param2}  where order_sn=#{param1} and status=#{param3}")
    void updateStatusByOrderSn(String orderSn, int newStatus, int oldStatus);

    List<Order> list(Map<String, String> params);

    @Select("SELECT CASE WHEN finance_product_type = 'RISE_FALL' AND product_name LIKE '%涨%' THEN '黄金看涨定期' WHEN finance_product_type = 'RISE_FALL' AND product_name LIKE '%跌%' THEN '黄金看跌定期' ELSE product_name END productName, count(distinct(member_id)) buyerCount, count(1) buyerTimes, sum(o.total_amount) investAmount FROM order_order o WHERE o.del_flag = '0' AND o. STATUS = 3 and member_id != 22 GROUP BY CASE WHEN finance_product_type = 'RISE_FALL' AND product_name LIKE '%涨%' THEN '黄金看涨定期' WHEN finance_product_type = 'RISE_FALL' AND product_name LIKE '%跌%' THEN '黄金看跌定期' ELSE product_name END")
    List<ProductStatisticsDto> productBuyerStatistics();


}
