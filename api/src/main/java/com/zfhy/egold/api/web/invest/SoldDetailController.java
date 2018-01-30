package com.zfhy.egold.api.web.invest;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.invest.dto.SoldDetailDto;
import com.zfhy.egold.domain.invest.service.SoldDetailService;
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



@RequestMapping("/invest/sold/detail")

@Slf4j
public class SoldDetailController {
    @Resource
    private SoldDetailService soldDetailService;

    
    @PostMapping("/add")
    public Result<String> add(SoldDetailDto soldDetailDto, SysParameter sysParameter) {
        SoldDetail soldDetail = soldDetailDto.convertTo();
        soldDetail.setCreateDate(new Date());
        soldDetailService.save(soldDetail);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "卖出记录id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        soldDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "卖出记录id", required = true) @NotBlank @RequestParam Integer id,
                                SoldDetailDto soldDetailDto, SysParameter sysParameter) {
        SoldDetail soldDetail = this.soldDetailService.findById(id);
        if (Objects.isNull(soldDetail) || soldDetail.getId() <= 0) {
            throw new BusException("卖出记录不存在");
        }
        if (Objects.nonNull(soldDetailDto.getMemberId())) {
            soldDetail.setMemberId(soldDetailDto.getMemberId());
        }
        if (Objects.nonNull(soldDetailDto.getGoldWeight())) {
            soldDetail.setGoldWeight(soldDetailDto.getGoldWeight());
        }
        if (Objects.nonNull(soldDetailDto.getProductName())) {
            soldDetail.setProductName(soldDetailDto.getProductName());
        }
        if (Objects.nonNull(soldDetailDto.getPrice())) {
            soldDetail.setPrice(soldDetailDto.getPrice());
        }
        if (Objects.nonNull(soldDetailDto.getIncomeAmount())) {
            soldDetail.setIncomeAmount(soldDetailDto.getIncomeAmount());
        }
        if (Objects.nonNull(soldDetailDto.getTotalAmount())) {
            soldDetail.setTotalAmount(soldDetailDto.getTotalAmount());
        }
        if (Objects.nonNull(soldDetailDto.getFee())) {
            soldDetail.setFee(soldDetailDto.getFee());
        }
        if (Objects.nonNull(soldDetailDto.getRemarks())) {
            soldDetail.setRemarks(soldDetailDto.getRemarks());
        }

        soldDetailService.update(soldDetail);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<SoldDetailDto> detail(@ApiParam(name = "id", value = "卖出记录id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        SoldDetail soldDetail = soldDetailService.findById(id);
        return ResultGenerator.genSuccessResult(new SoldDetailDto().convertFrom(soldDetail));
    }

    
    @PostMapping("/list")
    public Result<Page<SoldDetailDto>> list(SoldDetailDto soldDetailDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(SoldDetail.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(soldDetailDto.getMemberId())) {
            criteria.andEqualTo("memberId", soldDetailDto.getMemberId());
        }
        if (Objects.nonNull(soldDetailDto.getGoldWeight())) {
            criteria.andEqualTo("goldWeight", soldDetailDto.getGoldWeight());
        }
        if (StringUtils.isNotBlank(soldDetailDto.getProductName())) {
            criteria.andLike("productName", soldDetailDto.getProductName());
        }
        if (Objects.nonNull(soldDetailDto.getPrice())) {
            criteria.andEqualTo("price", soldDetailDto.getPrice());
        }
        if (Objects.nonNull(soldDetailDto.getIncomeAmount())) {
            criteria.andEqualTo("incomeAmount", soldDetailDto.getIncomeAmount());
        }
        if (Objects.nonNull(soldDetailDto.getTotalAmount())) {
            criteria.andEqualTo("totalAmount", soldDetailDto.getTotalAmount());
        }
        if (Objects.nonNull(soldDetailDto.getFee())) {
            criteria.andEqualTo("fee", soldDetailDto.getFee());
        }
        if (StringUtils.isNotBlank(soldDetailDto.getRemarks())) {
            criteria.andLike("remarks", soldDetailDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<SoldDetail> list = soldDetailService.findByCondition(condition);
        PageInfo<SoldDetail> pageInfo = new PageInfo<>(list);

        Page<SoldDetailDto> soldDetailDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, soldDetailDtoPageInfo);
        List<SoldDetailDto> dtoList = list.stream().map(ele -> new SoldDetailDto().convertFrom(ele)).collect(Collectors.toList());
        soldDetailDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(soldDetailDtoPageInfo);

    }
}
