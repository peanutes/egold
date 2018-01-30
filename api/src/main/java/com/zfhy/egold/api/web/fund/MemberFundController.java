package com.zfhy.egold.api.web.fund;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.fund.dto.MemberFundDto;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



@RequestMapping("/fund/member/fund")

@Slf4j
public class MemberFundController {
    @Resource
    private MemberFundService memberFundService;

    
    @PostMapping("/add")
    public Result<String> add(MemberFundDto memberFundDto, SysParameter sysParameter) {
        MemberFund memberFund = memberFundDto.convertTo();
        memberFund.setCreateDate(new Date());
        memberFundService.save(memberFund);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "会员资金id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        memberFundService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "会员资金id", required = true) @NotBlank @RequestParam Integer id,
                                MemberFundDto memberFundDto, SysParameter sysParameter) {
        MemberFund memberFund = this.memberFundService.findById(id);
        if (Objects.isNull(memberFund) || memberFund.getId() <= 0) {
            throw new BusException("会员资金不存在");
        }
        if (Objects.nonNull(memberFundDto.getMemberId())) {
            memberFund.setMemberId(memberFundDto.getMemberId());
        }
        if (Objects.nonNull(memberFundDto.getCashBalance())) {
            memberFund.setCashBalance(memberFundDto.getCashBalance());
        }
        if (Objects.nonNull(memberFundDto.getRemarks())) {
            memberFund.setRemarks(memberFundDto.getRemarks());
        }

        memberFundService.update(memberFund);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<MemberFundDto> detail(@ApiParam(name = "id", value = "会员资金id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        MemberFund memberFund = memberFundService.findById(id);
        return ResultGenerator.genSuccessResult(new MemberFundDto().convertFrom(memberFund));
    }

    
    @PostMapping("/list")
    public Result<Page<MemberFundDto>> list(MemberFundDto memberFundDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(MemberFund.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(memberFundDto.getMemberId())) {
            criteria.andEqualTo("memberId", memberFundDto.getMemberId());
        }
        if (Objects.nonNull(memberFundDto.getCashBalance())) {
            criteria.andEqualTo("cashBalance", memberFundDto.getCashBalance());
        }
        if (StringUtils.isNotBlank(memberFundDto.getRemarks())) {
            criteria.andLike("remarks", memberFundDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<MemberFund> list = memberFundService.findByCondition(condition);
        PageInfo<MemberFund> pageInfo = new PageInfo<>(list);

        Page<MemberFundDto> memberFundDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, memberFundDtoPageInfo);
        List<MemberFundDto> dtoList = list.stream().map(ele -> new MemberFundDto().convertFrom(ele)).collect(Collectors.toList());
        memberFundDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(memberFundDtoPageInfo);

    }
}
