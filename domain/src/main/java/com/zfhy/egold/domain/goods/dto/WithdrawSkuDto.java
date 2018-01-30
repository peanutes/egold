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
public class WithdrawSkuDto {


    
    private Integer id;
    

    
    private Integer spuId;
    

    
    private Double spec;
    

    
    private Integer stock;

    



    
    public Sku convertTo() {
        return  new SkuDtoConvert().convert(this);
    }

    
    public WithdrawSkuDto convertFrom(Sku sku) {
        return new SkuDtoConvert().reverse().convert(sku);

    }


    private static class SkuDtoConvert extends  Converter<WithdrawSkuDto, Sku> {


        @Override
        protected Sku doForward(WithdrawSkuDto skuDto) {
            Sku sku = new Sku();
            BeanUtils.copyProperties(skuDto, sku);
            return sku;
        }

        @Override
        protected WithdrawSkuDto doBackward(Sku sku) {
            WithdrawSkuDto skuDto = new WithdrawSkuDto();
            BeanUtils.copyProperties(sku, skuDto);
            return skuDto;
        }
    }
}
