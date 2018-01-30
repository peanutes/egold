package com.zfhy.egold.api.web.oauth;

import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.dto.OauthLoginResponse;
import com.zfhy.egold.domain.member.dto.OauthType;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.MemberOauthService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.gateway.wechat.WeChatApi;
import com.zfhy.egold.gateway.wechat.dto.GetAccessTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static com.zfhy.egold.api.aspect.HttpRequestAspect.IP_THREAD_LOCAL;


@RestController
@RequestMapping("/oauth/wechat")
@Api(value = "WeChatController", tags = "WeChatController", description = "微信登陆")
@Slf4j
public class WeChatController {

    @Autowired
    private WeChatApi weChatApi;

    @Autowired
    private MemberOauthService memberOauthService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberService memberService;


    
    @PostMapping("/getAccessToken")
    public Result<GetAccessTokenResponse> getAccessToken(
            @ApiParam(name = "code", value = "用户确认授权后的临时票据", required = true)
            @NotBlank(message = "用户确认授权后的临时票据必填") @RequestParam
            String code,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {


        GetAccessTokenResponse accessToken = weChatApi.getAccessToken(code);


        return ResultGenerator.genSuccessResult(accessToken);
    }

    
    @PostMapping("/hadBind")
    public Result<OauthLoginResponse> hadBind(
            @ApiParam(name = "openId", value = "openId", required = true)
            @NotBlank(message = "openId必填") @RequestParam
            String openId,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {


        OauthLoginResponse oauthLoginRespon = this.memberOauthService.hadBind(openId, OauthType.wechat, sysParameterWithoutToken, IP_THREAD_LOCAL.get());


        return ResultGenerator.genSuccessResult(oauthLoginRespon);
    }


    
    @PostMapping("/validateSmsCode")
    public Result<OauthLoginResponse> validateSmsCode(
            @ApiParam(name = "openId", value = "openId", required = true)
            @NotBlank(message = "openId必填") @RequestParam String openId,
            @ApiParam(name = "mobile", value = "手机号", required = true) @NotBlank(message = "手机号必填")
            @RequestParam String mobile,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true) @NotBlank(message = "短信验证码不允许为空")
            @RequestParam String smsCode,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        String code = this.redisService.getSmsCode(mobile, SmsTplType.REGISTER);

        if (!Objects.equals(smsCode, code)) {
            throw new BusException("验证码不正确，请重新输入");
        }


        OauthLoginResponse oauthLoginResponse;

        Member member = this.memberService.findByMobile(mobile);


        if (Objects.nonNull(member)) {
            this.memberOauthService.bindWechat(openId, member.getId(), OauthType.wechat);
            
            MemberOutPutDto memberOutPutDto = this.memberService.getMemberOutPutDto(sysParameterWithoutToken, member, IP_THREAD_LOCAL.get());

            oauthLoginResponse = OauthLoginResponse.builder()
                    .hadBind(true)
                    .memberOutPutDto(memberOutPutDto)
                    .build();

        } else{
            oauthLoginResponse = OauthLoginResponse.builder()
                    .hadBind(false)
                    .build();

        }

        return ResultGenerator.genSuccessResult(oauthLoginResponse);
    }

    
    @PostMapping("/registerAndBind")
    public Result<MemberOutPutDto> registerAndBind(
            @ApiParam(name = "openId", value = "openId", required = true)
            @NotBlank(message = "openId必填") @RequestParam String openId,
            @ApiParam(name = "mobile", value = "手机号", required = true)
            @NotBlank(message = "手机号必填") @ModelAttribute @RequestParam String mobile,
            @ApiParam(name = "password", value = "密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "密码不能为空") @Length(min = 6, max = 16, message = "密码不小于6位数") String password,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true) @NotBlank @RequestParam String smsCode,
            @ApiParam(name = "refereeMobile", value = "推荐人手机号", required = false) @RequestParam(required = false) String refereeMobile,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {


        Member member =  this.memberService.register(mobile, password, smsCode, refereeMobile);


        this.memberOauthService.bindWechat(openId, member.getId(), OauthType.wechat);

        MemberOutPutDto memberOutPutDto = this.memberService.getMemberOutPutDto(sysParameterWithoutToken, member, IP_THREAD_LOCAL.get());



        return ResultGenerator.genSuccessResult(memberOutPutDto);
    }


    
    @PostMapping("/bindWechat")
    public Result<String> bindWechat(
            @ApiParam(name = "openId", value = "openId", required = true)
            @NotBlank(message = "openId必填") @RequestParam
            String openId,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        this.memberOauthService.bindWechat(openId, member.getId(), OauthType.wechat);


        return ResultGenerator.genSuccessResult();
    }



}
