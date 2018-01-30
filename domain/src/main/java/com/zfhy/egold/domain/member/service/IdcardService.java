package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.report.dto.CountDto;

import java.util.List;
import java.util.Map;



public interface IdcardService  extends Service<Idcard> {

    List<Idcard> findByMemberIds(String memberIds);

    Idcard findByIdcardNo(String idCardNo);

    Idcard findByMemberId(Integer memberId);


    List<CountDto> statisticBindIdcard(Map<String, String> params);
}
