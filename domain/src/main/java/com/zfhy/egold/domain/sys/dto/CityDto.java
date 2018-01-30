package com.zfhy.egold.domain.sys.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.sys.entity.City;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {


    
    private Integer id;
    

    
    private Integer pid;
    

    
    private String name;
    

    
    private String pingyin;
    

    
    private String level;
    

    
    private Integer sort;
    

    
    private String remarks;

    
    private List<CityDto> cityDtoList;
    



    
    public City convertTo() {
        return  new CityDtoConvert().convert(this);
    }

    
    public CityDto convertFrom(City city) {
        return new CityDtoConvert().reverse().convert(city);

    }


    private static class CityDtoConvert extends  Converter<CityDto, City> {


        @Override
        protected City doForward(CityDto cityDto) {
            City city = new City();
            BeanUtils.copyProperties(cityDto, city);
            return city;
        }

        @Override
        protected CityDto doBackward(City city) {
            CityDto cityDto = new CityDto();
            BeanUtils.copyProperties(city, cityDto);
            return cityDto;
        }
    }
}
