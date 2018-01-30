package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.sys.dto.CityDto;
import com.zfhy.egold.domain.sys.entity.City;
import com.zfhy.egold.common.core.Service;



public interface CityService  extends Service<City> {

    CityDto findCityAndSubCityById(int id);
}
