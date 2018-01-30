package com.zfhy.egold.wap.web.sys;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.entity.City;
import com.zfhy.egold.domain.sys.dto.CityDto;
import com.zfhy.egold.domain.sys.service.CityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import org.hibernate.validator.constraints.*;

import com.zfhy.egold.common.core.parameter.SysParameter;

import javax.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;
import java.util.Date;
import java.util.stream.Collectors;
import com.zfhy.egold.common.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.*;
import tk.mybatis.mapper.entity.Example;
import org.springframework.beans.BeanUtils;
import java.math.BigDecimal;
import com.zfhy.egold.common.core.result.Page;



@RequestMapping("/sys/city")

@Slf4j
public class CityController {
    @Resource
    private CityService cityService;

    
    @PostMapping("/add")
    public Result<String> add(CityDto cityDto, SysParameter sysParameter) {
        City city = cityDto.convertTo();
        cityService.save(city);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "城市区域表id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        cityService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "城市区域表id", required = true) @NotBlank @RequestParam Integer id,
                                CityDto cityDto, SysParameter sysParameter) {
        City city = this.cityService.findById(id);
        if (Objects.isNull(city) || city.getId() <= 0) {
            throw new BusException("城市区域表不存在");
        }
        if (Objects.nonNull(cityDto.getId())) {
            city.setId(cityDto.getId());
        }
        if (Objects.nonNull(cityDto.getPid())) {
            city.setPid(cityDto.getPid());
        }
        if (Objects.nonNull(cityDto.getName())) {
            city.setName(cityDto.getName());
        }
        if (Objects.nonNull(cityDto.getPingyin())) {
            city.setPingyin(cityDto.getPingyin());
        }
        if (Objects.nonNull(cityDto.getLevel())) {
            city.setLevel(cityDto.getLevel());
        }
        if (Objects.nonNull(cityDto.getSort())) {
            city.setSort(cityDto.getSort());
        }

        cityService.update(city);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<CityDto> detail(@ApiParam(name = "id", value = "城市区域表id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        City city = cityService.findById(id);
        return ResultGenerator.genSuccessResult(new CityDto().convertFrom(city));
    }

    
    @PostMapping("/list")
    public Result<Page<CityDto>> list(CityDto cityDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(City.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(cityDto.getId())) {
            criteria.andEqualTo("id", cityDto.getId());
        }
        if (Objects.nonNull(cityDto.getPid())) {
            criteria.andEqualTo("pid", cityDto.getPid());
        }
        if (StringUtils.isNotBlank(cityDto.getName())) {
            criteria.andLike("name", cityDto.getName());
        }
        if (StringUtils.isNotBlank(cityDto.getPingyin())) {
            criteria.andLike("pingyin", cityDto.getPingyin());
        }
        if (StringUtils.isNotBlank(cityDto.getLevel())) {
            criteria.andLike("level", cityDto.getLevel());
        }
        if (Objects.nonNull(cityDto.getSort())) {
            criteria.andEqualTo("sort", cityDto.getSort());
        }
        if (StringUtils.isNotBlank(cityDto.getRemarks())) {
            criteria.andLike("remarks", cityDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<City> list = cityService.findByCondition(condition);
        PageInfo<City> pageInfo = new PageInfo<>(list);

        Page<CityDto> cityDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, cityDtoPageInfo);
        List<CityDto> dtoList = list.stream().map(ele -> new CityDto().convertFrom(ele)).collect(Collectors.toList());
        cityDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(cityDtoPageInfo);

    }
}
