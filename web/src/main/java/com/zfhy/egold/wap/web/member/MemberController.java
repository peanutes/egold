package com.zfhy.egold.wap.web.member;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.aspect.ann.Register;
import com.zfhy.egold.common.constant.AppEnvConst;
import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.validator.MobileValidate;
import com.zfhy.egold.domain.fund.service.FundRecordService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.member.dto.*;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.*;
import com.zfhy.egold.domain.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zfhy.egold.wap.aspect.HttpRequestAspect.IP_THREAD_LOCAL;


@RestController
@RequestMapping("/member/member")
@Api(value = "MemberController",tags = "MemberController", description = "会员")
@Slf4j
@Validated
public class MemberController {
    @Resource
    private MemberService memberService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private HuanxinUserService huanxinUserService;

    @Autowired
    private BankcardUnbindApplicationService bankcardUnbindApplicationService;

    @Autowired
    private InvestRecordService investRecordService;

    @Autowired
    private BankcardService bankcardService;

    @Autowired
    private ChangeMobileApplicationService changeMobileApplicationService;

    @Autowired
    private FundRecordService fundRecordService;
    @Value("${app.env}")
    private String appEnv;


    
    @PostMapping("/login")
    public Result<MemberOutPutDto> login(
            @ApiParam(name = "userName", value = "用户名/手机号/email", required = true)
            @NotBlank @RequestParam @MobileValidate String userName,
            @ApiParam(name = "password", value = "密码", required = true) @NotBlank(message = "密码不允许为空") @RequestParam String password,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        
        Member member = memberService.login(userName, password);

        MemberOutPutDto memberOutPutDto = this.memberService.getMemberOutPutDto(sysParameterWithoutToken, member, IP_THREAD_LOCAL.get());


        return ResultGenerator.genSuccessResult(memberOutPutDto);
    }

    
    @PostMapping("/logout")
    public Result<String> logout(@ModelAttribute @Valid SysParameter sysParameter) {

            MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
            this.redisService.logout(sysParameter.getToken());




        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/validateRegisterSmsCode")
    public Result<String> validateRegisterSmsCode(
            @ApiParam(name = "mobile", value = "手机号", required = true)
            @NotBlank(message = "手机号必填") @MobileValidate @RequestParam String mobile,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true) @NotBlank(message = "短信验证码不允许为为") @RequestParam String smsCode,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        String code = this.redisService.getSmsCode(mobile, SmsTplType.REGISTER);

        if (!Objects.equals(smsCode, code)) {
            throw new BusException("验证码不正确，请重新输入");
        }

        return ResultGenerator.genSuccessResult();
    }


    @Register
    
    @PostMapping("/register")
    public Result<MemberOutPutDto> register(
            @ApiParam(name = "mobile", value = "手机号", required = true)
            @NotBlank(message = "手机号必填") @MobileValidate @RequestParam String mobile,
            @ApiParam(name = "password", value = "密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "密码不能为空") @Length(min = 6, max = 16, message = "密码不小于6位数") String password,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true) @NotBlank @RequestParam String smsCode,
            @ApiParam(name = "refereeMobile", value = "推荐人手机号", required = false) @RequestParam(required = false) String refereeMobile,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        Member member = this.memberService.register(mobile, password, smsCode, refereeMobile);

        MemberOutPutDto memberOutPutDto = this.memberService.getMemberOutPutDto(sysParameterWithoutToken, member, IP_THREAD_LOCAL.get());

        return ResultGenerator.genSuccessResult(memberOutPutDto);
    }


    
    @PostMapping("/updatePwd")
    public Result<String> updatePwd(
            @ApiParam(name = "originPassword", value = "原密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "原密码不能为空") @Length(min = 6, max = 16, message = "密码不能小于6位数") String originPassword,
            @ApiParam(name = "password", value = "新密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "密码不能为空") @Length(min = 6, max = 16, message = "密码不能小于6位数") String password,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        this.memberService.updatePwd(memberSession.getId(), password, originPassword);
        return ResultGenerator.genSuccessResult("更新成功");
    }

    
    @PostMapping("/forgetPwd")
    public Result<String> forgetPwd(
            @ApiParam(name = "mobile", value = "手机号", required = true)
            @NotBlank(message = "手机号必填") @MobileValidate @RequestParam String mobile,
            @ApiParam(name = "password", value = "新密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "密码不能为空") @Length(min = 6, max = 16, message = "密码不能小于6位数") String password,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true) @NotBlank @RequestParam String smsCode,
            @ApiParam(name = "realName", value = "真实姓名", required = false) @RequestParam(required = false) String realName,
            @ApiParam(name = "idCardNo", value = "身份证号", required = false) @RequestParam(required = false) String idCardNo,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        this.memberService.forgetPwd(mobile, password, smsCode, realName, idCardNo);
        return ResultGenerator.genSuccessResult("更新成功");
    }


    
    @PostMapping("/isRegister")
    public Result<Boolean> isRegister(
            @ApiParam(name = "mobile", value = "手机号", required = true)
            @NotBlank(message = "手机号必填") @MobileValidate @RequestParam String mobile,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        Boolean isRegister = this.memberService.isRegister(mobile);

        return ResultGenerator.genSuccessResult(isRegister);
    }


    
    @PostMapping("/accountOverview")
    public Result<MemberAccountDto> accountOverview(@ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        MemberAccountDto memberAccountDto = this.memberService.getAccountOverview(memberSession.getId());

        return ResultGenerator.genSuccessResult(memberAccountDto);
    }

    
    @PostMapping("/realNameAuth")
    public Result<String> realNameAuth(@ApiParam(name = "realName", value = "真实姓名", required = true) @NotBlank(message = "真实姓名必填") @RequestParam String realName,
                                       @ApiParam(name = "idCard", value = "身份证", required = true) @Length(min = 15, max = 18, message = "身份证长度为15或者18") @NotBlank(message = "身份证必填") @RequestParam String idCard,
                                       @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        this.memberService.realNameAuth(memberSession.getId(), realName, idCard);

        return ResultGenerator.genSuccessResult();
    }



    
    @PostMapping("/settingDealPwd")
    public Result<String> settingDealPwd(
            @ApiParam(name = "dealPwd", value = "交易密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "交易密码不能为空") @Length(min = 6, max = 16, message = "交易密码不能小于6位数") String dealPwd,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        this.memberService.settingDealPwd(memberSession.getId(), dealPwd);
        return ResultGenerator.genSuccessResult("设置成功");
    }


    
    @PostMapping("/myInvitation")
    public Result<MyInvitationDto> myInvitation(
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        PageHelper.startPage(page, size);
        List<Member> memberList = this.memberService.findInvitation(memberSession.getId());

        PageInfo<Member> memberPageInfo = new PageInfo<>(memberList);

        Page<MyInvitationMember> myInvitationDtoPage = new Page<>();
        BeanUtils.copyProperties(memberPageInfo, myInvitationDtoPage);
        if (CollectionUtils.isEmpty(memberList)) {
            return ResultGenerator.genSuccessResult(MyInvitationDto.EMPTY);
        }
        List<MyInvitationMember> myInvitationMembers = memberList.stream().map(ele -> {
            int count = this.investRecordService.countInvest(ele.getId());

            return MyInvitationMember.builder()
                    .registerDate(DateUtil.toString(ele.getCreateDate(), DateUtil.YYYY_MM_DD))
                    .mobile(ele.getMobilePhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"))
                    .invisted(count > 0 ? "是" : "否").build();
        }).collect(Collectors.toList());

        myInvitationDtoPage.setList(myInvitationMembers);


        Double commission = this.fundRecordService.queryCommission(memberSession.getId());

        MyInvitationDto myInvitationDto = MyInvitationDto.builder()
                .myInvitationMemberPage(myInvitationDtoPage)
                .commission(commission)
                .build();

        return ResultGenerator.genSuccessResult(myInvitationDto);
    }


    
    @PostMapping("/deleteMember")
    public Result<String> deleteMember(@ApiParam(name = "mobile", value = "手机号", required = true)
                                       @RequestParam @NotNull(message = "手机号不允许为空") String mobile) {

        if (Objects.equals(appEnv, AppEnvConst.prd.name())) {
            
            return ResultGenerator.genSuccessResult();
        }

        Member member = this.memberService.findBy("mobilePhone", mobile);
        if (Objects.isNull(member)) {
            return ResultGenerator.genSuccessResult();
        }
        this.memberService.deleteByMemberId(member.getId());

        return ResultGenerator.genSuccessResult();
    }



    
    @PostMapping("/changeMobileWithoutSms")
    public Result<String> changeMobileWithoutSms(
            @ApiParam(name = "idcardHandImg", value = "手持身份证照片", required = true) @NotBlank(message = "手持身份证照片必填") @URL(message = "请输入有效的图片地址") @RequestParam
            String idcardHandImg,
            @ApiParam(name = "idcardFrontImg", value = "身份证正面照片", required = true) @NotBlank(message = "身份证正面照片必填") @URL(message = "请输入有效的图片地址") @RequestParam
            String idcardFrontImg,
            @ApiParam(name = "idcardBackImg", value = "身份证反面照片", required = true) @NotBlank(message = "身份证反面照片必填") @URL(message = "请输入有效的图片地址") @RequestParam
            String idcardBackImg,
            @ApiParam(name = "bankCardImg", value = "银行卡照片", required = true) @NotBlank(message = "银行卡照片必填") @URL(message = "请输入有效的图片地址") @RequestParam
            String bankCardImg,
            @ApiParam(name = "newMobile", value = "新手机号", required = true)
            @NotBlank(message = "新手机号必填") @MobileValidate @RequestParam
            String newMobile,
            @ApiParam(name = "smsCode", value = "新手机验证码", required = true) @NotBlank(message = "新手机验证码") @RequestParam
            String smsCode,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        this.changeMobileApplicationService.applyChangeMobile(memberSession.getId(), memberSession.getMobilePhone(), newMobile, smsCode, idcardHandImg, idcardFrontImg, idcardBackImg, bankCardImg);


        return ResultGenerator.genSuccessResult("更改手机号申请成功，我们会尽快处理，请耐心等待");
    }


    
    @PostMapping("/validateDealPwd")
    public Result<String> validateDealPwd(
            @ApiParam(name = "dealPwd", value = "交易密码", required = true) @NotBlank(message = "交易密码必填")@RequestParam
            String dealPwd,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        this.memberService.validateDealPwd(memberSession.getId(), dealPwd);

        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/validateChangeMobileSmsCode")
    public Result<String> validateChangeMobileSmsCode(
            @ApiParam(name = "smsCode", value = "短信验证码", required = true) @NotBlank(message = "短信验证码必填")@RequestParam
            String smsCode,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        String sessionSms = this.redisService.getSmsCode(memberSession.getMobilePhone(), SmsTplType.CHANGE_MOBILE);
        if (!Objects.equals(sessionSms, smsCode)) {
            throw new BusException("您好，验证码错误，请重新输入");
        }

        return ResultGenerator.genSuccessResult();
    }


    
    @PostMapping("/changeMobileWithSms")
    public Result<String> changeMobileWithSms(
            @ApiParam(name = "idCardNo", value = "身份证号", required = true) @NotBlank(message = "身份证号必填") @RequestParam
            String idCardNo,
            @ApiParam(name = "realName", value = "真实姓名", required = true) @NotBlank(message = "真实姓名必填") @RequestParam
            String realName,
            @ApiParam(name = "newMobile", value = "新手机号", required = true)
            @NotBlank(message = "新手机号必填") @MobileValidate @RequestParam
            String newMobile,
            @ApiParam(name = "smsCode", value = "新手机验证码", required = true) @NotBlank(message = "新手机验证码必填") @RequestParam
            String smsCode,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        this.changeMobileApplicationService.applyChangeMobileWithSms(memberSession.getId(), idCardNo, realName, newMobile, smsCode);

        return ResultGenerator.genSuccessResult();
    }







}
