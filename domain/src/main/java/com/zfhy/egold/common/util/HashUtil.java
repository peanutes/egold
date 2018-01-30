package com.zfhy.egold.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
public class HashUtil {




    public static String getMd5(String text) {
        return DigestUtils.md5Hex(text);
    }

    public static String sha1(String text) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            log.error("sha1签名错误", e);
            return null;
        }
        digest.update(text.getBytes());
        byte messageDigest[] = digest.digest();
        return Hex.encodeHexString(messageDigest);

    }



    public static String genSalt(int count){
        char[] chars="0123456789abcdefghijklmnopqrwtuvzxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        Random RANDOM = new SecureRandom();
        return IntStream.range(0, count)
                .mapToObj(i -> chars[RANDOM.nextInt(62)])
                .map(ch -> Character.toString(ch))
                .collect(Collectors.joining());

    }

    public static String getMd5Salt(String pwd,int saltLen){
        String salt = HashUtil.genSalt(saltLen);
        String md5 = HashUtil.getMd5(pwd);
        return HashUtil.getMd5(md5 + salt);
    }

    public static void main(String[] args) {
        System.out.println(genSalt(6));
    }

}
