package com.zfhy.egold.api.web.invest;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.invest.entity.IncomeDetail;
import com.zfhy.egold.domain.invest.dto.IncomeDetailDto;
import com.zfhy.egold.domain.invest.service.IncomeDetailService;
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



@RequestMapping("/invest/income/detail")

@Slf4j
public class IncomeDetailController {
    @Resource
    private IncomeDetailService incomeDetailService;

    
    @PostMapping("/add")
    public Result<String> add(IncomeDetailDto incomeDetailDto, SysParameter sysParameter) {
        IncomeDetail incomeDetail = incomeDetailDto.convertTo();
        incomeDetail.setCreateDate(new Date());
        incomeDetailService.save(incomeDetail);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "收益明细id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        incomeDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "收益明细id", required = true) @NotBlank @RequestParam Integer id,
                                IncomeDetailDto incomeDetailDto, SysParameter sysParameter) {
        IncomeDetail incomeDetail = this.incomeDetailService.findById(id);
        if (Objects.isNull(incomeDetail) || incomeDetail.getId() <= 0) {
            throw new BusException("收益明细不存在");
        }
        if (Objects.nonNull(incomeDetailDto.getInvestId())) {
            incomeDetail.setInvestId(incomeDetailDto.getInvestId());
        }
        if (Objects.nonNull(incomeDetailDto.getIncome())) {
            incomeDetail.setIncome(incomeDetailDto.getIncome());
        }
        if (Objects.nonNull(incomeDetailDto.getRemarks())) {
            incomeDetail.setRemarks(incomeDetailDto.getRemarks());
        }

        incomeDetailService.update(incomeDetail);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<IncomeDetailDto> detail(@ApiParam(name = "id", value = "收益明细id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        IncomeDetail incomeDetail = incomeDetailService.findById(id);
        return ResultGenerator.genSuccessResult(new IncomeDetailDto().convertFrom(incomeDetail));
    }

    
    @PostMapping("/list")
    public Result<Page<IncomeDetailDto>> list(IncomeDetailDto incomeDetailDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(IncomeDetail.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(incomeDetailDto.getInvestId())) {
            criteria.andEqualTo("investId", incomeDetailDto.getInvestId());
        }
        if (Objects.nonNull(incomeDetailDto.getIncome())) {
            criteria.andEqualTo("income", incomeDetailDto.getIncome());
        }
        if (StringUtils.isNotBlank(incomeDetailDto.getRemarks())) {
            criteria.andLike("remarks", incomeDetailDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<IncomeDetail> list = incomeDetailService.findByCondition(condition);
        PageInfo<IncomeDetail> pageInfo = new PageInfo<>(list);

        Page<IncomeDetailDto> incomeDetailDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, incomeDetailDtoPageInfo);
        List<IncomeDetailDto> dtoList = list.stream().map(ele -> new IncomeDetailDto().convertFrom(ele)).collect(Collectors.toList());
        incomeDetailDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(incomeDetailDtoPageInfo);

    }
}
