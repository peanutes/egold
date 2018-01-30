package com.zfhy.egold.domain.invest.dto;

import com.google.common.base.Converter;
import com.google.gson.Gson;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCouponDto {

    
    private Integer id;

    
    private Integer memberId;
    

    
    private Integer investId;
    

    
    private Integer type;
    

    
    private Integer status;
    

    
    private Double couponAmount;


    
    private String couponAmountStr;
    

    
    private Date insertTime;
    

    
    private Date beginTime;
    

    
    private Date endTime;
    

    
    private Date useTime;
    

    
    private Double investAmount;
    

    
    private Double investAmountMin;
    

    
    private Integer investDealineMin;
    

    
    private String productType;
    

    
    private String remarks;
    



    
    public MemberCoupon convertTo() {
        return  new MemberCouponDtoConvert().convert(this);
    }

    
    public MemberCouponDto convertFrom(MemberCoupon memberCoupon) {
        return new MemberCouponDtoConvert().reverse().convert(memberCoupon);

    }


    private static class MemberCouponDtoConvert extends  Converter<MemberCouponDto, MemberCoupon> {


        @Override
        protected MemberCoupon doForward(MemberCouponDto memberCouponDto) {
            MemberCoupon memberCoupon = new MemberCoupon();
            BeanUtils.copyProperties(memberCouponDto, memberCoupon);
            return memberCoupon;
        }

        @Override
        protected MemberCouponDto doBackward(MemberCoupon memberCoupon) {
            MemberCouponDto memberCouponDto = new MemberCouponDto();
            BeanUtils.copyProperties(memberCoupon, memberCouponDto);

            Double couponAmount = memberCoupon.getCouponAmount();
            if (Objects.equals(memberCouponDto.getType(), CouponType.DISCOUNT_COUPON.getCode())) {
                
                memberCouponDto.setCouponAmountStr(String.format("%s%%", DoubleUtil.toString(couponAmount)));
            } else {
                
                memberCouponDto.setCouponAmountStr(String.format("%s元", DoubleUtil.toString(couponAmount)));
            }
            return memberCouponDto;
        }
    }

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 34, 52, 3, 23, 6);

        integers.sort((i1, i2) -> i2.compareTo(i1));

        System.out.println(new Gson().toJson(integers));

    }

}
