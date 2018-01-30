package com.zfhy.egold.domain.invest.dto;

import com.zfhy.egold.domain.invest.entity.SoldDetail;
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
public class SoldDetailDto {


    
    private Integer memberId;
    

    
    private Date soldTime;
    

    
    private Double goldWeight;
    

    
    private String productName;
    

    
    private Double price;
    

    
    private Double incomeAmount;
    

    
    private Double totalAmount;
    

    
    private Double fee;
    

    
    private String remarks;
    



    
    public SoldDetail convertTo() {
        return  new SoldDetailDtoConvert().convert(this);
    }

    
    public SoldDetailDto convertFrom(SoldDetail soldDetail) {
        return new SoldDetailDtoConvert().reverse().convert(soldDetail);

    }


    private static class SoldDetailDtoConvert extends  Converter<SoldDetailDto, SoldDetail> {


        @Override
        protected SoldDetail doForward(SoldDetailDto soldDetailDto) {
            SoldDetail soldDetail = new SoldDetail();
            BeanUtils.copyProperties(soldDetailDto, soldDetail);
            return soldDetail;
        }

        @Override
        protected SoldDetailDto doBackward(SoldDetail soldDetail) {
            SoldDetailDto soldDetailDto = new SoldDetailDto();
            BeanUtils.copyProperties(soldDetail, soldDetailDto);
            return soldDetailDto;
        }
    }
}
