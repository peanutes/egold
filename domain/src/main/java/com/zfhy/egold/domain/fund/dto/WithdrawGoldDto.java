package com.zfhy.egold.domain.fund.dto;

import com.zfhy.egold.domain.fund.entity.WithdrawGold;
import com.google.common.base.Converter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawGoldDto {


    
    private Integer id;
    

    
    private Integer memberId;
    

    
    private Date applyTime;
    

    
    private String withdrawType;
    

    
    private Double withdrawGoldWeight;
    

    
    private Date withdrawTime;
    

    
    private String deliverNo;
    

    
    private Integer storeId;
    

    
    private String storeName;
    

    
    private Integer adminId;
    

    
    private String operator;
    

    
    private String status;
    

    
    private String remarks;
    



    
    public WithdrawGold convertTo() {
        return  new WithdrawGoldDtoConvert().convert(this);
    }

    
    public WithdrawGoldDto convertFrom(WithdrawGold withdrawGold) {
        return new WithdrawGoldDtoConvert().reverse().convert(withdrawGold);

    }


    private static class WithdrawGoldDtoConvert extends  Converter<WithdrawGoldDto, WithdrawGold> {


        @Override
        protected WithdrawGold doForward(WithdrawGoldDto withdrawGoldDto) {
            WithdrawGold withdrawGold = new WithdrawGold();
            BeanUtils.copyProperties(withdrawGoldDto, withdrawGold);
            return withdrawGold;
        }

        @Override
        protected WithdrawGoldDto doBackward(WithdrawGold withdrawGold) {
            WithdrawGoldDto withdrawGoldDto = new WithdrawGoldDto();
            BeanUtils.copyProperties(withdrawGold, withdrawGoldDto);
            return withdrawGoldDto;
        }
    }
}
