package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.sys.dao.MailMapper;
import com.zfhy.egold.domain.sys.dto.MailStatus;
import com.zfhy.egold.domain.sys.entity.Mail;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.sys.service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;
import java.util.Objects;



@Service
@Transactional
public class MailServiceImpl extends AbstractService<Mail> implements MailService{
    @Autowired
    private MailMapper mailMapper;

    @Override
    public List<Mail> findMailByMemberId(Integer memberId) {
        Condition condition = new Condition(Mail.class);
        condition.createCriteria()
                .andEqualTo("reciver", memberId)
                .andNotEqualTo("mailStatus", 2);
        condition.setOrderByClause("mail_status ASC, create_date DESC");


        return this.findByCondition(condition);
    }

    @Override
    public void sendMail(String title, String content, Integer reciver) {
        Mail mail = new Mail();
        mail.setDelFlag("0");
        mail.setCreateDate(new Date());
        mail.setMailContent(content);
        mail.setMailStatus(MailStatus.UN_READ.getCode());
        mail.setMailTitle(title);
        mail.setMailType(1);
        mail.setReciver(reciver);
        mail.setSendTime(new Date());
        mail.setUpdateDate(new Date());

        this.save(mail);
    }

    @Override
    public void settingMailRead(Integer memberId, Integer mailId) {
        Mail mail = this.findById(mailId);
        if (Objects.isNull(mail)) {
            throw new BusException("站内信不存在");
        }

        if (!Objects.equals(mail.getReciver(), memberId)) {
            throw new BusException("不能操作其他人的站内信");
        }


        mail.setMailStatus(MailStatus.HAD_READ.getCode());
        this.update(mail);
    }

}
