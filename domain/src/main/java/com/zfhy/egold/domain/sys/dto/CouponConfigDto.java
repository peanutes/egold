package com.zfhy.egold.domain.sys.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.sys.entity.CouponConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponConfigDto {


    
    private Integer useScene;
    

    
    private Integer type;
    

    
    private Double couponAmount;
    

    
    private Integer effectDay;
    

    
    private Double investAmountMin;
    

    
    private String investAmountMinDesc;
    

    
    private Integer investDeadlineMin;
    

    
    private String investDeadlineMinDesc;
    

    
    private Double conditionInvestAmount;
    

    
    private String conditionDes;
    

    
    private String productType;
    

    
    private String remarks;
    



    
    public CouponConfig convertTo() {
        return  new CouponConfigDtoConvert().convert(this);
    }

    
    public CouponConfigDto convertFrom(CouponConfig couponConfig) {
        return new CouponConfigDtoConvert().reverse().convert(couponConfig);

    }


    private static class CouponConfigDtoConvert extends  Converter<CouponConfigDto, CouponConfig> {


        @Override
        protected CouponConfig doForward(CouponConfigDto couponConfigDto) {
            CouponConfig couponConfig = new CouponConfig();
            BeanUtils.copyProperties(couponConfigDto, couponConfig);
            return couponConfig;
        }

        @Override
        protected CouponConfigDto doBackward(CouponConfig couponConfig) {
            CouponConfigDto couponConfigDto = new CouponConfigDto();
            BeanUtils.copyProperties(couponConfig, couponConfigDto);
            return couponConfigDto;
        }
    }
}
