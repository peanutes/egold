package com.zfhy.egold.domain.sys.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.sys.entity.Sms;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

public interface SmsMapper extends Mapper<Sms> {

    @Select("select count(1) from sys_sms s where s.mobile=#{param1} and s.tpl_type=#{param2} and s.send_time > DATE_SUB(SYSDATE(),INTERVAL #{param3} second) ")
    int countByMobile(@Param("mobile") String mobile, @Param("smsTplCode") String smsTplCode, @Param("second") int second);

}
