package com.zfhy.egold.domain.order.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.order.entity.PaymentLog;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface PaymentLogMapper extends Mapper<PaymentLog> {
    List<PaymentLog> list(Map<String, String> params);

    @Update("update order_payment_log set status=#{param2}, error_msg=#{param3} where status not in (2, 3) and  pay_request_no=#{param1}")
    int updateWaitCallBack(String payRequestNo, int status, String errorMsg);


}
