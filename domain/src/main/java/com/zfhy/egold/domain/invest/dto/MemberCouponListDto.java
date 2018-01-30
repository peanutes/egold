package com.zfhy.egold.domain.invest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCouponListDto {
    
    private Integer id;

    
    private Integer type;


    
    private Integer status;


    
    private Double couponAmount;


    
    private Date insertTime;


    
    private Date beginTime;

    
    private Date useTime;


    
    private Date endTime;

    
    private String remarks;

    
    private String memberMobile;

}
