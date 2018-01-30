package com.zfhy.egold.domain.invest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDescDto {
    
    private String title;
    
    private String keyPoint;
    
    private String url;
}
