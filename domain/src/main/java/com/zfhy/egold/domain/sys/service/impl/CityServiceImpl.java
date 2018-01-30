package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.sys.dao.CityMapper;
import com.zfhy.egold.domain.sys.dto.CityDto;
import com.zfhy.egold.domain.sys.entity.City;
import com.zfhy.egold.domain.sys.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;



@Service
@Transactional
public class CityServiceImpl extends AbstractService<City> implements CityService{
    @Autowired
    private CityMapper cityMapper;

    @Override
    public CityDto findCityAndSubCityById(int id) {
        City city = this.findById(id);

        if (Objects.isNull(city)) {
            throw new BusException("找不到对应的城市");
        }
        CityDto cityDto = new CityDto().convertFrom(city);

        List<CityDto> cityDtoList = this.findByPid(id);

        cityDto.setCityDtoList(cityDtoList);

        return cityDto;

    }

    public List<CityDto> findByPid(int pid) {
        return this.cityMapper.findByPid(pid);

    }
}
