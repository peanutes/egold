package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.domain.sys.dao.AdminMapper;
import com.zfhy.egold.domain.sys.dao.AdminRoleMapper;
import com.zfhy.egold.domain.sys.entity.Admin;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.sys.entity.AdminRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.sys.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



@Service
@Transactional
public class AdminServiceImpl extends AbstractService<Admin> implements AdminService{
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;


    @Override
    public void saveAdminAndAdminRole(Admin admin) {
        this.adminMapper.insert(admin);


        updateAndSaveAdminRole(admin);

    }

    private void updateAndSaveAdminRole(Admin admin) {
        Condition condition = new Condition(AdminRole.class);
        condition.createCriteria().andEqualTo("adminId", admin.getId());
        this.adminRoleMapper.deleteByCondition(condition);

        List<AdminRole> adminRoles = admin.getRoleIds().stream().map(roleId -> {
            AdminRole adminRole = new AdminRole();
            adminRole.setRoleId(roleId);
            adminRole.setAdminId(admin.getId());
            return adminRole;
        }).collect(Collectors.toList());


        this.adminRoleMapper.batchInsert(adminRoles);
    }

    @Override
    public void saveAdminWithRole(Admin admin) {
        this.adminMapper.updateByPrimaryKey(admin);

        updateAndSaveAdminRole(admin);


    }

    @Override
    public void removeWhtiRoleByAdminId(Integer adminId) {
        Condition condition = new Condition(AdminRole.class);
        condition.createCriteria().andEqualTo("adminId", adminId);
        this.adminRoleMapper.deleteByCondition(condition);

        this.adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {

        this.adminRoleMapper.batchDeleteByAdminIds(ids);


        String idStrs = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        this.adminMapper.deleteByIds(idStrs);



    }

    @Override
    public void resetPwd(int userId, String password) {


        Admin adminEntity = this.adminMapper.selectByPrimaryKey(userId);
        adminEntity.setPassword(HashUtil.sha1(String.join("", adminEntity.getLoginAccount(), password)));

        this.adminMapper.updateByPrimaryKey(adminEntity);
    }

    @Override
    public boolean passsexist(int userId, String opassword) {
        Admin admin = this.findById(userId);

        if (Objects.isNull(admin)) {
            return false;
        }

        String password = admin.getPassword();

        String oldPwd = HashUtil.sha1(String.join("", admin.getLoginAccount(), opassword));

        return Objects.equals(password, oldPwd);
    }
}
