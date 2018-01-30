package com.zfhy.egold.domain.invest.dto;

import com.zfhy.egold.domain.invest.entity.Switchinfo;
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
public class SwitchinfoDto {


    
    private Integer investId;
    

    
    private Integer memberId;
    

    
    private Integer orderId;
    

    
    private Double goldWeight;
    

    
    private Integer status;
    

    
    private String remarks;
    



    
    public Switchinfo convertTo() {
        return  new SwitchinfoDtoConvert().convert(this);
    }

    
    public SwitchinfoDto convertFrom(Switchinfo switchinfo) {
        return new SwitchinfoDtoConvert().reverse().convert(switchinfo);

    }


    private static class SwitchinfoDtoConvert extends  Converter<SwitchinfoDto, Switchinfo> {


        @Override
        protected Switchinfo doForward(SwitchinfoDto switchinfoDto) {
            Switchinfo switchinfo = new Switchinfo();
            BeanUtils.copyProperties(switchinfoDto, switchinfo);
            return switchinfo;
        }

        @Override
        protected SwitchinfoDto doBackward(Switchinfo switchinfo) {
            SwitchinfoDto switchinfoDto = new SwitchinfoDto();
            BeanUtils.copyProperties(switchinfo, switchinfoDto);
            return switchinfoDto;
        }
    }
}
