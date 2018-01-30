package com.zfhy.egold.domain.order.dto;

import com.zfhy.egold.domain.order.entity.MyorderAttr;
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
public class MyorderAttrDto {


    
    private Integer myorderId;
    

    
    private String attrName;
    

    
    private String attrValue;
    

    
    private Integer sort;
    



    
    public MyorderAttr convertTo() {
        return  new MyorderAttrDtoConvert().convert(this);
    }

    
    public MyorderAttrDto convertFrom(MyorderAttr myorderAttr) {
        return new MyorderAttrDtoConvert().reverse().convert(myorderAttr);

    }


    private static class MyorderAttrDtoConvert extends  Converter<MyorderAttrDto, MyorderAttr> {


        @Override
        protected MyorderAttr doForward(MyorderAttrDto myorderAttrDto) {
            MyorderAttr myorderAttr = new MyorderAttr();
            BeanUtils.copyProperties(myorderAttrDto, myorderAttr);
            return myorderAttr;
        }

        @Override
        protected MyorderAttrDto doBackward(MyorderAttr myorderAttr) {
            MyorderAttrDto myorderAttrDto = new MyorderAttrDto();
            BeanUtils.copyProperties(myorderAttr, myorderAttrDto);
            return myorderAttrDto;
        }
    }
}
