package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.domain.member.dao.MemberOauthMapper;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.dto.OauthLoginResponse;
import com.zfhy.egold.domain.member.dto.OauthType;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.entity.MemberOauth;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.LoginLogService;
import com.zfhy.egold.domain.member.service.MemberOauthService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;
import java.util.Objects;





@Service
@Transactional
@Slf4j
public class MemberOauthServiceImpl extends AbstractService<MemberOauth> implements MemberOauthService{
    @Autowired
    private MemberOauthMapper memberOauthMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private BankcardService bankcardService;


    @Override
    public OauthLoginResponse hadBind(String openId, OauthType oauthType, SysParameterWithoutToken sysParameterWithoutToken, String ip) {
        MemberOauth memberOauth = this.findByOpenId(openId, oauthType);

        if (Objects.isNull(memberOauth)) {
            return OauthLoginResponse.builder().hadBind(false).build();
        } else {
            Integer memberId = memberOauth.getMemberId();
            Member member = this.memberService.findById(memberId);

            MemberOutPutDto memberOutPutDto = this.memberService.getMemberOutPutDto(sysParameterWithoutToken, member, ip);

            return OauthLoginResponse.builder()
                    .hadBind(true)
                    .memberOutPutDto(memberOutPutDto)
                    .build();
        }




    }

    @Override
    public void bindWechat(String openId, Integer memberId, OauthType oauthType) {
        this.memberService.bindWechat(memberId);

        MemberOauth memberOauth = new MemberOauth();
        memberOauth.setUpdateDate(new Date());
        memberOauth.setDelFlag("0");
        memberOauth.setMemberId(memberId);
        memberOauth.setCreateDate(new Date());
        memberOauth.setOpenId(openId);
        memberOauth.setType(oauthType.name());
        this.save(memberOauth);

    }

    private MemberOauth findByOpenId(String openId, OauthType oauthType) {
        Condition condition = new Condition(MemberOauth.class);
        condition.createCriteria()
                .andEqualTo("openId", openId)
                .andEqualTo("type", oauthType.name());

        List<MemberOauth> memberOauths = this.findByCondition(condition);

        if (CollectionUtils.isEmpty(memberOauths)) {
            return null;
        }
        return memberOauths.get(0);
    }
}
