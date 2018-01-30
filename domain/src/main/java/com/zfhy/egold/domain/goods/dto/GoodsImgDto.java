package com.zfhy.egold.domain.goods.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.goods.entity.GoodsImg;
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
public class GoodsImgDto {


    
    private Integer id;

    
    private Integer spuId;
    

    
    private String imgUrl;
    

    
    private String remarks;
    



    
    public GoodsImg convertTo() {
        return  new GoodsImgDtoConvert().convert(this);
    }

    
    public GoodsImgDto convertFrom(GoodsImg goodsImg) {
        return new GoodsImgDtoConvert().reverse().convert(goodsImg);

    }


    private static class GoodsImgDtoConvert extends  Converter<GoodsImgDto, GoodsImg> {


        @Override
        protected GoodsImg doForward(GoodsImgDto goodsImgDto) {
            GoodsImg goodsImg = new GoodsImg();
            BeanUtils.copyProperties(goodsImgDto, goodsImg);
            return goodsImg;
        }

        @Override
        protected GoodsImgDto doBackward(GoodsImg goodsImg) {
            GoodsImgDto goodsImgDto = new GoodsImgDto();
            BeanUtils.copyProperties(goodsImg, goodsImgDto);
            return goodsImgDto;
        }
    }
}
