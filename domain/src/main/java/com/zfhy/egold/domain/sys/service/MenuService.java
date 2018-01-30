package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.common.core.dto.Tree;
import com.zfhy.egold.domain.sys.entity.Menu;
import com.zfhy.egold.common.core.Service;

import java.util.List;
import java.util.Set;



public interface MenuService  extends Service<Menu> {

    List<Tree<Menu>> listMenuTree(Integer userId);

    Set<String> listPermissions(Integer userId);

    Tree<Menu> getTree();

    Tree<Menu> getTree(Integer roleId);

    void deleteMenuById(Integer id);
}
