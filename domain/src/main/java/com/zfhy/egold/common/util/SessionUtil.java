package com.zfhy.egold.common.util;

import com.zfhy.egold.domain.member.dto.MemberSession;

import javax.servlet.http.HttpSession;

/**
 * Created by LAI on 2017/11/12.
 */
public class SessionUtil {

    public static String SESSION_ATTR = "auth_member_session";

    public static MemberSession getSession() {
        HttpSession session = RequestUtil.getHttpServletSession();
        return (MemberSession) session.getAttribute(SESSION_ATTR);
    }

}
