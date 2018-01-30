package com.zfhy.egold.domain.member.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.member.dto.BankcardListDto;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.report.dto.CountDto;
import org.apache.ibatis.annotations.Delete;

import java.util.List;
import java.util.Map;

public interface BankcardMapper extends Mapper<Bankcard> {
    List<BankcardListDto> list(Map<String, String> params);

    @Delete("delete from member_bankcard where member_id=#{param1}")
    void deleteByMemberId(Integer memberId);

    List<CountDto> statisticBindCard(Map<String, String> params);
}
