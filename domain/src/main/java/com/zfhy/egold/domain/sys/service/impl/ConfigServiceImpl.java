package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.domain.sys.dao.ConfigMapper;
import com.zfhy.egold.domain.sys.entity.Config;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.sys.service.ConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class ConfigServiceImpl extends AbstractService<Config> implements ConfigService{
    @Autowired
    private ConfigMapper configMapper;

    @Cacheable("ConfigServiceImpl_findByType")
    @CacheDuration(duration = 600)
    @Override
    public Config findByType(String type) {
        Config config = new Config();
        config.setType(type);

        return this.configMapper.selectOne(config);

    }
}
