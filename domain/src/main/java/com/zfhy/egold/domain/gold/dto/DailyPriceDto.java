package com.zfhy.egold.domain.gold.dto;

import com.zfhy.egold.domain.gold.entity.DailyPrice;
import com.google.common.base.Converter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyPriceDto {


    
    private Double price;
    



    
    public DailyPrice convertTo() {
        return  new DailyPriceDtoConvert().convert(this);
    }

    
    public DailyPriceDto convertFrom(DailyPrice dailyPrice) {
        return new DailyPriceDtoConvert().reverse().convert(dailyPrice);

    }


    private static class DailyPriceDtoConvert extends  Converter<DailyPriceDto, DailyPrice> {


        @Override
        protected DailyPrice doForward(DailyPriceDto dailyPriceDto) {
            DailyPrice dailyPrice = new DailyPrice();
            BeanUtils.copyProperties(dailyPriceDto, dailyPrice);
            return dailyPrice;
        }

        @Override
        protected DailyPriceDto doBackward(DailyPrice dailyPrice) {
            DailyPriceDto dailyPriceDto = new DailyPriceDto();
            BeanUtils.copyProperties(dailyPrice, dailyPriceDto);
            return dailyPriceDto;
        }
    }
}
