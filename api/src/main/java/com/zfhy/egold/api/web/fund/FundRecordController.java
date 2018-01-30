package com.zfhy.egold.api.web.fund;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.fund.dto.FundRecordDto;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import com.zfhy.egold.domain.fund.service.FundRecordService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;





@Slf4j
@Validated
public class FundRecordController {
    @Resource
    private FundRecordService fundRecordService;

    
    @PostMapping("/add")
    public Result<String> add(FundRecordDto fundRecordDto, @ModelAttribute @Valid SysParameter sysParameter) {
        FundRecord fundRecord = fundRecordDto.convertTo();
        fundRecord.setCreateDate(new Date());
        fundRecordService.save(fundRecord);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "资金明细id", required = true) @NotBlank @RequestParam Integer id, @ModelAttribute @Valid SysParameter sysParameter) {
        fundRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "资金明细id", required = true) @NotBlank @RequestParam Integer id,
                                FundRecordDto fundRecordDto, @ModelAttribute @Valid SysParameter sysParameter) {
        FundRecord fundRecord = this.fundRecordService.findById(id);
        if (Objects.isNull(fundRecord) || fundRecord.getId() <= 0) {
            throw new BusException("资金明细不存在");
        }
        if (Objects.nonNull(fundRecordDto.getId())) {
            fundRecord.setId(fundRecordDto.getId());
        }
        if (Objects.nonNull(fundRecordDto.getMemberId())) {
            fundRecord.setMemberId(fundRecordDto.getMemberId());
        }
        if (Objects.nonNull(fundRecordDto.getOperateMoney())) {
            fundRecord.setOperateMoney(fundRecordDto.getOperateMoney());
        }
        if (Objects.nonNull(fundRecordDto.getGoldWeight())) {
            fundRecord.setGoldWeight(fundRecordDto.getGoldWeight());
        }
        if (Objects.nonNull(fundRecordDto.getProductName())) {
            fundRecord.setProductName(fundRecordDto.getProductName());
        }
        if (Objects.nonNull(fundRecordDto.getOperateId())) {
            fundRecord.setOperateId(fundRecordDto.getOperateId());
        }
        if (Objects.nonNull(fundRecordDto.getDisplayLabel())) {
            fundRecord.setDisplayLabel(fundRecordDto.getDisplayLabel());
        }
        if (Objects.nonNull(fundRecordDto.getOperateType())) {
            fundRecord.setOperateType(fundRecordDto.getOperateType());
        }
        if (Objects.nonNull(fundRecordDto.getRemarks())) {
            fundRecord.setRemarks(fundRecordDto.getRemarks());
        }

        fundRecordService.update(fundRecord);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<FundRecordDto> detail(@ApiParam(name = "id", value = "资金明细id", required = true) @NotBlank @RequestParam Integer id, @ModelAttribute @Valid SysParameter sysParameter) {
        FundRecord fundRecord = fundRecordService.findById(id);
        return ResultGenerator.genSuccessResult(new FundRecordDto().convertFrom(fundRecord));
    }

    
    @PostMapping("/list")
    public Result<Page<FundRecordDto>> list(FundRecordDto fundRecordDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, @ModelAttribute @Valid SysParameter sysParameter) {
        Condition condition = new Condition(FundRecord.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(fundRecordDto.getId())) {
            criteria.andEqualTo("id", fundRecordDto.getId());
        }
        if (Objects.nonNull(fundRecordDto.getMemberId())) {
            criteria.andEqualTo("memberId", fundRecordDto.getMemberId());
        }
        if (Objects.nonNull(fundRecordDto.getOperateMoney())) {
            criteria.andEqualTo("operateMoney", fundRecordDto.getOperateMoney());
        }
        if (Objects.nonNull(fundRecordDto.getGoldWeight())) {
            criteria.andEqualTo("goldWeight", fundRecordDto.getGoldWeight());
        }
        if (StringUtils.isNotBlank(fundRecordDto.getProductName())) {
            criteria.andLike("productName", fundRecordDto.getProductName());
        }
        if (Objects.nonNull(fundRecordDto.getOperateId())) {
            criteria.andEqualTo("operateId", fundRecordDto.getOperateId());
        }
        if (StringUtils.isNotBlank(fundRecordDto.getDisplayLabel())) {
            criteria.andLike("displayLabel", fundRecordDto.getDisplayLabel());
        }
        if (StringUtils.isNotBlank(fundRecordDto.getOperateType())) {
            criteria.andLike("operateType", fundRecordDto.getOperateType());
        }
        if (StringUtils.isNotBlank(fundRecordDto.getRemarks())) {
            criteria.andLike("remarks", fundRecordDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<FundRecord> list = fundRecordService.findByCondition(condition);
        PageInfo<FundRecord> pageInfo = new PageInfo<>(list);

        Page<FundRecordDto> fundRecordDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, fundRecordDtoPageInfo);
        List<FundRecordDto> dtoList = list.stream().map(ele -> new FundRecordDto().convertFrom(ele)).collect(Collectors.toList());
        fundRecordDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(fundRecordDtoPageInfo);

    }
}
