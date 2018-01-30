package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.domain.sys.dao.AdminRoleMapper;
import com.zfhy.egold.domain.sys.dao.MenuMapper;
import com.zfhy.egold.domain.sys.dao.RoleMapper;
import com.zfhy.egold.domain.sys.dao.RoleMenuMapper;
import com.zfhy.egold.domain.sys.entity.AdminRole;
import com.zfhy.egold.domain.sys.entity.Role;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.sys.entity.RoleMenu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.sys.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;


    @Override

    public void saveRole(Role role) {
        this.roleMapper.insert(role);
        updateRoleMenu(role);

    }


    @Override
    public void updateRole(Role role) {
        this.roleMapper.updateByPrimaryKey(role);
        updateRoleMenu(role);

    }

    @Override
    public void deleteRoleById(Integer id) {
        Condition condition = new Condition(RoleMenu.class);
        condition.createCriteria().andEqualTo("roleId", id);
        this.roleMenuMapper.deleteByCondition(condition);

        super.deleteById(id);
    }

    @Override
    public List<Role> findRolesByAdminId(Integer adminId) {

        List<Integer> roleIds = this.adminRoleMapper.findRoleIdByAdminId(adminId);

        List<Role> roles = this.roleMapper.selectAll();

        roles.stream().filter(role -> roleIds.contains(role.getId())).forEach(role -> role.setRoleSign("true"));

        return roles;

    }


    private void updateRoleMenu(Role role) {
        List<Integer> menuIds = role.getMenuIds();
        this.menuMapper.removeByRoleId(role.getId());

        List<RoleMenu> roleMenus = menuIds.stream().map(mid -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(mid);
            return roleMenu;
        }).collect(Collectors.toList());

        this.roleMenuMapper.insertBatch(roleMenus);
    }
}
