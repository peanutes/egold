package com.zfhy.egold.domain.fund.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.fund.entity.Recharge;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RechargeDto {


    
    private Integer memberId;
    

    
    private Date rechargeTime;
    

    
    private String rechargeType;
    

    
    private String bankName;
    

    
    private Double rechargeAmount;
    

    
    private Double rechargeFee;
    

    
    private String status;
    

    
    private String payNo;
    

    
    private String payAccount;
    

    
    private String ip;
    

    
    private Integer adminId;
    

    
    private String operator;
    

    
    private String remarks;
    



    
    public Recharge convertTo() {
        return  new RechargeDtoConvert().convert(this);
    }

    
    public RechargeDto convertFrom(Recharge recharge) {
        return new RechargeDtoConvert().reverse().convert(recharge);

    }


    private static class RechargeDtoConvert extends  Converter<RechargeDto, Recharge> {


        @Override
        protected Recharge doForward(RechargeDto rechargeDto) {
            Recharge recharge = new Recharge();
            BeanUtils.copyProperties(rechargeDto, recharge);
            return recharge;
        }

        @Override
        protected RechargeDto doBackward(Recharge recharge) {
            RechargeDto rechargeDto = new RechargeDto();
            BeanUtils.copyProperties(recharge, rechargeDto);
            return rechargeDto;
        }
    }
}
