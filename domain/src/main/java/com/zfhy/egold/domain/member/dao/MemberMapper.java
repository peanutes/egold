package com.zfhy.egold.domain.member.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.report.dto.CountDto;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface MemberMapper extends Mapper<Member> {
    @Update("update member_member set bankcard_bind=0 where id=#{id} ")
    void unbindCard(Integer id);

    @Update("update member_member set bind_wechat=1 where id=#{id}")
    void bindWechat(Integer id);

    List<CountDto> statisticRegister(Map<String, String> params);
}
