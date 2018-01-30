package com.zfhy.egold.domain.cms.service.impl;

import com.zfhy.egold.domain.cms.dao.BannerMapper;
import com.zfhy.egold.domain.cms.entity.Banner;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.cms.service.BannerService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class BannerServiceImpl extends AbstractService<Banner> implements BannerService{
    @Autowired
    private BannerMapper bannerMapper;

}
