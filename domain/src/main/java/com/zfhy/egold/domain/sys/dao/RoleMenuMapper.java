package com.zfhy.egold.domain.sys.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.sys.entity.RoleMenu;

import java.util.List;

public interface RoleMenuMapper extends Mapper<RoleMenu> {
    void insertBatch(List<RoleMenu> roleMenus);
}
