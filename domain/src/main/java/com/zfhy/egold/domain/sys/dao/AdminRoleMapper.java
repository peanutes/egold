package com.zfhy.egold.domain.sys.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.sys.entity.AdminRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminRoleMapper extends Mapper<AdminRole> {
    @Select("select ar.role_id from sys_admin_role ar where ar.admin_id = #{adminId}")
    List<Integer> findRoleIdByAdminId(Integer adminId);

    void batchInsert(List<AdminRole> adminRoles);

    void batchDeleteByAdminIds(List<Integer> ids);
}
