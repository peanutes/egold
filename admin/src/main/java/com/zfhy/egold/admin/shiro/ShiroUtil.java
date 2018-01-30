package com.zfhy.egold.admin.shiro;

import com.zfhy.egold.domain.sys.dto.AdminDto;
import com.zfhy.egold.domain.sys.dto.SecureAdminDto;
import com.zfhy.egold.domain.sys.entity.Admin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.Objects;

/**
 * Created by LAI on 2017/9/17.
 */
public class ShiroUtil {
    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }
    public static Admin getUser() {
        return (Admin)getSubjct().getPrincipal();
    }
    public static int getUserId() {
        return getUser().getId();
    }

    public static String getLogginAccount() {
        Admin user = getUser();
        if (Objects.isNull(user)) {
            return "";
        }
        return user.getLoginAccount();
    }

    public static void logout() {
        getSubjct().logout();
    }
}
