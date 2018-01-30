package com.zfhy.egold.wap.web.sys;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.entity.AppVersion;
import com.zfhy.egold.domain.sys.dto.AppVersionDto;
import com.zfhy.egold.domain.sys.service.AppVersionService;
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



@RequestMapping("/sys/app/version")

@Slf4j
public class AppVersionController {
    @Resource
    private AppVersionService appVersionService;

    
    @PostMapping("/add")
    public Result<String> add(AppVersionDto appVersionDto, SysParameter sysParameter) {
        AppVersion appVersion = appVersionDto.convertTo();
        appVersionService.save(appVersion);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "app应用版本号id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        appVersionService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "app应用版本号id", required = true) @NotBlank @RequestParam Integer id,
                                AppVersionDto appVersionDto, SysParameter sysParameter) {
        AppVersion appVersion = this.appVersionService.findById(id);
        if (Objects.isNull(appVersion) || appVersion.getId() <= 0) {
            throw new BusException("app应用版本号不存在");
        }
        if (Objects.nonNull(appVersionDto.getAppName())) {
            appVersion.setAppName(appVersionDto.getAppName());
        }
        if (Objects.nonNull(appVersionDto.getVersionCode())) {
            appVersion.setVersionCode(appVersionDto.getVersionCode());
        }
        if (Objects.nonNull(appVersionDto.getVersionName())) {
            appVersion.setVersionName(appVersionDto.getVersionName());
        }
        if (Objects.nonNull(appVersionDto.getApkUrl())) {
            appVersion.setApkUrl(appVersionDto.getApkUrl());
        }
        if (Objects.nonNull(appVersionDto.getChangeLog())) {
            appVersion.setChangeLog(appVersionDto.getChangeLog());
        }
        if (Objects.nonNull(appVersionDto.getAppType())) {
            appVersion.setAppType(appVersionDto.getAppType());
        }
        if (Objects.nonNull(appVersionDto.getUpdateTips())) {
            appVersion.setUpdateTips(appVersionDto.getUpdateTips());
        }
        if (Objects.nonNull(appVersionDto.getForceUpdate())) {
            appVersion.setForceUpdate(appVersionDto.getForceUpdate());
        }

        appVersionService.update(appVersion);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<AppVersionDto> detail(@ApiParam(name = "id", value = "app应用版本号id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        AppVersion appVersion = appVersionService.findById(id);
        return ResultGenerator.genSuccessResult(new AppVersionDto().convertFrom(appVersion));
    }

    
    @PostMapping("/list")
    public Result<Page<AppVersionDto>> list(AppVersionDto appVersionDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(AppVersion.class);
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotBlank(appVersionDto.getAppName())) {
            criteria.andLike("appName", appVersionDto.getAppName());
        }
        if (StringUtils.isNotBlank(appVersionDto.getVersionCode())) {
            criteria.andLike("versionCode", appVersionDto.getVersionCode());
        }
        if (StringUtils.isNotBlank(appVersionDto.getVersionName())) {
            criteria.andLike("versionName", appVersionDto.getVersionName());
        }
        if (StringUtils.isNotBlank(appVersionDto.getApkUrl())) {
            criteria.andLike("apkUrl", appVersionDto.getApkUrl());
        }
        if (StringUtils.isNotBlank(appVersionDto.getChangeLog())) {
            criteria.andLike("changeLog", appVersionDto.getChangeLog());
        }
        if (StringUtils.isNotBlank(appVersionDto.getAppType())) {
            criteria.andLike("appType", appVersionDto.getAppType());
        }
        if (StringUtils.isNotBlank(appVersionDto.getUpdateTips())) {
            criteria.andLike("updateTips", appVersionDto.getUpdateTips());
        }
        if (StringUtils.isNotBlank(appVersionDto.getForceUpdate())) {
            criteria.andLike("forceUpdate", appVersionDto.getForceUpdate());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<AppVersion> list = appVersionService.findByCondition(condition);
        PageInfo<AppVersion> pageInfo = new PageInfo<>(list);

        Page<AppVersionDto> appVersionDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, appVersionDtoPageInfo);
        List<AppVersionDto> dtoList = list.stream().map(ele -> new AppVersionDto().convertFrom(ele)).collect(Collectors.toList());
        appVersionDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(appVersionDtoPageInfo);

    }
}
