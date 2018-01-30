package com.zfhy.egold.domain.member.dto;

import com.zfhy.egold.domain.member.entity.BindBankcardLog;
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
public class BindBankcardLogDto {


    
    private Integer memberId;
    

    
    private String requestNo;
    

    
    private String cardNo;
    

    
    private String realName;
    

    
    private String idcardNo;
    

    
    private Integer status;
    

    
    private String mobile;
    

    
    private String errorMsg;
    

    
    private String remarks;
    



    
    public BindBankcardLog convertTo() {
        return  new BindBankcardLogDtoConvert().convert(this);
    }

    
    public BindBankcardLogDto convertFrom(BindBankcardLog bindBankcardLog) {
        return new BindBankcardLogDtoConvert().reverse().convert(bindBankcardLog);

    }


    private static class BindBankcardLogDtoConvert extends  Converter<BindBankcardLogDto, BindBankcardLog> {


        @Override
        protected BindBankcardLog doForward(BindBankcardLogDto bindBankcardLogDto) {
            BindBankcardLog bindBankcardLog = new BindBankcardLog();
            BeanUtils.copyProperties(bindBankcardLogDto, bindBankcardLog);
            return bindBankcardLog;
        }

        @Override
        protected BindBankcardLogDto doBackward(BindBankcardLog bindBankcardLog) {
            BindBankcardLogDto bindBankcardLogDto = new BindBankcardLogDto();
            BeanUtils.copyProperties(bindBankcardLog, bindBankcardLogDto);
            return bindBankcardLogDto;
        }
    }
}
