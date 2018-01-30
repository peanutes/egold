package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.member.dao.IdcardMapper;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.service.IdcardService;
import com.zfhy.egold.domain.report.dto.CountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;



@Service
@Transactional
public class IdcardServiceImpl extends AbstractService<Idcard> implements IdcardService{
    @Autowired
    private IdcardMapper idcardMapper;

    @Override
    public List<Idcard> findByMemberIds(String memberIds) {
        return idcardMapper.findByMemberIds(memberIds);

    }

    @Override
    public Idcard findByIdcardNo(String idCardNo) {

        return this.findBy("idCard", idCardNo);
    }

    @Override
    public Idcard findByMemberId(Integer memberId) {

        return  this.findBy("memberId", memberId);
    }

    @Override
    public List<CountDto> statisticBindIdcard(Map<String, String> params) {
        return this.idcardMapper.statisticBindIdcard(params);
    }

}
