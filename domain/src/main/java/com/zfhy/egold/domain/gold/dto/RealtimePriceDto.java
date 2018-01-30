package com.zfhy.egold.domain.gold.dto;

import com.google.gson.annotations.SerializedName;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.gold.entity.RealtimePrice;
import com.google.common.base.Converter;
import com.zfhy.egold.gateway.juhe.ShGoldPriceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RealtimePriceDto {


    @SerializedName("time")
    
    private Date updateTime;
    

    @SerializedName("latestpri")
    
    private Double latestPrice;
    

    @SerializedName("yespri")
    
    private Double yesPrice;
    

    @SerializedName("openpri")
    
    private Double openPrice;
    

    @SerializedName("maxpri")
    
    private Double maxPrice;
    

    @SerializedName("minpri")
    
    private Double minPrice;
    

    @SerializedName("limit")
    
    private Double changePercent;
    



    
    public RealtimePrice convertTo() {
        return  new RealtimePriceDtoConvert().convert(this);
    }

    
    public RealtimePriceDto convertFrom(RealtimePrice realtimePrice) {
        return new RealtimePriceDtoConvert().reverse().convert(realtimePrice);

    }

    public RealtimePriceDto convertFromShGoldPriceDto(ShGoldPriceDto shGoldPriceDto) {
        if (Objects.isNull(shGoldPriceDto)) {
            return null;
        }

        RealtimePriceDto realtimePriceDto = new RealtimePriceDto();

        realtimePriceDto.setChangePercent(this.getDoubleFromString(shGoldPriceDto.getChangePercent()));
        realtimePriceDto.setLatestPrice(this.getDoubleFromString(shGoldPriceDto.getLatestPrice()));
        realtimePriceDto.setMaxPrice(this.getDoubleFromString(shGoldPriceDto.getMaxPrice()));
        realtimePriceDto.setMinPrice(this.getDoubleFromString(shGoldPriceDto.getMinPrice()));

        realtimePriceDto.setOpenPrice(this.getDoubleFromString(shGoldPriceDto.getOpenPrice()));
        realtimePriceDto.setUpdateTime(DateUtil.convertStringToDate(DateUtil.YYYY_MM_DD_HH_MM_SS, shGoldPriceDto.getUpdateTime()));

        realtimePriceDto.setYesPrice(this.getDoubleFromString(shGoldPriceDto.getYesPrice()));

        return realtimePriceDto;
    }

    private Double getDoubleFromString(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (Objects.equals("--", str)) {
            return null;
        }

        if (str.contains("%")) {
            str = str.replaceAll("%", "");
        }

        return new Double(str);

    }


    private static class RealtimePriceDtoConvert extends  Converter<RealtimePriceDto, RealtimePrice> {


        @Override
        protected RealtimePrice doForward(RealtimePriceDto realtimePriceDto) {
            RealtimePrice realtimePrice = new RealtimePrice();
            BeanUtils.copyProperties(realtimePriceDto, realtimePrice);
            return realtimePrice;
        }

        @Override
        protected RealtimePriceDto doBackward(RealtimePrice realtimePrice) {
            RealtimePriceDto realtimePriceDto = new RealtimePriceDto();
            BeanUtils.copyProperties(realtimePrice, realtimePriceDto);
            return realtimePriceDto;
        }
    }
}
