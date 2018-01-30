package com.zfhy.egold.domain.invest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RiseFallGoldDto {

    
    private String title;
    
    private String keyPoint;
    
    List<FinancialProductDto> riseAndFallProduct;
}
