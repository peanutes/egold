package com.zfhy.egold.domain.goods.dto;

import com.zfhy.egold.domain.goods.entity.GoodsDetail;
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
public class GoodsDetailDto {


    
    private Integer spuId;
    

    
    private String content;
    

    
    private String remarks;
    



    
    public GoodsDetail convertTo() {
        return  new GoodsDetailDtoConvert().convert(this);
    }

    
    public GoodsDetailDto convertFrom(GoodsDetail goodsDetail) {
        return new GoodsDetailDtoConvert().reverse().convert(goodsDetail);

    }


    private static class GoodsDetailDtoConvert extends  Converter<GoodsDetailDto, GoodsDetail> {


        @Override
        protected GoodsDetail doForward(GoodsDetailDto goodsDetailDto) {
            GoodsDetail goodsDetail = new GoodsDetail();
            BeanUtils.copyProperties(goodsDetailDto, goodsDetail);
            return goodsDetail;
        }

        @Override
        protected GoodsDetailDto doBackward(GoodsDetail goodsDetail) {
            GoodsDetailDto goodsDetailDto = new GoodsDetailDto();
            BeanUtils.copyProperties(goodsDetail, goodsDetailDto);
            return goodsDetailDto;
        }
    }
}
