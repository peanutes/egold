package com.zfhy.egold.domain.sys.service.impl;

import com.google.common.collect.Lists;
import com.zfhy.egold.common.core.dto.Tree;
import com.zfhy.egold.common.util.BuildTree;
import com.zfhy.egold.domain.sys.dao.MenuMapper;
import com.zfhy.egold.domain.sys.entity.Menu;
import com.zfhy.egold.common.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.sys.service.MenuService;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.util.*;
import java.util.stream.Collectors;



@Service
@Transactional
public class MenuServiceImpl extends AbstractService<Menu> implements MenuService{
    @Autowired
    private MenuMapper menuMapper;


    @Override
    public List<Tree<Menu>> listMenuTree(Integer userId) {
        List<Tree<Menu>> trees = Lists.newArrayList();
        List<Menu> menus = menuMapper.listMenuByUserId(userId);
        for (Menu menu : menus) {
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getMenuName());
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("url", menu.getHref());
            attributes.put("icon", menu.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        
        List<Tree<Menu>> list = BuildTree.buildList(trees,"0");
        return list;

    }

    @Override
    public Set<String> listPermissions(Integer userId) {
        List<String> permissions = this.menuMapper.listUserPermissions(userId);

        return permissions.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    @Override
    public Tree<Menu> getTree() {
        List<Tree<Menu>> trees = new ArrayList<>();
        List<Menu> menu = menuMapper.selectAll();
        for (Menu sysMenu : menu) {
            Tree<Menu> tree = new Tree<>();
            tree.setId(sysMenu.getId().toString());
            tree.setParentId(sysMenu.getParentId().toString());
            tree.setText(sysMenu.getMenuName());
            
            
            
            
            
            
            trees.add(tree);
        }
        
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Tree<Menu> getTree(Integer roleId) {
        
        new Condition(Menu.class);
        List<Integer> menuIds = menuMapper.listMenuIdByRoleId(roleId);
        List<Tree<Menu>> trees = new ArrayList<>();
        List<Menu> menuDOs = menuMapper.selectAll();
        for (Menu sysMenuDO : menuDOs) {
            Tree<Menu> tree = new Tree<>();
            tree.setId(sysMenuDO.getId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getMenuName());
            Map<String, Object> state = new HashMap<>();
            Integer menuId = sysMenuDO.getId();
            if (menuIds.contains(menuId)) {
                state.put("selected", true);
            } else {
                state.put("selected", false);
            }
            tree.setState(state);
            trees.add(tree);
        }
        
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    @Transactional
    public void deleteMenuById(Integer id) {
        this.menuMapper.deleteRoleMenuByMenuId(id);
        this.menuMapper.deleteByPrimaryKey(id);
    }

}
