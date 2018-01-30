package com.zfhy.egold.domain.goods.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.goods.entity.Sku;
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
public class SkuDto {


    
    private Integer id;
    

    
    private Integer spuId;
    

    
    private Double spec;
    

    
    private Integer stock;


    
    private String goodName;

    
    private Double price;

    
    private String listImgUrl;

    
    private String barcode;

    
    private Integer sort;


    
    private Double fee;

    



    
    public Sku convertTo() {
        return  new SkuDtoConvert().convert(this);
    }

    
    public SkuDto convertFrom(Sku sku) {
        return new SkuDtoConvert().reverse().convert(sku);

    }


    private static class SkuDtoConvert extends  Converter<SkuDto, Sku> {


        @Override
        protected Sku doForward(SkuDto skuDto) {
            Sku sku = new Sku();
            BeanUtils.copyProperties(skuDto, sku);
            return sku;
        }

        @Override
        protected SkuDto doBackward(Sku sku) {
            SkuDto skuDto = new SkuDto();
            BeanUtils.copyProperties(sku, skuDto);
            return skuDto;
        }
    }
}
