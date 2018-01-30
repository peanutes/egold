package com.zfhy.egold.domain.sys.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.sys.dto.CityDto;
import com.zfhy.egold.domain.sys.entity.City;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CityMapper extends Mapper<City> {
    @Select("select * from sys_city where pid=#{param1}")
    List<CityDto> findByPid(int pid);
}
