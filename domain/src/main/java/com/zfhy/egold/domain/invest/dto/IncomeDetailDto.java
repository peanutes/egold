package com.zfhy.egold.domain.invest.dto;

import com.zfhy.egold.domain.invest.entity.IncomeDetail;
import com.google.common.base.Converter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import javax.persistence.Column;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDetailDto {


    
    private Integer investId;

    
    private Integer memberId;
    

    
    private Double income;
    

    
    private Date incomeTime;
    

    
    private String remarks;
    



    
    public IncomeDetail convertTo() {
        return  new IncomeDetailDtoConvert().convert(this);
    }

    
    public IncomeDetailDto convertFrom(IncomeDetail incomeDetail) {
        return new IncomeDetailDtoConvert().reverse().convert(incomeDetail);

    }


    private static class IncomeDetailDtoConvert extends  Converter<IncomeDetailDto, IncomeDetail> {


        @Override
        protected IncomeDetail doForward(IncomeDetailDto incomeDetailDto) {
            IncomeDetail incomeDetail = new IncomeDetail();
            BeanUtils.copyProperties(incomeDetailDto, incomeDetail);
            return incomeDetail;
        }

        @Override
        protected IncomeDetailDto doBackward(IncomeDetail incomeDetail) {
            IncomeDetailDto incomeDetailDto = new IncomeDetailDto();
            BeanUtils.copyProperties(incomeDetail, incomeDetailDto);
            return incomeDetailDto;
        }
    }
}
