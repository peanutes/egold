package com.zfhy.egold.domain.member.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.report.dto.CountDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface IdcardMapper extends Mapper<Idcard> {
    @Select("select * from member_idcard i where i.member_id in (#{memberIds})")
    List<Idcard> findByMemberIds(String memberIds);

    List<CountDto> statisticBindIdcard(Map<String, String> params);
}
