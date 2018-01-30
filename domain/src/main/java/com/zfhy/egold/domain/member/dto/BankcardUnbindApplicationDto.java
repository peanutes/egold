package com.zfhy.egold.domain.member.dto;

import com.zfhy.egold.domain.member.entity.BankcardUnbindApplication;
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
public class BankcardUnbindApplicationDto {


    
    private Integer memberId;
    

    
    private String idcardFrontImg;
    

    
    private String idcardBackImg;
    

    
    private Integer status;
    

    
    private String realName;
    

    
    private String originBankCard;
    

    
    private String mobile;
    

    
    private String remarks;
    



    
    public BankcardUnbindApplication convertTo() {
        return  new BankcardUnbindApplicationDtoConvert().convert(this);
    }

    
    public BankcardUnbindApplicationDto convertFrom(BankcardUnbindApplication bankcardUnbindApplication) {
        return new BankcardUnbindApplicationDtoConvert().reverse().convert(bankcardUnbindApplication);

    }


    private static class BankcardUnbindApplicationDtoConvert extends  Converter<BankcardUnbindApplicationDto, BankcardUnbindApplication> {


        @Override
        protected BankcardUnbindApplication doForward(BankcardUnbindApplicationDto bankcardUnbindApplicationDto) {
            BankcardUnbindApplication bankcardUnbindApplication = new BankcardUnbindApplication();
            BeanUtils.copyProperties(bankcardUnbindApplicationDto, bankcardUnbindApplication);
            return bankcardUnbindApplication;
        }

        @Override
        protected BankcardUnbindApplicationDto doBackward(BankcardUnbindApplication bankcardUnbindApplication) {
            BankcardUnbindApplicationDto bankcardUnbindApplicationDto = new BankcardUnbindApplicationDto();
            BeanUtils.copyProperties(bankcardUnbindApplication, bankcardUnbindApplicationDto);
            return bankcardUnbindApplicationDto;
        }
    }
}
