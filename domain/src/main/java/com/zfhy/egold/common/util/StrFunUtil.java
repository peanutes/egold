package com.zfhy.egold.common.util;


import org.apache.commons.lang.StringUtils;

/**
 * Created by pangjian on 16-12-2.
 */
public class StrFunUtil {

    public static int valueInt(String value){
        return Integer.valueOf(value);
    }


    public static String valueOf(Double value) {
        if (value == null)
            return null;
        return String.valueOf(value);
    }

    public static String valueOf(Long value) {
        if (value == null)
            return null;
        return String.valueOf(value);
    }

    /* 获取指定数量的随机数 */
    public static String genRandom(int count) {
        String str = "";
        for (int i = 1; i <= count; i++) {
            char ca = (char) (Math.random() * 9 + 48);
            str += ca + "";
        }
        return str;
    }

    public static String assembleUrl(String terminalType, String terminalId, String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        if (url.contains("?")) {
            if (url.endsWith("?")) {
                return String.join("", url, "terminalType=", terminalType, "&terminalId=", terminalId);
            } else {
                return String.join("", url, "&terminalType=", terminalType, "&terminalId=", terminalId);
            }

        } else {
            return String.join("", url, "?terminalType=", terminalType, "&terminalId=", terminalId);
        }
    }


    public static String hidMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return "";
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }


    public static String hidIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return "";
        }
        return idCard.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1****$2");
    }



}
