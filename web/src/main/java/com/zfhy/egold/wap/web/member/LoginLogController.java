package com.zfhy.egold.wap.web.member;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.member.entity.LoginLog;
import com.zfhy.egold.domain.member.dto.LoginLogDto;
import com.zfhy.egold.domain.member.service.LoginLogService;
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



@RequestMapping("/member/login/log")

@Slf4j
public class LoginLogController {
    @Resource
    private LoginLogService loginLogService;

    
    @PostMapping("/add")
    public Result<String> add(LoginLogDto loginLogDto, SysParameter sysParameter) {
        LoginLog loginLog = loginLogDto.convertTo();
        loginLog.setCreateDate(new Date());
        loginLogService.save(loginLog);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "会员登陆日志id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        loginLogService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "会员登陆日志id", required = true) @NotBlank @RequestParam Integer id,
                                LoginLogDto loginLogDto, SysParameter sysParameter) {
        LoginLog loginLog = this.loginLogService.findById(id);
        if (Objects.isNull(loginLog) || loginLog.getId() <= 0) {
            throw new BusException("会员登陆日志不存在");
        }
        if (Objects.nonNull(loginLogDto.getMemberId())) {
            loginLog.setMemberId(loginLogDto.getMemberId());
        }
        if (Objects.nonNull(loginLogDto.getTerminalType())) {
            loginLog.setTerminalType(loginLogDto.getTerminalType());
        }
        if (Objects.nonNull(loginLogDto.getTerminalId())) {
            loginLog.setTerminalId(loginLogDto.getTerminalId());
        }
        if (Objects.nonNull(loginLogDto.getLoginIp())) {
            loginLog.setLoginIp(loginLogDto.getLoginIp());
        }
        if (Objects.nonNull(loginLogDto.getRemarks())) {
            loginLog.setRemarks(loginLogDto.getRemarks());
        }

        loginLogService.update(loginLog);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<LoginLogDto> detail(@ApiParam(name = "id", value = "会员登陆日志id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        LoginLog loginLog = loginLogService.findById(id);
        return ResultGenerator.genSuccessResult(new LoginLogDto().convertFrom(loginLog));
    }

    
    @PostMapping("/list")
    public Result<Page<LoginLogDto>> list(LoginLogDto loginLogDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(LoginLog.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(loginLogDto.getMemberId())) {
            criteria.andEqualTo("memberId", loginLogDto.getMemberId());
        }
        if (StringUtils.isNotBlank(loginLogDto.getTerminalType())) {
            criteria.andLike("terminalType", loginLogDto.getTerminalType());
        }
        if (StringUtils.isNotBlank(loginLogDto.getTerminalId())) {
            criteria.andLike("terminalId", loginLogDto.getTerminalId());
        }
        if (StringUtils.isNotBlank(loginLogDto.getLoginIp())) {
            criteria.andLike("loginIp", loginLogDto.getLoginIp());
        }
        if (StringUtils.isNotBlank(loginLogDto.getRemarks())) {
            criteria.andLike("remarks", loginLogDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<LoginLog> list = loginLogService.findByCondition(condition);
        PageInfo<LoginLog> pageInfo = new PageInfo<>(list);

        Page<LoginLogDto> loginLogDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, loginLogDtoPageInfo);
        List<LoginLogDto> dtoList = list.stream().map(ele -> new LoginLogDto().convertFrom(ele)).collect(Collectors.toList());
        loginLogDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(loginLogDtoPageInfo);

    }
}
