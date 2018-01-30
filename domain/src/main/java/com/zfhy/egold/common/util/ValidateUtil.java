package com.zfhy.egold.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Po on 2017/10/12.
 */
public class ValidateUtil {
    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^(13|14|15|17|18)[0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


    public static void main(String[] args) {
        System.out.println(isMobile("17130853380"));
    }
}
