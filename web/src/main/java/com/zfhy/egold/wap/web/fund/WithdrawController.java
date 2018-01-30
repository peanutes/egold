package com.zfhy.egold.wap.web.fund;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.fund.dto.WithdrawDto;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.fund.service.WithdrawService;
import io.swagger.annotations.ApiOperation;
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



@RequestMapping("/fund/withdraw")

@Slf4j
public class WithdrawController {
    @Resource
    private WithdrawService withdrawService;

    
    @PostMapping("/add")
    public Result<String> add(WithdrawDto withdrawDto, SysParameter sysParameter) {
        Withdraw withdraw = withdrawDto.convertTo();
        withdraw.setCreateDate(new Date());
        withdrawService.save(withdraw);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "提现记录id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        withdrawService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "提现记录id", required = true) @NotBlank @RequestParam Integer id,
                                WithdrawDto withdrawDto, SysParameter sysParameter) {
        Withdraw withdraw = this.withdrawService.findById(id);
        if (Objects.isNull(withdraw) || withdraw.getId() <= 0) {
            throw new BusException("提现记录不存在");
        }
        if (Objects.nonNull(withdrawDto.getId())) {
            withdraw.setId(withdrawDto.getId());
        }
        if (Objects.nonNull(withdrawDto.getWithdrawAmount())) {
            withdraw.setWithdrawAmount(withdrawDto.getWithdrawAmount());
        }
        if (Objects.nonNull(withdrawDto.getWithdrawAccount())) {
            withdraw.setWithdrawAccount(withdrawDto.getWithdrawAccount());
        }
        if (Objects.nonNull(withdrawDto.getWithdrawFee())) {
            withdraw.setWithdrawFee(withdrawDto.getWithdrawFee());
        }
        if (Objects.nonNull(withdrawDto.getStatus())) {
            withdraw.setStatus(withdrawDto.getStatus());
        }

        withdrawService.update(withdraw);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<WithdrawDto> detail(@ApiParam(name = "id", value = "提现记录id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        Withdraw withdraw = withdrawService.findById(id);
        return ResultGenerator.genSuccessResult(new WithdrawDto().convertFrom(withdraw));
    }

    
    @PostMapping("/list")
    public Result<Page<WithdrawDto>> list(WithdrawDto withdrawDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(Withdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(withdrawDto.getId())) {
            criteria.andEqualTo("id", withdrawDto.getId());
        }
        if (Objects.nonNull(withdrawDto.getWithdrawAmount())) {
            criteria.andEqualTo("withdrawAmount", withdrawDto.getWithdrawAmount());
        }
        if (StringUtils.isNotBlank(withdrawDto.getWithdrawAccount())) {
            criteria.andLike("withdrawAccount", withdrawDto.getWithdrawAccount());
        }
        if (Objects.nonNull(withdrawDto.getWithdrawFee())) {
            criteria.andEqualTo("withdrawFee", withdrawDto.getWithdrawFee());
        }
        if (StringUtils.isNotBlank(withdrawDto.getStatus())) {
            criteria.andLike("status", withdrawDto.getStatus());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<Withdraw> list = withdrawService.findByCondition(condition);
        PageInfo<Withdraw> pageInfo = new PageInfo<>(list);

        Page<WithdrawDto> withdrawDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, withdrawDtoPageInfo);
        List<WithdrawDto> dtoList = list.stream().map(ele -> new WithdrawDto().convertFrom(ele)).collect(Collectors.toList());
        withdrawDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(withdrawDtoPageInfo);

    }
}
