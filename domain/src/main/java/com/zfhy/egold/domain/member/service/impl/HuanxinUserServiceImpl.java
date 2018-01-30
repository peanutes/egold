package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.member.dao.HuanxinUserMapper;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.entity.HuanxinUser;
import com.zfhy.egold.common.core.AbstractService;
//import com.zfhy.egold.gateway.huanxin.HuanxinApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.member.service.HuanxinUserService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class HuanxinUserServiceImpl extends AbstractService<HuanxinUser> implements HuanxinUserService{
    @Value("${app.env}")
    private String env;

//    @Autowired
//    private HuanxinApi huanxinApi;
//    @Autowired
    private HuanxinUserMapper huanxinUserMapper;

    @Override
    public HuanxinUser register(Integer memberId, String mobilePhone) {

       /* String huanxinUserName = String.join("_", "egold", env, mobilePhone);
        String huanxinPwd = HashUtil.getMd5(String.join("_", memberId.toString(), StrFunUtil.genRandom(6)));
        boolean flag = this.huanxinApi.regUser(huanxinUserName, huanxinPwd);
        if (flag) {
            HuanxinUser huanxinUser = new HuanxinUser();
            huanxinUser.setCreateDate(new Date());
            huanxinUser.setHuanxinUserId(huanxinUserName);
            huanxinUser.setHuangxinPass(huanxinPwd);
            huanxinUser.setMemberId(memberId);
            this.huanxinUserMapper.insert(huanxinUser);
            return huanxinUser;

        }*/


        return null;
    }

    @Override
    public void getOrRegister(MemberOutPutDto memberOutPutDto, Integer memberId) {
        try {
            HuanxinUser huanxinUser = this.findBy("memberId", memberId);
            if (Objects.isNull(huanxinUser)) {
                huanxinUser = this.register(memberId, memberOutPutDto.getMobilePhone());
            }

            if (Objects.nonNull(huanxinUser)) {
                memberOutPutDto.setHuanxinId(huanxinUser.getHuanxinUserId());
                memberOutPutDto.setHuanxinPwd(huanxinUser.getHuangxinPass());

            }

        } catch (Throwable t) {
            
            log.error("注册环信用户时失败", t);
        }
    }
}
