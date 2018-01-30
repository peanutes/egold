package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.core.dto.AppType;
import com.zfhy.egold.domain.sys.dao.AppVersionMapper;
import com.zfhy.egold.domain.sys.entity.AppVersion;
import com.zfhy.egold.domain.sys.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class AppVersionServiceImpl extends AbstractService<AppVersion> implements AppVersionService{
    @Autowired
    private AppVersionMapper appVersionMapper;

    @Cacheable("AppVersionServiceImpl_findLatelyVersion")
    @CacheDuration(duration = 60L)
    @Override
    public AppVersion findLatelyVersion(AppType appType) {
        return this.appVersionMapper.findLatelyVersion(appType.name());

    }
}
