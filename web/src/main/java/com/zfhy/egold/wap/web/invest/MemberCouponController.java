package com.zfhy.egold.wap.web.invest;

import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.invest.dto.CashAndDiscountCouponDto;
import com.zfhy.egold.domain.invest.dto.CouponStatus;
import com.zfhy.egold.domain.invest.dto.CouponType;
import com.zfhy.egold.domain.invest.dto.QueryCouponResultDto;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import com.zfhy.egold.domain.invest.service.MemberCouponService;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/invest/member/coupon")
@Api(value = "MemberCouponController",tags = "MemberCouponController", description = "优惠卷")
@Slf4j
@Validated
public class MemberCouponController {
    @Resource
    private MemberCouponService memberCouponService;

    @Autowired
    private RedisService redisService;


    
    @PostMapping("/enableCoupons")
    public Result<CashAndDiscountCouponDto> enableCoupons(
            @ApiParam(name = "productId", value = "理财产品id", required = true)
            @NotNull @RequestParam Integer productId,
            @ApiParam(name = "orderAmount", value = "订单金额", required = true)
            @NotNull @RequestParam Double orderAmount,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        CashAndDiscountCouponDto cashAndDiscountCouponDto = this.memberCouponService.findMemberEnableCoupons(memberSession.getId(), productId, orderAmount);
        return ResultGenerator.genSuccessResult(cashAndDiscountCouponDto);
    }
    
    @PostMapping("/enableCouponsBySwitch")
    public Result<CashAndDiscountCouponDto> enableCouponsBySwitch(
            @ApiParam(name = "productId", value = "理财产品id", required = true)
            @NotNull @RequestParam Integer productId,
            @ApiParam(name = "switchGoldWeight", value = "转入克重", required = true)
            @NotNull @RequestParam Double switchGoldWeight,
            @ModelAttribute @Valid SysParameter sysParameter) {
        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        Double realTimePrice = this.redisService.getRealTimePrice();
        CashAndDiscountCouponDto cashAndDiscountCouponDto = this.memberCouponService.findMemberEnableCoupons(memberSession.getId(), productId, DoubleUtil.doubleMul(realTimePrice, switchGoldWeight));

        return ResultGenerator.genSuccessResult(cashAndDiscountCouponDto);
    }

    
    @PostMapping("/queryCoupon")
    public Result<List<QueryCouponResultDto>> queryCoupon(
            @ApiParam(name = "couponType", value = "优惠卷类型", required = true)
            @NotNull @RequestParam CouponType couponType,
            @ApiParam(name = "couponStatus", value = "优惠卷状态", required = true)
            @NotNull @RequestParam CouponStatus couponStatus,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        Condition condition = new Condition(MemberCoupon.class);
        condition.createCriteria()
                .andEqualTo("type", couponType.getCode())
                .andEqualTo("status", couponStatus.getCode())
                .andEqualTo("memberId", memberSession.getId())
                .andEqualTo("delFlag", "0");

        condition.setOrderByClause("create_date desc");

        List<MemberCoupon> couponList = this.memberCouponService.findByCondition(condition);


        List<QueryCouponResultDto> queryCouponResultDtos = couponList.stream()
                .map(e -> new QueryCouponResultDto().convertFrom(e))
                .collect(Collectors.toList());

        return ResultGenerator.genSuccessResult(queryCouponResultDtos);
    }


}
