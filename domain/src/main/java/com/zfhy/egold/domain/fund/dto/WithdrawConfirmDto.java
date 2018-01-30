package com.zfhy.egold.domain.fund.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawConfirmDto {
    
    private String withdrawAmount;
    
    private String fee;
    
    private String payAmount;
    
    private String receiveTime;

}
