package com.zfhy.egold.domain.sys.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.sys.entity.Notice;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticeMapper extends Mapper<Notice> {

    
    @Select("select * from sys_notice n where n.notice_status=2 and n.invalid_time > now() order by create_date desc")
    List<Notice> listAll();
}
