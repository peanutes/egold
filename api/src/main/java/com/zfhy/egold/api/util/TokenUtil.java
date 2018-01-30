package com.zfhy.egold.api.util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zfhy.egold.domain.member.dto.MemberSession;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class TokenUtil {
    public static ThreadLocal<MemberSession> MEMBER_SESSION_THREAD_LOCAL = new ThreadLocal<>();


    
    public static MemberSession getMemberSession() {
        return MEMBER_SESSION_THREAD_LOCAL.get();
    }


    public static Integer getSessionMemberId() {
        MemberSession memberSession = MEMBER_SESSION_THREAD_LOCAL.get();
        if (Objects.isNull(memberSession)) {
            return 0;
        }

        return memberSession.getId();

    }



    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());

        List<String> strings = Lists.newArrayList();
        strings.addAll(Arrays.asList("1", "2"));


        strings.add("3");
        System.out.println(new Gson().toJson(strings));

    }

}
