package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.domain.member.dto.OauthLoginResponse;
import com.zfhy.egold.domain.member.dto.OauthType;
import com.zfhy.egold.domain.member.entity.MemberOauth;
import com.zfhy.egold.common.core.Service;



public interface MemberOauthService  extends Service<MemberOauth> {


    OauthLoginResponse hadBind(String openId, OauthType oauthType, SysParameterWithoutToken sysParameterWithoutToken, String ip);

    void bindWechat(String openId, Integer memberId, OauthType oauthType);
}
