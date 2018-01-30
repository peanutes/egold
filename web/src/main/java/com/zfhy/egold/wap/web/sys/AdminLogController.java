package com.zfhy.egold.wap.web.sys;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.entity.AdminLog;
import com.zfhy.egold.domain.sys.dto.AdminLogDto;
import com.zfhy.egold.domain.sys.service.AdminLogService;
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



@RequestMapping("/sys/admin/log")

@Slf4j
public class AdminLogController {
    @Resource
    private AdminLogService adminLogService;

    
    @PostMapping("/add")
    public Result<String> add(AdminLogDto adminLogDto, SysParameter sysParameter) {
        AdminLog adminLog = adminLogDto.convertTo();
        adminLog.setCreateDate(new Date());
        adminLogService.save(adminLog);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "后台操作日志id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        adminLogService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "后台操作日志id", required = true) @NotBlank @RequestParam Integer id,
                                AdminLogDto adminLogDto, SysParameter sysParameter) {
        AdminLog adminLog = this.adminLogService.findById(id);
        if (Objects.isNull(adminLog) || adminLog.getId() <= 0) {
            throw new BusException("后台操作日志不存在");
        }
        if (Objects.nonNull(adminLogDto.getLoginAccount())) {
            adminLog.setLoginAccount(adminLogDto.getLoginAccount());
        }
        if (Objects.nonNull(adminLogDto.getOperateMethod())) {
            adminLog.setOperateMethod(adminLogDto.getOperateMethod());
        }
        if (Objects.nonNull(adminLogDto.getOperateInput())) {
            adminLog.setOperateInput(adminLogDto.getOperateInput());
        }
        if (Objects.nonNull(adminLogDto.getOperateOutput())) {
            adminLog.setOperateOutput(adminLogDto.getOperateOutput());
        }
        if (Objects.nonNull(adminLogDto.getRemarks())) {
            adminLog.setRemarks(adminLogDto.getRemarks());
        }

        adminLogService.update(adminLog);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<AdminLogDto> detail(@ApiParam(name = "id", value = "后台操作日志id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        AdminLog adminLog = adminLogService.findById(id);
        return ResultGenerator.genSuccessResult(new AdminLogDto().convertFrom(adminLog));
    }

    
    @PostMapping("/list")
    public Result<Page<AdminLogDto>> list(AdminLogDto adminLogDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(AdminLog.class);
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotBlank(adminLogDto.getLoginAccount())) {
            criteria.andLike("loginAccount", adminLogDto.getLoginAccount());
        }
        if (StringUtils.isNotBlank(adminLogDto.getOperateMethod())) {
            criteria.andLike("operateMethod", adminLogDto.getOperateMethod());
        }
        if (StringUtils.isNotBlank(adminLogDto.getOperateInput())) {
            criteria.andLike("operateInput", adminLogDto.getOperateInput());
        }
        if (StringUtils.isNotBlank(adminLogDto.getOperateOutput())) {
            criteria.andLike("operateOutput", adminLogDto.getOperateOutput());
        }
        if (StringUtils.isNotBlank(adminLogDto.getRemarks())) {
            criteria.andLike("remarks", adminLogDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<AdminLog> list = adminLogService.findByCondition(condition);
        PageInfo<AdminLog> pageInfo = new PageInfo<>(list);

        Page<AdminLogDto> adminLogDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, adminLogDtoPageInfo);
        List<AdminLogDto> dtoList = list.stream().map(ele -> new AdminLogDto().convertFrom(ele)).collect(Collectors.toList());
        adminLogDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(adminLogDtoPageInfo);

    }
}
