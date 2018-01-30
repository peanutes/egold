package com.zfhy.egold.domain.goods.dto;

import com.zfhy.egold.domain.member.dto.AddressOutputDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawGoldFeeDto {
    
    private Double insuranceFee;


    
    private Double manMadeFee;

    
    private Double expressFee;


    
    private List<AddressOutputDto> addressDtoList;

}
