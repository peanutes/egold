package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.sys.dao.DictMapper;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.entity.Dict;
import com.zfhy.egold.domain.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class DictServiceImpl extends AbstractService<Dict> implements DictService{
    @Autowired
    private DictMapper dictMapper;

    @Transactional(readOnly = true)
    @Cacheable("DictService_findByType")
    @CacheDuration(duration = 300L)
    @Override
    public Dict findByType(DictType type) {
        Dict dict = super.findBy("type", type.name());
        if (Objects.isNull(dict)) {
            log.error("通过type获取数据字典错误");
            throw new BusException("数据字典不存在");
        }
        return dict;
    }


    public Integer findIntByType(DictType type) {
        Dict dict = this.findByType(type);

        return new Integer(dict.getValue());
    }

    @Override
    public Long findLongByType(DictType type) {
        Dict dict = this.findByType(type);

        return new Long(dict.getValue());

    }


    public String findStringByType(DictType type) {
        Dict dict = this.findByType(type);

        return dict.getValue();
    }

    @Transactional(readOnly = true)
    @CacheDuration(duration = 99999999L)
    @Cacheable("DictServiceImpl_findDictMap")
    @Override
    public Map<String, String> findDictMap() {

        List<Dict> dictList = this.findAll();

        return dictList.stream().collect(Collectors.toMap(Dict::getType, Dict::getValue));
    }

    
    @CacheEvict(value ="DictServiceImpl_findDictMap", allEntries=true)
    public void removeDictMap() {
    }

    @Override
    public Double findDouble(DictType dictType) {
        Dict dict = this.findByType(dictType);

        return new Double(dict.getValue());
    }


}
