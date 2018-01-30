package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.AppVersion;
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
public class AppVersionDto {


    
    private String appName;
    

    
    private String versionCode;
    

    
    private String versionName;
    

    
    private String apkUrl;
    

    
    private String changeLog;
    

    
    private String appType;
    

    
    private String updateTips;
    

    
    private Date upgradeTime;
    

    
    private Date createTime;
    

    
    private String forceUpdate;
    



    
    public AppVersion convertTo() {
        return  new AppVersionDtoConvert().convert(this);
    }

    
    public AppVersionDto convertFrom(AppVersion appVersion) {
        return new AppVersionDtoConvert().reverse().convert(appVersion);

    }


    private static class AppVersionDtoConvert extends  Converter<AppVersionDto, AppVersion> {


        @Override
        protected AppVersion doForward(AppVersionDto appVersionDto) {
            AppVersion appVersion = new AppVersion();
            BeanUtils.copyProperties(appVersionDto, appVersion);
            return appVersion;
        }

        @Override
        protected AppVersionDto doBackward(AppVersion appVersion) {
            AppVersionDto appVersionDto = new AppVersionDto();
            BeanUtils.copyProperties(appVersion, appVersionDto);
            return appVersionDto;
        }
    }
}
