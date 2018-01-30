package com.zfhy.egold.domain.gold.dto;

import com.google.common.base.Converter;
import com.google.gson.Gson;
import com.zfhy.egold.domain.gold.entity.DailyPrice;
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
/**
 * {
 "x": [],
 "y": []
 }
 */
public class PriceDto {


    
    private Double price;

    
    private String createDate;

    public static void main(String[] args) {
        PriceDto priceDto = new PriceDto();
        priceDto.setPrice(233.12D);
        priceDto.setCreateDate("20170922195600");
        System.out.println(new Gson().toJson(priceDto));
    }



}
