package com.zfhy.egold.api.web.invest;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.dto.InvestRecordDto;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
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
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;





@Slf4j
@Validated
public class InvestRecordController {
    @Resource
    private InvestRecordService investRecordService;

    
    @PostMapping("/add")
    public Result<String> add(InvestRecordDto investRecordDto, @ModelAttribute @Valid SysParameter sysParameter) {
        InvestRecord investRecord = investRecordDto.convertTo();
        investRecord.setCreateDate(new Date());
        investRecordService.save(investRecord);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "投资记录id", required = true) @NotBlank @RequestParam Integer id, @ModelAttribute @Valid SysParameter sysParameter) {
        investRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "投资记录id", required = true) @NotBlank @RequestParam Integer id,
                                InvestRecordDto investRecordDto, @ModelAttribute @Valid SysParameter sysParameter) {
        InvestRecord investRecord = this.investRecordService.findById(id);
        if (Objects.isNull(investRecord) || investRecord.getId() <= 0) {
            throw new BusException("投资记录不存在");
        }
        if (Objects.nonNull(investRecordDto.getMemberId())) {
            investRecord.setMemberId(investRecordDto.getMemberId());
        }
        if (Objects.nonNull(investRecordDto.getProductId())) {
            investRecord.setProductId(investRecordDto.getProductId());
        }
        if (Objects.nonNull(investRecordDto.getProductName())) {
            investRecord.setProductName(investRecordDto.getProductName());
        }
        if (Objects.nonNull(investRecordDto.getInvestAmount())) {
            investRecord.setInvestAmount(investRecordDto.getInvestAmount());
        }
        if (Objects.nonNull(investRecordDto.getInvestGoldWeight())) {
            investRecord.setInvestGoldWeight(investRecordDto.getInvestGoldWeight());
        }
        if (Objects.nonNull(investRecordDto.getPrice())) {
            investRecord.setPrice(investRecordDto.getPrice());
        }
        if (Objects.nonNull(investRecordDto.getStatus())) {
            investRecord.setStatus(investRecordDto.getStatus());
        }
        if (Objects.nonNull(investRecordDto.getAnnualRate())) {
            investRecord.setAnnualRate(investRecordDto.getAnnualRate());
        }
        if (Objects.nonNull(investRecordDto.getAdditionalRate())) {
            investRecord.setAdditionalRate(investRecordDto.getAdditionalRate());
        }
        if (Objects.nonNull(investRecordDto.getRemarks())) {
            investRecord.setRemarks(investRecordDto.getRemarks());
        }

        investRecordService.update(investRecord);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<InvestRecordDto> detail(@ApiParam(name = "id", value = "投资记录id", required = true) @NotBlank @RequestParam Integer id, @ModelAttribute @Valid SysParameter sysParameter) {
        InvestRecord investRecord = investRecordService.findById(id);
        return ResultGenerator.genSuccessResult(new InvestRecordDto().convertFrom(investRecord));
    }

    
    @PostMapping("/list")
    public Result<Page<InvestRecordDto>> list(InvestRecordDto investRecordDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, @ModelAttribute @Valid SysParameter sysParameter) {
        Condition condition = new Condition(InvestRecord.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(investRecordDto.getMemberId())) {
            criteria.andEqualTo("memberId", investRecordDto.getMemberId());
        }
        if (Objects.nonNull(investRecordDto.getProductId())) {
            criteria.andEqualTo("productId", investRecordDto.getProductId());
        }
        if (StringUtils.isNotBlank(investRecordDto.getProductName())) {
            criteria.andLike("productName", investRecordDto.getProductName());
        }
        if (Objects.nonNull(investRecordDto.getInvestAmount())) {
            criteria.andEqualTo("investAmount", investRecordDto.getInvestAmount());
        }
        if (Objects.nonNull(investRecordDto.getInvestGoldWeight())) {
            criteria.andEqualTo("investGoldWeight", investRecordDto.getInvestGoldWeight());
        }
        if (Objects.nonNull(investRecordDto.getPrice())) {
            criteria.andEqualTo("price", investRecordDto.getPrice());
        }
        if (StringUtils.isNotBlank(investRecordDto.getStatus())) {
            criteria.andLike("status", investRecordDto.getStatus());
        }
        if (Objects.nonNull(investRecordDto.getAnnualRate())) {
            criteria.andEqualTo("annualRate", investRecordDto.getAnnualRate());
        }
        if (Objects.nonNull(investRecordDto.getAdditionalRate())) {
            criteria.andEqualTo("additionalRate", investRecordDto.getAdditionalRate());
        }
        if (StringUtils.isNotBlank(investRecordDto.getRemarks())) {
            criteria.andLike("remarks", investRecordDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<InvestRecord> list = investRecordService.findByCondition(condition);
        PageInfo<InvestRecord> pageInfo = new PageInfo<>(list);

        Page<InvestRecordDto> investRecordDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, investRecordDtoPageInfo);
        List<InvestRecordDto> dtoList = list.stream().map(ele -> new InvestRecordDto().convertFrom(ele)).collect(Collectors.toList());
        investRecordDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(investRecordDtoPageInfo);

    }
}
