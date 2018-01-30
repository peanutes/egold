package com.zfhy.egold.domain.report.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountDto {

    
    private String countDate;

    
    private Integer count;

}
