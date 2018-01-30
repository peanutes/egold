package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.Sms;
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
public class SmsDto {


    
    private String mobile;
    

    
    private String code;
    

    
    private String tplType;
    

    
    private Date sendTime;
    

    
    private String ip;
    

    
    private String remarks;
    



    
    public Sms convertTo() {
        return  new SmsDtoConvert().convert(this);
    }

    
    public SmsDto convertFrom(Sms sms) {
        return new SmsDtoConvert().reverse().convert(sms);

    }


    private static class SmsDtoConvert extends  Converter<SmsDto, Sms> {


        @Override
        protected Sms doForward(SmsDto smsDto) {
            Sms sms = new Sms();
            BeanUtils.copyProperties(smsDto, sms);
            return sms;
        }

        @Override
        protected SmsDto doBackward(Sms sms) {
            SmsDto smsDto = new SmsDto();
            BeanUtils.copyProperties(sms, smsDto);
            return smsDto;
        }
    }
}
