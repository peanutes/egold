package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.sys.entity.Admin;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface AdminService  extends Service<Admin> {

    void saveAdminAndAdminRole(Admin admin);

    void saveAdminWithRole(Admin admin);

    void removeWhtiRoleByAdminId(Integer id);

    void deleteByIds(List<Integer> ids);

    void resetPwd(int userId, String password);

    boolean passsexist(int userId, String opassword);

}
