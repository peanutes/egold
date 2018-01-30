package com.zfhy.egold.gateway.juhe;

import com.google.common.base.Converter;
import com.google.gson.annotations.SerializedName;
import com.zfhy.egold.domain.gold.entity.RealtimePrice;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
* Created by CodeGenerator on 2017/09/24.
*/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShGoldPriceDto {


    @SerializedName("time")
    @ApiModelProperty(value = "更新时间")
    private String updateTime;
    

    @SerializedName("latestpri")
    @ApiModelProperty(value = "最新价格")
    private String latestPrice;
    

    @SerializedName("yespri")
    @ApiModelProperty(value = "昨收价格")
    private String yesPrice;
    

    @SerializedName("openpri")
    @ApiModelProperty(value = "开盘价")
    private String openPrice;
    

    @SerializedName("maxpri")
    @ApiModelProperty(value = "最高价")
    private String maxPrice;
    

    @SerializedName("minpri")
    @ApiModelProperty(value = "最低价")
    private String minPrice;
    

    @SerializedName("limit")
    @ApiModelProperty(value = "涨跌幅")
    private String changePercent;
    



    /**
     * 实体转dto
     *
     * @return
     */
    public RealtimePrice convertTo() {
        return  new RealtimePriceDtoConvert().convert(this);
    }

    /**
     * dto转实体
     *
     * @param realtimePrice
     * @return
     */
    public ShGoldPriceDto convertFrom(RealtimePrice realtimePrice) {
        return new RealtimePriceDtoConvert().reverse().convert(realtimePrice);

    }


    private static class RealtimePriceDtoConvert extends  Converter<ShGoldPriceDto, RealtimePrice> {


        @Override
        protected RealtimePrice doForward(ShGoldPriceDto realtimePriceDto) {
            RealtimePrice realtimePrice = new RealtimePrice();
            BeanUtils.copyProperties(realtimePriceDto, realtimePrice);
            return realtimePrice;
        }

        @Override
        protected ShGoldPriceDto doBackward(RealtimePrice realtimePrice) {
            ShGoldPriceDto realtimePriceDto = new ShGoldPriceDto();
            BeanUtils.copyProperties(realtimePrice, realtimePriceDto);
            return realtimePriceDto;
        }
    }
}
