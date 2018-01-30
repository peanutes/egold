package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.sys.entity.Role;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface RoleService  extends Service<Role> {

    void saveRole(Role role);

    void updateRole(Role role);

    void deleteRoleById(Integer id);

    List<Role> findRolesByAdminId(Integer id);
}
