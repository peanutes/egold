package com.zfhy.egold.domain.goods.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PcHomeSkuDto {
    
    private String productName;

    
    private Double price;

    
    private String listImgUrl;


}
