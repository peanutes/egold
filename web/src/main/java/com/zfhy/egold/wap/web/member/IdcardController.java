package com.zfhy.egold.wap.web.member;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.dto.IdcardDto;
import com.zfhy.egold.domain.member.service.IdcardService;
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



@RequestMapping("/member/idcard")

@Slf4j
public class IdcardController {
    @Resource
    private IdcardService idcardService;

    
    @PostMapping("/add")
    public Result<String> add(IdcardDto idcardDto, SysParameter sysParameter) {
        Idcard idcard = idcardDto.convertTo();
        idcard.setCreateDate(new Date());
        idcardService.save(idcard);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "实名认证id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        idcardService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "实名认证id", required = true) @NotBlank @RequestParam Integer id,
                                IdcardDto idcardDto, SysParameter sysParameter) {
        Idcard idcard = this.idcardService.findById(id);
        if (Objects.isNull(idcard) || idcard.getId() <= 0) {
            throw new BusException("实名认证不存在");
        }
        if (Objects.nonNull(idcardDto.getMemberId())) {
            idcard.setMemberId(idcardDto.getMemberId());
        }
        if (Objects.nonNull(idcardDto.getRealName())) {
            idcard.setRealName(idcardDto.getRealName());
        }
        if (Objects.nonNull(idcardDto.getIdCard())) {
            idcard.setIdCard(idcardDto.getIdCard());
        }
        if (Objects.nonNull(idcardDto.getTel())) {
            idcard.setTel(idcardDto.getTel());
        }
        if (Objects.nonNull(idcardDto.getRemarks())) {
            idcard.setRemarks(idcardDto.getRemarks());
        }

        idcardService.update(idcard);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<IdcardDto> detail(@ApiParam(name = "id", value = "实名认证id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        Idcard idcard = idcardService.findById(id);
        return ResultGenerator.genSuccessResult(new IdcardDto().convertFrom(idcard));
    }

    
    @PostMapping("/list")
    public Result<Page<IdcardDto>> list(IdcardDto idcardDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(Idcard.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(idcardDto.getMemberId())) {
            criteria.andEqualTo("memberId", idcardDto.getMemberId());
        }
        if (StringUtils.isNotBlank(idcardDto.getRealName())) {
            criteria.andLike("realName", idcardDto.getRealName());
        }
        if (StringUtils.isNotBlank(idcardDto.getIdCard())) {
            criteria.andLike("idCard", idcardDto.getIdCard());
        }
        if (StringUtils.isNotBlank(idcardDto.getTel())) {
            criteria.andLike("tel", idcardDto.getTel());
        }
        if (StringUtils.isNotBlank(idcardDto.getRemarks())) {
            criteria.andLike("remarks", idcardDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<Idcard> list = idcardService.findByCondition(condition);
        PageInfo<Idcard> pageInfo = new PageInfo<>(list);

        Page<IdcardDto> idcardDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, idcardDtoPageInfo);
        List<IdcardDto> dtoList = list.stream().map(ele -> new IdcardDto().convertFrom(ele)).collect(Collectors.toList());
        idcardDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(idcardDtoPageInfo);

    }
}
