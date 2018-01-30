package com.zfhy.egold.api.web.sys;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.entity.Dict;
import com.zfhy.egold.domain.sys.dto.DictDto;
import com.zfhy.egold.domain.sys.service.DictService;
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



@RequestMapping("/sys/dict")

@Slf4j
public class DictController {
    @Resource
    private DictService dictService;

    
    @PostMapping("/add")
    public Result<String> add(DictDto dictDto, SysParameter sysParameter) {
        Dict dict = dictDto.convertTo();
        dict.setCreateDate(new Date());
        dictService.save(dict);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "数据字典id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        dictService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "数据字典id", required = true) @NotBlank @RequestParam Integer id,
                                DictDto dictDto, SysParameter sysParameter) {
        Dict dict = this.dictService.findById(id);
        if (Objects.isNull(dict) || dict.getId() <= 0) {
            throw new BusException("数据字典不存在");
        }
        if (Objects.nonNull(dictDto.getType())) {
            dict.setType(dictDto.getType());
        }
        if (Objects.nonNull(dictDto.getLabelName())) {
            dict.setLabelName(dictDto.getLabelName());
        }
        if (Objects.nonNull(dictDto.getValue())) {
            dict.setValue(dictDto.getValue());
        }
        if (Objects.nonNull(dictDto.getDes())) {
            dict.setDes(dictDto.getDes());
        }
        if (Objects.nonNull(dictDto.getSort())) {
            dict.setSort(dictDto.getSort());
        }
        if (Objects.nonNull(dictDto.getRemarks())) {
            dict.setRemarks(dictDto.getRemarks());
        }

        dictService.update(dict);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<DictDto> detail(@ApiParam(name = "id", value = "数据字典id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        Dict dict = dictService.findById(id);
        return ResultGenerator.genSuccessResult(new DictDto().convertFrom(dict));
    }

    
    @PostMapping("/list")
    public Result<Page<DictDto>> list(DictDto dictDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(Dict.class);
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotBlank(dictDto.getType())) {
            criteria.andLike("type", dictDto.getType());
        }
        if (StringUtils.isNotBlank(dictDto.getLabelName())) {
            criteria.andLike("labelName", dictDto.getLabelName());
        }
        if (StringUtils.isNotBlank(dictDto.getValue())) {
            criteria.andLike("value", dictDto.getValue());
        }
        if (StringUtils.isNotBlank(dictDto.getDes())) {
            criteria.andLike("des", dictDto.getDes());
        }
        if (Objects.nonNull(dictDto.getSort())) {
            criteria.andEqualTo("sort", dictDto.getSort());
        }
        if (StringUtils.isNotBlank(dictDto.getRemarks())) {
            criteria.andLike("remarks", dictDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<Dict> list = dictService.findByCondition(condition);
        PageInfo<Dict> pageInfo = new PageInfo<>(list);

        Page<DictDto> dictDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, dictDtoPageInfo);
        List<DictDto> dtoList = list.stream().map(ele -> new DictDto().convertFrom(ele)).collect(Collectors.toList());
        dictDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(dictDtoPageInfo);

    }
}
