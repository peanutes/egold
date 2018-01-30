package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.AdminLog;
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
public class AdminLogDto {


    
    private String loginAccount;
    

    
    private String operateMethod;
    

    
    private String operateInput;
    

    
    private String operateOutput;
    

    
    private String remarks;
    

    
    private String ip;
    

    
    private Long time;
    



    
    public AdminLog convertTo() {
        return  new AdminLogDtoConvert().convert(this);
    }

    
    public AdminLogDto convertFrom(AdminLog adminLog) {
        return new AdminLogDtoConvert().reverse().convert(adminLog);

    }


    private static class AdminLogDtoConvert extends  Converter<AdminLogDto, AdminLog> {


        @Override
        protected AdminLog doForward(AdminLogDto adminLogDto) {
            AdminLog adminLog = new AdminLog();
            BeanUtils.copyProperties(adminLogDto, adminLog);
            return adminLog;
        }

        @Override
        protected AdminLogDto doBackward(AdminLog adminLog) {
            AdminLogDto adminLogDto = new AdminLogDto();
            BeanUtils.copyProperties(adminLog, adminLogDto);
            return adminLogDto;
        }
    }
}
