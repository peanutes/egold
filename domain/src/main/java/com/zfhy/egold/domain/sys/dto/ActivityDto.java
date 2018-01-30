package com.zfhy.egold.domain.sys.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.sys.entity.Activity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {


    
    private String name;
    

    
    private Integer type;
    

    
    private Date beginDate;
    

    
    private Date endDate;
    

    
    private String href;
    

    
    private String imgUrl;
    

    
    private String remarks;
    



    
    public Activity convertTo() {
        return  new ActivityDtoConvert().convert(this);
    }

    
    public ActivityDto convertFrom(Activity activity, String terminalType, String terminalId) {

        ActivityDto activityDto = new ActivityDtoConvert().reverse().convert(activity);
        if (StringUtils.isNotBlank(activityDto.getHref())) {
            activityDto.setHref(StrFunUtil.assembleUrl(terminalType, terminalId, activityDto.getHref()));
        }

        return activityDto;

    }


    private static class ActivityDtoConvert extends  Converter<ActivityDto, Activity> {


        @Override
        protected Activity doForward(ActivityDto activityDto) {
            Activity activity = new Activity();
            BeanUtils.copyProperties(activityDto, activity);
            return activity;
        }

        @Override
        protected ActivityDto doBackward(Activity activity) {
            ActivityDto activityDto = new ActivityDto();
            BeanUtils.copyProperties(activity, activityDto);
            return activityDto;
        }
    }
}
