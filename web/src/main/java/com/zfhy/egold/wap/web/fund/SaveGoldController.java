package com.zfhy.egold.wap.web.fund;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.fund.entity.SaveGold;
import com.zfhy.egold.domain.fund.dto.SaveGoldDto;
import com.zfhy.egold.domain.fund.service.SaveGoldService;
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



@RequestMapping("/fund/save/gold")

@Slf4j
public class SaveGoldController {
    @Resource
    private SaveGoldService saveGoldService;

    
    @PostMapping("/add")
    public Result<String> add(SaveGoldDto saveGoldDto, SysParameter sysParameter) {
        SaveGold saveGold = saveGoldDto.convertTo();
        saveGold.setCreateDate(new Date());
        saveGoldService.save(saveGold);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "存金记录id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        saveGoldService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "存金记录id", required = true) @NotBlank @RequestParam Integer id,
                                SaveGoldDto saveGoldDto, SysParameter sysParameter) {
        SaveGold saveGold = this.saveGoldService.findById(id);
        if (Objects.isNull(saveGold) || saveGold.getId() <= 0) {
            throw new BusException("存金记录不存在");
        }
        if (Objects.nonNull(saveGoldDto.getId())) {
            saveGold.setId(saveGoldDto.getId());
        }
        if (Objects.nonNull(saveGoldDto.getMemberId())) {
            saveGold.setMemberId(saveGoldDto.getMemberId());
        }
        if (Objects.nonNull(saveGoldDto.getGoldWeight())) {
            saveGold.setGoldWeight(saveGoldDto.getGoldWeight());
        }
        if (Objects.nonNull(saveGoldDto.getStoreId())) {
            saveGold.setStoreId(saveGoldDto.getStoreId());
        }
        if (Objects.nonNull(saveGoldDto.getStoreName())) {
            saveGold.setStoreName(saveGoldDto.getStoreName());
        }
        if (Objects.nonNull(saveGoldDto.getAdminId())) {
            saveGold.setAdminId(saveGoldDto.getAdminId());
        }
        if (Objects.nonNull(saveGoldDto.getOperator())) {
            saveGold.setOperator(saveGoldDto.getOperator());
        }
        if (Objects.nonNull(saveGoldDto.getStatus())) {
            saveGold.setStatus(saveGoldDto.getStatus());
        }
        if (Objects.nonNull(saveGoldDto.getRemarks())) {
            saveGold.setRemarks(saveGoldDto.getRemarks());
        }

        saveGoldService.update(saveGold);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<SaveGoldDto> detail(@ApiParam(name = "id", value = "存金记录id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        SaveGold saveGold = saveGoldService.findById(id);
        return ResultGenerator.genSuccessResult(new SaveGoldDto().convertFrom(saveGold));
    }

    
    @PostMapping("/list")
    public Result<Page<SaveGoldDto>> list(SaveGoldDto saveGoldDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(SaveGold.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(saveGoldDto.getId())) {
            criteria.andEqualTo("id", saveGoldDto.getId());
        }
        if (Objects.nonNull(saveGoldDto.getMemberId())) {
            criteria.andEqualTo("memberId", saveGoldDto.getMemberId());
        }
        if (Objects.nonNull(saveGoldDto.getGoldWeight())) {
            criteria.andEqualTo("goldWeight", saveGoldDto.getGoldWeight());
        }
        if (Objects.nonNull(saveGoldDto.getStoreId())) {
            criteria.andEqualTo("storeId", saveGoldDto.getStoreId());
        }
        if (StringUtils.isNotBlank(saveGoldDto.getStoreName())) {
            criteria.andLike("storeName", saveGoldDto.getStoreName());
        }
        if (Objects.nonNull(saveGoldDto.getAdminId())) {
            criteria.andEqualTo("adminId", saveGoldDto.getAdminId());
        }
        if (StringUtils.isNotBlank(saveGoldDto.getOperator())) {
            criteria.andLike("operator", saveGoldDto.getOperator());
        }
        if (StringUtils.isNotBlank(saveGoldDto.getStatus())) {
            criteria.andLike("status", saveGoldDto.getStatus());
        }
        if (StringUtils.isNotBlank(saveGoldDto.getRemarks())) {
            criteria.andLike("remarks", saveGoldDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<SaveGold> list = saveGoldService.findByCondition(condition);
        PageInfo<SaveGold> pageInfo = new PageInfo<>(list);

        Page<SaveGoldDto> saveGoldDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, saveGoldDtoPageInfo);
        List<SaveGoldDto> dtoList = list.stream().map(ele -> new SaveGoldDto().convertFrom(ele)).collect(Collectors.toList());
        saveGoldDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(saveGoldDtoPageInfo);

    }
}
