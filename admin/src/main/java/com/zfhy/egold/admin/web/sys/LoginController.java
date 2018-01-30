package com.zfhy.egold.admin.web.sys;

import com.zfhy.egold.admin.shiro.ShiroUtil;
import com.zfhy.egold.common.core.dto.Tree;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.domain.sys.entity.Menu;
import com.zfhy.egold.domain.sys.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Slf4j
@Controller
public class LoginController {

	@Value("${app.env}")
	private String appEnv;

	@Autowired
	MenuService menuService;

	@GetMapping({ "/", "", "/index" })
	String index(Model model) {

		List<Tree<Menu>> menus =  menuService.listMenuTree(ShiroUtil.getUserId());

		model.addAttribute("menus", menus);
		model.addAttribute("name", ShiroUtil.getUser().getName());

		model.addAttribute("appEnv", appEnv);

		return "index";
	}

	@GetMapping("/login")
	String login() {
		return "login";
	}

	@PostMapping("/login_bak")
	String doLogin(String username, String password) {


		password = HashUtil.sha1(String.join("", username, password));
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return "redirect:/index";
		} catch (AuthenticationException e) {
			System.out.println("登录失败信息------" + e.getMessage());
			return "redirect:/login";
		}
	}

	@PostMapping("/login")
	@ResponseBody
	Result<String> ajaxLogin(String username, String password) {
		password = HashUtil.sha1(String.join("", username, password));
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return ResultGenerator.genSuccessResult();
		} catch (AuthenticationException e) {
			return ResultGenerator.genFailResult("用户或密码错误");
		}
	}

	@GetMapping("/logout")
	String logout() {
		ShiroUtil.logout();
		return "redirect:/login";
	}

	@GetMapping("/main")
	String main() {
		return "main";
	}

	@GetMapping("/403")
	String error403() {
		return "403";
	}

	@GetMapping("/reset_pwd")
	String resetPwd() {
		return "sys/admin/reset_pwd";
	}

}
