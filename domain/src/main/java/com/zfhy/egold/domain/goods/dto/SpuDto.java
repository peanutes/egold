package com.zfhy.egold.domain.goods.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.goods.entity.Spu;
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
public class SpuDto {


    
    private Integer id;
    

    
    private Integer goodsTypeId;
    

    
    private String goodsTypeName;
    

    
    private String goodsName;
    

    
    private String purityDes;
    

    
    private String weightDes;
    

    
    private String sizeDes;
    

    
    private String feeDes;
    

    
    private String imgUrl;
    

    
    private String remarks;
    



    
    public Spu convertTo() {
        return  new SpuDtoConvert().convert(this);
    }

    
    public SpuDto convertFrom(Spu spu) {
        return new SpuDtoConvert().reverse().convert(spu);

    }


    private static class SpuDtoConvert extends  Converter<SpuDto, Spu> {


        @Override
        protected Spu doForward(SpuDto spuDto) {
            Spu spu = new Spu();
            BeanUtils.copyProperties(spuDto, spu);
            return spu;
        }

        @Override
        protected SpuDto doBackward(Spu spu) {
            SpuDto spuDto = new SpuDto();
            BeanUtils.copyProperties(spu, spuDto);
            return spuDto;
        }
    }
}
