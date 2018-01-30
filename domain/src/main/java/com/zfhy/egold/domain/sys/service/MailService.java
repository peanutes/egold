package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.sys.entity.Mail;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface MailService  extends Service<Mail> {

    List<Mail> findMailByMemberId(Integer memberId);

    void sendMail(String title, String content, Integer reciver);

    void settingMailRead(Integer memberId, Integer mailId);
}
