package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.Config;
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
public class ConfigDto {


    
    private String name;
    

    
    private String value;
    

    
    private String type;
    

    
    private String remarks;
    



    
    public Config convertTo() {
        return  new ConfigDtoConvert().convert(this);
    }

    
    public ConfigDto convertFrom(Config config) {
        return new ConfigDtoConvert().reverse().convert(config);

    }


    private static class ConfigDtoConvert extends  Converter<ConfigDto, Config> {


        @Override
        protected Config doForward(ConfigDto configDto) {
            Config config = new Config();
            BeanUtils.copyProperties(configDto, config);
            return config;
        }

        @Override
        protected ConfigDto doBackward(Config config) {
            ConfigDto configDto = new ConfigDto();
            BeanUtils.copyProperties(config, configDto);
            return configDto;
        }
    }
}
