package com.zfhy.egold.admin.web.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.shiro.ShiroUtil;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.sys.entity.Admin;
import com.zfhy.egold.domain.sys.entity.Role;
import com.zfhy.egold.domain.sys.service.AdminService;
import com.zfhy.egold.domain.sys.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestMapping("/sys/admin")
@Controller
public class AdminController {
	@Autowired
	AdminService adminService;
	@Autowired
	RoleService roleService;

	@RequiresPermissions("sys:admin:admin")
	@GetMapping("")
	String admin(Model model) {
		return "sys/admin/admin";
	}

	@GetMapping("/list")
	@ResponseBody
	PageUtils list(@RequestParam Map<String, String> params) {

		PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
		Condition condition = new Condition(Admin.class);
		Example.Criteria criteria = condition.createCriteria();
		List<Map.Entry<String, String>> parameters = params.entrySet().stream()
				.filter(entry -> StringUtils.isNotBlank(entry.getValue()))
				.filter(entry -> !"page".equalsIgnoreCase(entry.getKey()) && !"size".equalsIgnoreCase(entry.getKey()))
				.collect(Collectors.toList());

		for (Map.Entry<String, String> entry : parameters) {
			criteria.andEqualTo(entry.getKey(), entry.getValue());
		}

		List<Admin> adminList = this.adminService.findByCondition(condition);
		PageInfo<Admin> pageInfo = new PageInfo<>(adminList);

		return new PageUtils(adminList, (int)pageInfo.getTotal());
	}

	@RequiresPermissions("sys:admin:add")
	@GetMapping("/add")
	String add(Model model) {
		List<Role> roles = roleService.findAll();
		model.addAttribute("roles", roles);
		return "sys/admin/add";
	}

	@RequiresPermissions("sys:admin:add")
	@PostMapping("/save")
	@ResponseBody
	Result<String> save(Admin admin, String confirm_password) {
		if (!Objects.equals(admin.getPassword(), confirm_password)) {
			throw new BusException("密码与确认密码不一致！");
		}


		admin.setPassword(HashUtil.sha1(String.join("", admin.getLoginAccount(), admin.getPassword())));
		this.adminService.saveAdminAndAdminRole(admin);

		return ResultGenerator.genSuccessResult();
	}


	@PostMapping("/notExists")
	@ResponseBody
	boolean notExists(@RequestParam String loginAccount) {
		Admin admin = this.adminService.findBy("loginAccount", loginAccount);

		return Objects.isNull(admin) || admin.getId() == 0;
	}

	@RequiresPermissions("sys:admin:edit")
	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") Integer id) {
		Admin userDO = this.adminService.findById(id);
		model.addAttribute("admin", userDO);
		List<Role> roles = roleService.findRolesByAdminId(id);
		model.addAttribute("roles", roles);
		return "sys/admin/edit";
	}

	@RequiresPermissions("sys:admin:edit")
	@PostMapping("/update")
	@ResponseBody
	Result<String> update(Admin admin) {
		this.adminService.saveAdminWithRole(admin);
		return ResultGenerator.genSuccessResult();
	}


	@RequiresPermissions("sys:admin:remove")
	@PostMapping("/remove")
	@ResponseBody
	Result<String> remove(Integer id) {

		this.adminService.removeWhtiRoleByAdminId(id);

		return ResultGenerator.genSuccessResult();
	}

	@RequiresPermissions("sys:admin:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	Result<String> batchRemove(@RequestParam("ids[]") Integer[] userIds) {
		
		List<Integer> ids = Arrays.asList(userIds);

		this.adminService.deleteByIds(ids);

		return ResultGenerator.genSuccessResult();
	}


	@RequiresPermissions("sys:admin:resetPwd")
	@GetMapping("/resetPwd/{id}")
	String resetPwd(@PathVariable("id") Integer adminId, Model model) {

		Admin admin = new Admin();
		admin.setId(adminId);
		model.addAttribute("admin", admin);
		return "sys/admin/reset_pwd";
	}

	@RequiresPermissions("sys:admin:resetPwd")
	@PostMapping("/resetPwd")
	@ResponseBody
	Result<String> resetPwd(String password) {


		this.adminService.resetPwd(ShiroUtil.getUserId(), password);
		return ResultGenerator.genSuccessResult();
	}


	@PostMapping("/validatePwd")
	@ResponseBody
	boolean validatePwd(String opassword) {
		
		int userId = ShiroUtil.getUserId();
		return this.adminService.passsexist(userId,opassword);
	}


}
