package com.zfhy.egold.domain.member.dto;

import com.zfhy.egold.domain.member.entity.LoginLog;
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
public class LoginLogDto {


    
    private Integer memberId;
    

    
    private String terminalType;
    

    
    private String terminalId;
    

    
    private Date loginTime;
    

    
    private String loginIp;
    

    
    private String remarks;
    



    
    public LoginLog convertTo() {
        return  new LoginLogDtoConvert().convert(this);
    }

    
    public LoginLogDto convertFrom(LoginLog loginLog) {
        return new LoginLogDtoConvert().reverse().convert(loginLog);

    }


    private static class LoginLogDtoConvert extends  Converter<LoginLogDto, LoginLog> {


        @Override
        protected LoginLog doForward(LoginLogDto loginLogDto) {
            LoginLog loginLog = new LoginLog();
            BeanUtils.copyProperties(loginLogDto, loginLog);
            return loginLog;
        }

        @Override
        protected LoginLogDto doBackward(LoginLog loginLog) {
            LoginLogDto loginLogDto = new LoginLogDto();
            BeanUtils.copyProperties(loginLog, loginLogDto);
            return loginLogDto;
        }
    }
}
