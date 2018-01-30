package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.Sysfile;
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
public class SysfileDto {


    
    private String type;
    

    
    private String url;
    

    
    private String originFileName;
    

    
    private Integer sort;
    

    
    private String remarks;
    



    
    public Sysfile convertTo() {
        return  new SysfileDtoConvert().convert(this);
    }

    
    public SysfileDto convertFrom(Sysfile sysfile) {
        return new SysfileDtoConvert().reverse().convert(sysfile);

    }


    private static class SysfileDtoConvert extends  Converter<SysfileDto, Sysfile> {


        @Override
        protected Sysfile doForward(SysfileDto sysfileDto) {
            Sysfile sysfile = new Sysfile();
            BeanUtils.copyProperties(sysfileDto, sysfile);
            return sysfile;
        }

        @Override
        protected SysfileDto doBackward(Sysfile sysfile) {
            SysfileDto sysfileDto = new SysfileDto();
            BeanUtils.copyProperties(sysfile, sysfileDto);
            return sysfileDto;
        }
    }
}
