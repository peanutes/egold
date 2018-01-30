package com.zfhy.egold.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAccountDto {


    
    private Double totalAssets;

    
    private Double yesterdayProfit;

    
    private Double totalProfit;

    
    private Double goldBalance;

    
    private Double goldBalanceAmount;

    
    private Double enableBalance;


    
    private Double investBalance;

    
    private Double currentGoldBalance;


    
    private Double currentGoldBalanceRise;


    
    private Double currentGoldBalanceFall;




    
    private Double TermGoldBalance;

    
    private Integer bankcardBind;






}
