package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.entity.Dict;
import com.zfhy.egold.common.core.Service;

import java.util.Map;



public interface DictService  extends Service<Dict> {


    Dict findByType(DictType type);

    Integer findIntByType(DictType type);

    Long findLongByType(DictType type);

    String findStringByType(DictType type);


    Map<String,String> findDictMap();

    void removeDictMap();

    Double findDouble(DictType dictType);

}
