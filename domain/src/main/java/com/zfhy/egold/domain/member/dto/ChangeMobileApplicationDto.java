package com.zfhy.egold.domain.member.dto;

import com.zfhy.egold.domain.member.entity.ChangeMobileApplication;
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
public class ChangeMobileApplicationDto {


    
    private Integer memberId;
    

    
    private String idcardHandImg;
    

    
    private String idcardFrontImg;
    

    
    private String idcardBackImg;
    

    
    private String bankCardImg;
    

    
    private String newMobile;
    

    
    private String oldMobile;
    

    
    private Integer status;
    

    
    private String auditor;
    

    
    private Date auditDate;
    

    
    private String remarks;
    



    
    public ChangeMobileApplication convertTo() {
        return  new ChangeMobileApplicationDtoConvert().convert(this);
    }

    
    public ChangeMobileApplicationDto convertFrom(ChangeMobileApplication changeMobileApplication) {
        return new ChangeMobileApplicationDtoConvert().reverse().convert(changeMobileApplication);

    }


    private static class ChangeMobileApplicationDtoConvert extends  Converter<ChangeMobileApplicationDto, ChangeMobileApplication> {


        @Override
        protected ChangeMobileApplication doForward(ChangeMobileApplicationDto changeMobileApplicationDto) {
            ChangeMobileApplication changeMobileApplication = new ChangeMobileApplication();
            BeanUtils.copyProperties(changeMobileApplicationDto, changeMobileApplication);
            return changeMobileApplication;
        }

        @Override
        protected ChangeMobileApplicationDto doBackward(ChangeMobileApplication changeMobileApplication) {
            ChangeMobileApplicationDto changeMobileApplicationDto = new ChangeMobileApplicationDto();
            BeanUtils.copyProperties(changeMobileApplication, changeMobileApplicationDto);
            return changeMobileApplicationDto;
        }
    }
}
