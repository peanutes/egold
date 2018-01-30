package com.zfhy.egold.api.web.fund;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.fund.entity.WithdrawGold;
import com.zfhy.egold.domain.fund.dto.WithdrawGoldDto;
import com.zfhy.egold.domain.fund.service.WithdrawGoldService;
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



@RequestMapping("/fund/withdraw/gold")

@Slf4j
public class WithdrawGoldController {
    @Resource
    private WithdrawGoldService withdrawGoldService;

    
    @PostMapping("/add")
    public Result<String> add(WithdrawGoldDto withdrawGoldDto, SysParameter sysParameter) {
        WithdrawGold withdrawGold = withdrawGoldDto.convertTo();
        withdrawGold.setCreateDate(new Date());
        withdrawGoldService.save(withdrawGold);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "提金申请id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        withdrawGoldService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "提金申请id", required = true) @NotBlank @RequestParam Integer id,
                                WithdrawGoldDto withdrawGoldDto, SysParameter sysParameter) {
        WithdrawGold withdrawGold = this.withdrawGoldService.findById(id);
        if (Objects.isNull(withdrawGold) || withdrawGold.getId() <= 0) {
            throw new BusException("提金申请不存在");
        }
        if (Objects.nonNull(withdrawGoldDto.getId())) {
            withdrawGold.setId(withdrawGoldDto.getId());
        }
        if (Objects.nonNull(withdrawGoldDto.getMemberId())) {
            withdrawGold.setMemberId(withdrawGoldDto.getMemberId());
        }
        if (Objects.nonNull(withdrawGoldDto.getWithdrawType())) {
            withdrawGold.setWithdrawType(withdrawGoldDto.getWithdrawType());
        }
        if (Objects.nonNull(withdrawGoldDto.getWithdrawGoldWeight())) {
            withdrawGold.setWithdrawGoldWeight(withdrawGoldDto.getWithdrawGoldWeight());
        }
        if (Objects.nonNull(withdrawGoldDto.getDeliverNo())) {
            withdrawGold.setDeliverNo(withdrawGoldDto.getDeliverNo());
        }
        if (Objects.nonNull(withdrawGoldDto.getStoreId())) {
            withdrawGold.setStoreId(withdrawGoldDto.getStoreId());
        }
        if (Objects.nonNull(withdrawGoldDto.getStoreName())) {
            withdrawGold.setStoreName(withdrawGoldDto.getStoreName());
        }
        if (Objects.nonNull(withdrawGoldDto.getAdminId())) {
            withdrawGold.setAdminId(withdrawGoldDto.getAdminId());
        }
        if (Objects.nonNull(withdrawGoldDto.getOperator())) {
            withdrawGold.setOperator(withdrawGoldDto.getOperator());
        }
        if (Objects.nonNull(withdrawGoldDto.getStatus())) {
            withdrawGold.setStatus(withdrawGoldDto.getStatus());
        }
        if (Objects.nonNull(withdrawGoldDto.getRemarks())) {
            withdrawGold.setRemarks(withdrawGoldDto.getRemarks());
        }

        withdrawGoldService.update(withdrawGold);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<WithdrawGoldDto> detail(@ApiParam(name = "id", value = "提金申请id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        WithdrawGold withdrawGold = withdrawGoldService.findById(id);
        return ResultGenerator.genSuccessResult(new WithdrawGoldDto().convertFrom(withdrawGold));
    }

    
    @PostMapping("/list")
    public Result<Page<WithdrawGoldDto>> list(WithdrawGoldDto withdrawGoldDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(WithdrawGold.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(withdrawGoldDto.getId())) {
            criteria.andEqualTo("id", withdrawGoldDto.getId());
        }
        if (Objects.nonNull(withdrawGoldDto.getMemberId())) {
            criteria.andEqualTo("memberId", withdrawGoldDto.getMemberId());
        }
        if (StringUtils.isNotBlank(withdrawGoldDto.getWithdrawType())) {
            criteria.andLike("withdrawType", withdrawGoldDto.getWithdrawType());
        }
        if (Objects.nonNull(withdrawGoldDto.getWithdrawGoldWeight())) {
            criteria.andEqualTo("withdrawGoldWeight", withdrawGoldDto.getWithdrawGoldWeight());
        }
        if (StringUtils.isNotBlank(withdrawGoldDto.getDeliverNo())) {
            criteria.andLike("deliverNo", withdrawGoldDto.getDeliverNo());
        }
        if (Objects.nonNull(withdrawGoldDto.getStoreId())) {
            criteria.andEqualTo("storeId", withdrawGoldDto.getStoreId());
        }
        if (StringUtils.isNotBlank(withdrawGoldDto.getStoreName())) {
            criteria.andLike("storeName", withdrawGoldDto.getStoreName());
        }
        if (Objects.nonNull(withdrawGoldDto.getAdminId())) {
            criteria.andEqualTo("adminId", withdrawGoldDto.getAdminId());
        }
        if (StringUtils.isNotBlank(withdrawGoldDto.getOperator())) {
            criteria.andLike("operator", withdrawGoldDto.getOperator());
        }
        if (StringUtils.isNotBlank(withdrawGoldDto.getStatus())) {
            criteria.andLike("status", withdrawGoldDto.getStatus());
        }
        if (StringUtils.isNotBlank(withdrawGoldDto.getRemarks())) {
            criteria.andLike("remarks", withdrawGoldDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<WithdrawGold> list = withdrawGoldService.findByCondition(condition);
        PageInfo<WithdrawGold> pageInfo = new PageInfo<>(list);

        Page<WithdrawGoldDto> withdrawGoldDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, withdrawGoldDtoPageInfo);
        List<WithdrawGoldDto> dtoList = list.stream().map(ele -> new WithdrawGoldDto().convertFrom(ele)).collect(Collectors.toList());
        withdrawGoldDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(withdrawGoldDtoPageInfo);

    }
}
