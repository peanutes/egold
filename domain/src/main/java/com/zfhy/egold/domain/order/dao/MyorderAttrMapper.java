package com.zfhy.egold.domain.order.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.order.entity.MyorderAttr;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyorderAttrMapper extends Mapper<MyorderAttr> {

    void batchInsert(@Param("myorderId") Integer myorderId, @Param("myorderAttrs") List<MyorderAttr> myorderAttrs);
}
