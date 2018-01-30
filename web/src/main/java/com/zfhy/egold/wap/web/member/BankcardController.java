package com.zfhy.egold.wap.web.member;

import com.zfhy.egold.common.aspect.ann.BindBankCard;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.member.dto.BankDto;
import com.zfhy.egold.domain.member.dto.BankcardDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.BankcardUnbindApplicationService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/member/bankcard")
@Api(value = "BankcardController",tags = "BankcardController", description = "银行卡", position = 99)
@Slf4j
public class BankcardController {
    @Resource
    private BankcardService bankcardService;


    @Autowired
    private RedisService redisService;

    @Resource
    private MemberService memberService;

    @Autowired
    private BankcardUnbindApplicationService bankcardUnbindApplicationService;




    
    @PostMapping("/bankCardCheck")
    public Result<BankDto> bankCardCheck(
            @ApiParam(name = "bankCardNo", value = "银行卡号", required = true) @NotBlank(message = "银行卡号必填") @RequestParam
            String bankCardNo,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        BankDto bankDto = this.bankcardService.bankCardCheck(bankCardNo);


        return ResultGenerator.genSuccessResult(bankDto);
    }



    
    @PostMapping("/bindCardRequest")
    public Result<String> bindCardRequest(
            @ApiParam(name = "bankCardNo", value = "银行卡号", required = true) @NotBlank(message = "银行卡号必填") @RequestParam String bankCardNo,
            @ApiParam(name = "mobile", value = "银行预留手机号", required = true) @NotBlank(message = "手机号不能为空！") @RequestParam String mobile,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        String bindCardRequestNo = this.bankcardService.bindCardRequest(memberSession.getId(), bankCardNo, mobile);

        return ResultGenerator.genSuccessResult(bindCardRequestNo);
    }



    @BindBankCard
    
    @PostMapping("/bindCardConfirm")
    public Result<String> bindCardConfirm(
            @ApiParam(name = "bindCardRequestNo", value = "绑卡请求号", required = true) @NotBlank(message = "绑卡请求号必填") @RequestParam
            String bindCardRequestNo,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true) @NotBlank(message = "短信验证码不能为空！") @RequestParam
            String smsCode,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        this.bankcardService.bindCardConfirm(memberSession.getId(), bindCardRequestNo, smsCode);

        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/unbindBankcard")
    public Result<String> unbindBankcard(
            @ApiParam(name = "idcardFrontImg", value = "身份证正面照片", required = true) @NotBlank(message = "身份证正面照片必填") @URL(message = "请输入有效的图片地址") @RequestParam String idcardFrontImg,
            @ApiParam(name = "idcardBackImg", value = "身份证反面照片", required = true) @NotBlank(message = "身份证反面照片必填") @URL(message = "请输入有效的图片地址") @RequestParam String idcardBackImg,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        this.bankcardUnbindApplicationService.applyUnbindBankCard(memberSession.getId(), idcardFrontImg, idcardBackImg);


        return ResultGenerator.genSuccessResult();
    }


    
    @PostMapping("/myBankcard")
    public Result<BankcardDto> myBankcard(@ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        BankcardDto bankcardDto =  this.memberService.myBankcard(memberSession.getId());


        return ResultGenerator.genSuccessResult(bankcardDto);
    }
}
