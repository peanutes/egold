package com.zfhy.egold.domain.invest.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QueryCouponResultDto {


    
    private Integer memberId;


    
    private String couponAmountLabel;

    
    private String couponTypeLabel;


    
    private Integer status;

    
    private String endTimeStr;

    
    private String investAmountMinDesc;

    
    private String investDeadlineMinDesc;




    
    public MemberCoupon convertTo() {
        return  new MemberCouponDtoConvert().convert(this);
    }

    
    public QueryCouponResultDto convertFrom(MemberCoupon memberCoupon) {
        return new MemberCouponDtoConvert().reverse().convert(memberCoupon);

    }


    private static class MemberCouponDtoConvert extends  Converter<QueryCouponResultDto, MemberCoupon> {


        @Override
        protected MemberCoupon doForward(QueryCouponResultDto memberCouponDto) {
            MemberCoupon memberCoupon = new MemberCoupon();
            BeanUtils.copyProperties(memberCouponDto, memberCoupon);
            return memberCoupon;
        }

        @Override
        protected QueryCouponResultDto doBackward(MemberCoupon memberCoupon) {
            QueryCouponResultDto memberCouponDto = new QueryCouponResultDto();
            BeanUtils.copyProperties(memberCoupon, memberCouponDto);

            if (memberCoupon.getType() == CouponType.CASH_COUPON.getCode()) {
                
                memberCouponDto.setCouponAmountLabel(String.format("%d元", memberCoupon.getCouponAmount().intValue()));
                memberCouponDto.setCouponTypeLabel("现金红包");
            } else {
                memberCouponDto.setCouponAmountLabel(String.join("", DoubleUtil.toString(memberCoupon.getCouponAmount()), "%"));
                memberCouponDto.setCouponTypeLabel("加息卷");
            }

            if (memberCoupon.getEndTime() != null) {
                memberCouponDto.setEndTimeStr(DateFormatUtils.format(memberCoupon.getEndTime(), "yyyy-MM-dd"));
            }
            return memberCouponDto;
        }
    }
}
