package com.zfhy.egold.domain.invest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PcHomeNewProductDto {
    
    private Double price;
    
    private Integer term;
}
