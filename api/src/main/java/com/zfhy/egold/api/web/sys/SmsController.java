package com.zfhy.egold.api.web.sys;

import com.zfhy.egold.api.aspect.HttpRequestAspect;
import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.validator.MobileValidate;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/sys/sms")
@Api(value = "SmsController",tags = "SmsController", description = "短信管理")
@Slf4j
@Validated
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberService memberService;


    
    @PostMapping("/send")
    public Result<String> send(
            @ApiParam(name = "mobile", value = "手机号", required = true) @NotBlank(message = "手机号不允许为空")
            @RequestParam @MobileValidate(message = "你好，您输入的手机号不正确") String mobile,
            @ApiParam(name = "smsTplType", value = "短信类型,REGISTER=注册短信，UPDATE_PWD=忘记密码短信,CHANGE_MOBILE=更改手机号", required = true)
            @NotNull(message = "短信类型不能为空") @RequestParam SmsTplType smsTplType,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        /*Boolean register = this.memberService.isRegister(mobile);
        if (register) {
            throw new BusException("您好，您的手机号已注册过，请直接登陆");
        }*/

        String smsCode = this.smsService.send(mobile, smsTplType, sysParameterWithoutToken.getTerminalType(), HttpRequestAspect.IP_THREAD_LOCAL.get());

        this.redisService.setSmsCode(mobile, smsCode, smsTplType);

        return ResultGenerator.genSuccessResult();
    }

}
