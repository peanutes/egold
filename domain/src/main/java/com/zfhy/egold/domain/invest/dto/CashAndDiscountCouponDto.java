package com.zfhy.egold.domain.invest.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CashAndDiscountCouponDto {

    
    private List<MemberCouponDto> cashCoupons = Lists.newArrayList();

    
    private List<MemberCouponDto> discountCoupons = Lists.newArrayList();

}
