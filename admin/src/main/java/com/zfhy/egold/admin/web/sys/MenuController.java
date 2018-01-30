package com.zfhy.egold.admin.web.sys;

import com.google.gson.Gson;
import com.zfhy.egold.common.core.dto.Tree;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.sys.entity.Menu;
import com.zfhy.egold.domain.sys.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequestMapping("/sys/menu")
@Controller
@Slf4j
public class MenuController {
	@Autowired
	MenuService menuService;

	@RequiresPermissions("sys:menu:menu")
	@GetMapping()
	String menu(Model model) {
		return "sys/menu/menu";
	}

	@RequiresPermissions("sys:menu:menu")
	@RequestMapping("/list")
	@ResponseBody
	List<Menu> list() {
		Condition condition = new Condition(Menu.class);

		condition.createCriteria().andEqualTo("delFlag", "0");
		condition.orderBy("type");
		condition.orderBy("sort");

		List<Menu> menus = this.menuService.findByCondition(condition);


		log.info(">>>>>>>{}", new Gson().toJson(menus));

		return menus;
	}

	@RequiresPermissions("sys:menu:add")
	@GetMapping("/add/{pId}")
	String add(Model model, @PathVariable("pId") Integer pId) {
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.findById(pId).getMenuName());
		}
		return "sys/menu/add";
	}

	@RequiresPermissions("sys:menu:edit")
	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") Integer id) {
		Menu menu = menuService.findById(id);
		if (Objects.isNull(menu)) {
			throw new BusException("菜单不存在");
		}

		model.addAttribute("pId", menu.getParentId());
		if (menu.getParentId() == 0) {
			model.addAttribute("pName", "根目录");
		} else {
			Menu pMenu = this.menuService.findById(menu.getParentId());
			if (Objects.nonNull(pMenu)) {
				model.addAttribute("pName", pMenu.getMenuName());
			}

		}

		model.addAttribute("menu", menu);
		return "sys/menu/edit";
	}

	@RequiresPermissions("sys:menu:remove")
	@PostMapping("/remove")
	@ResponseBody
	Result<String> remove(Integer id) {
		


		menuService.deleteMenuById(id);



		return ResultGenerator.genSuccessResult();
	}

	@RequiresPermissions("sys:menu:add")
	@PostMapping("/save")
	@ResponseBody
	Result<String> save(Menu menu) {
		menu.setCreateDate(new Date());
		menuService.save(menu);
		return ResultGenerator.genSuccessResult();

	}

	@RequiresPermissions("sys:menu:edit")
	@PostMapping("/update")
	@ResponseBody
	Result<String > update(Menu menu) {
		menuService.update(menu);
		return ResultGenerator.genSuccessResult();
	}

	@GetMapping("/tree")
	@ResponseBody
	Result<Tree<Menu>> tree() {
		Tree<Menu> tree =  menuService.getTree();
		return ResultGenerator.genSuccessResult(tree);
	}

	@GetMapping("/tree/{roleId}")
	@ResponseBody
	Result<Tree<Menu>> tree(@PathVariable("roleId") Integer roleId) {
		Tree<Menu> tree = menuService.getTree(roleId);
		return ResultGenerator.genSuccessResult(tree);
	}

}
