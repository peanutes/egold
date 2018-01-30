package com.zfhy.egold.domain.report.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatisticsDto {
    
    private String productName;
    
    private Integer buyerCount;
    
    private Integer buyerTimes;
    
    private Double investAmount;


}
