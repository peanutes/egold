package com.zfhy.egold.admin.shiro;

import com.zfhy.egold.domain.sys.entity.Admin;
import com.zfhy.egold.domain.sys.service.AdminService;
import com.zfhy.egold.domain.sys.service.MenuService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {
	@Autowired
	private MenuService menuService;

	@Autowired
	private AdminService adminService;



	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		int userId = ShiroUtil.getUserId();
		Set<String> perms = menuService.listPermissions(userId);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(perms);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		Map<String, Object> map  = new HashMap<>();
		map.put("username", username);
		String password = new String((char[]) token.getCredentials());

		
		Admin admin = this.adminService.findBy("loginAccount", username);


		
		if (Objects.isNull(admin)) {
			throw new UnknownAccountException("账号或密码不正确");
		}

		
		if (!password.equals(admin.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}

		
		if ("0".equals(admin.getStatus())) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(admin, password, getName());
		return info;
	}

}
