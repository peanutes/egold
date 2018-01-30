package com.zfhy.egold.domain.cms.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HotInfoDetailDto {

    
    private int id;

    
    private String title;
    

    
    private String source;
    

    
    private String content;
    
    
    private String href;

    
    private String publishTime;
    
    
    public HotInfo convertTo() {
        return  new HotInfoDtoConvert().convert(this);
    }

    
    public HotInfoDetailDto convertFrom(HotInfo hotInfo) {
        return new HotInfoDtoConvert().reverse().convert(hotInfo);

    }

    private static class HotInfoDtoConvert extends  Converter<HotInfoDetailDto, HotInfo> {

        @Override
        protected HotInfo doForward(HotInfoDetailDto hotInfoDto) {
            HotInfo hotInfo = new HotInfo();
            BeanUtils.copyProperties(hotInfoDto, hotInfo);
            return hotInfo;
        }

        @Override
        protected HotInfoDetailDto doBackward(HotInfo hotInfo) {
            HotInfoDetailDto hotInfoDto = new HotInfoDetailDto();
            BeanUtils.copyProperties(hotInfo, hotInfoDto);
            String date = DateFormatUtils.format(hotInfo.getCreateDate(),"yyyy-MM-dd HH:mm");
            hotInfoDto.setPublishTime(date);
            return hotInfoDto;
        }
    }


}
