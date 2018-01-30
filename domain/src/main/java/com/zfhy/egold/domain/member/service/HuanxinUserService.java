package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.entity.HuanxinUser;
import com.zfhy.egold.common.core.Service;



public interface HuanxinUserService  extends Service<HuanxinUser> {

    HuanxinUser register(Integer memberId, String mobilePhone);

    void getOrRegister(MemberOutPutDto memberOutPutDto, Integer memberId);
}
