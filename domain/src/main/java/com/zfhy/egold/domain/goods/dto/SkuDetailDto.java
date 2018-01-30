package com.zfhy.egold.domain.goods.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkuDetailDto {
    
    private Integer id;
    
    private String goodsName;
    
    private List<GoodsPropertyDto> goodsPropertyDtoList;
    
    private List<GoodsImgDto> goodsImgDtoList;
    
    private GoodsDetailDto goodsDetailDto;
    
    private Integer stock;
    
    private Double price;
    
    private Double spec;



}
