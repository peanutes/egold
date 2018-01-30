package com.zfhy.egold.domain.goods.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.goods.entity.GoodsProperty;
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
public class GoodsPropertyDto {


    
    private Integer id;

    
    private Integer spuId;
    

    
    private String propertyName;
    

    
    private String propertyValue;
    

    
    private String remarks;
    



    
    public GoodsProperty convertTo() {
        return  new GoodsPropertyDtoConvert().convert(this);
    }

    
    public GoodsPropertyDto convertFrom(GoodsProperty goodsProperty) {
        return new GoodsPropertyDtoConvert().reverse().convert(goodsProperty);

    }


    private static class GoodsPropertyDtoConvert extends  Converter<GoodsPropertyDto, GoodsProperty> {


        @Override
        protected GoodsProperty doForward(GoodsPropertyDto goodsPropertyDto) {
            GoodsProperty goodsProperty = new GoodsProperty();
            BeanUtils.copyProperties(goodsPropertyDto, goodsProperty);
            return goodsProperty;
        }

        @Override
        protected GoodsPropertyDto doBackward(GoodsProperty goodsProperty) {
            GoodsPropertyDto goodsPropertyDto = new GoodsPropertyDto();
            BeanUtils.copyProperties(goodsProperty, goodsPropertyDto);
            return goodsPropertyDto;
        }
    }
}
