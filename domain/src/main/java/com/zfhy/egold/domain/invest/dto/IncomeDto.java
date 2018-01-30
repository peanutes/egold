package com.zfhy.egold.domain.invest.dto;

import com.zfhy.egold.common.core.result.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class IncomeDto {
    
    private Double totalSum;
    
    private Page<IncomeDailyDto> incomeDailyDtoPage;

}
