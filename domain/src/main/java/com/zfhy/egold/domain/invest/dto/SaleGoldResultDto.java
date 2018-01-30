package com.zfhy.egold.domain.invest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;



@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleGoldResultDto {
    
    private Integer id;
    
    private String goldWeight;
    
    private String price;
    
    private String charge;

}
