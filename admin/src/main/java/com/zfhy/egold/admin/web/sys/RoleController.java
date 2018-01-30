package com.zfhy.egold.admin.web.sys;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.entity.Role;
import com.zfhy.egold.domain.sys.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping("/sys/role")
@Controller
public class RoleController {
	String prefix = "sys/role";
	@Autowired
	RoleService roleService;

	@RequiresPermissions("sys:role:role")
	@GetMapping()
	String role() {
		return prefix + "/role";
	}

	@RequiresPermissions("sys:role:role")
	@GetMapping("/list")
	@ResponseBody()
	List<Role> list() {
		return roleService.findAll();
	}

	@RequiresPermissions("sys:role:add")
	@GetMapping("/add")
	String add() {
		return prefix + "/add";
	}

	@RequiresPermissions("sys:role:edit")
	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Integer id, Model model) {
		Role role = roleService.findById(id);
		model.addAttribute("role", role);
		return prefix + "/edit";
	}

	@RequiresPermissions("sys:role:add")
	@PostMapping("/save")
	@ResponseBody()
	Result<String> save(Role role) {

		role.setCreateDate(new Date());
		this.roleService.saveRole(role);
		return ResultGenerator.genSuccessResult();
	}

	@RequiresPermissions("sys:role:edit")
	@PostMapping("/update")
	@ResponseBody()
	Result<String> update(Role role) {
		this.roleService.updateRole(role);

		return ResultGenerator.genSuccessResult();
	}

	@RequiresPermissions("sys:role:remove")
	@PostMapping("/remove")
	@ResponseBody()
	Result<String> remove(Integer id) {
		

		this.roleService.deleteRoleById(id);
		return ResultGenerator.genSuccessResult();
	}
}
