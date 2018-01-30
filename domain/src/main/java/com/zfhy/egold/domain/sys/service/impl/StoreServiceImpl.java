package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.domain.sys.dao.StoreMapper;
import com.zfhy.egold.domain.sys.entity.Store;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.sys.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class StoreServiceImpl extends AbstractService<Store> implements StoreService{
    @Autowired
    private StoreMapper storeMapper;

}
