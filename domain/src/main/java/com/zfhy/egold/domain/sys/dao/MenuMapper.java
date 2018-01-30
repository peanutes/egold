package com.zfhy.egold.domain.sys.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.sys.entity.Menu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper  extends Mapper<Menu>{
    @Select("select m.permission from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id left join sys_admin_role ar on ar.role_id = rm.role_id  where ar.admin_id = #{id} order by  m.type, m.sort")
    List<String> listUserPermissions(int id);

    @Select("select m.* from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id left join sys_admin_role ar on ar.role_id = rm.role_id  where ar.admin_id = #{id} order by m.type, m.sort")
    List<Menu> listMenuByUserId(int userId);


    @Select("select m.id from sys_menu m left join  sys_role_menu rm on m.id = rm.menu_id where  rm.role_id = #{roleId} order  by m.type, m.sort")
    List<Integer> listMenuIdByRoleId(Integer roleId);

    @Delete("delete from sys_role_menu where menu_id = #{menuId}")
    void deleteRoleMenuByMenuId(Integer menuId);

    @Select("delete from sys_role_menu where role_id = #{roleId}")
    void removeByRoleId(Integer id);
}
