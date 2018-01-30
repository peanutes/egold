package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.Dict;
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
public class DictDto {


    
    private String type;
    

    
    private String labelName;
    

    
    private String value;
    

    
    private String des;
    

    
    private Integer sort;
    

    
    private String remarks;
    



    
    public Dict convertTo() {
        return  new DictDtoConvert().convert(this);
    }

    
    public DictDto convertFrom(Dict dict) {
        return new DictDtoConvert().reverse().convert(dict);

    }


    private static class DictDtoConvert extends  Converter<DictDto, Dict> {


        @Override
        protected Dict doForward(DictDto dictDto) {
            Dict dict = new Dict();
            BeanUtils.copyProperties(dictDto, dict);
            return dict;
        }

        @Override
        protected DictDto doBackward(Dict dict) {
            DictDto dictDto = new DictDto();
            BeanUtils.copyProperties(dict, dictDto);
            return dictDto;
        }
    }
}
