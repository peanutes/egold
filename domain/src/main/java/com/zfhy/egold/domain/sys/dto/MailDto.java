package com.zfhy.egold.domain.sys.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.sys.entity.Mail;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {

    
    private Integer id;

    
    private String mailTitle;
    

    
    private String mailContent;
    

    
    private String sendTime;
    

    /*
    private Long sender;
    

    
    private Long reciver;
    

    
    private Integer mailType;
    */

    
    private Integer mailStatus;
    

    /*
    private String remarks;*/
    



    
    public Mail convertTo() {
        return  new MailDtoConvert().convert(this);
    }

    
    public MailDto convertFrom(Mail mail) {
        return new MailDtoConvert().reverse().convert(mail);

    }


    private static class MailDtoConvert extends  Converter<MailDto, Mail> {


        @Override
        protected Mail doForward(MailDto mailDto) {
            Mail mail = new Mail();
            BeanUtils.copyProperties(mailDto, mail);
            return mail;
        }

        @Override
        protected MailDto doBackward(Mail mail) {
            MailDto mailDto = new MailDto();
            BeanUtils.copyProperties(mail, mailDto);
            if (Objects.nonNull(mail.getSendTime())) {
                mailDto.setSendTime(DateUtil.toString(mail.getSendTime(), DateUtil.YYYY_MM_DD));
            }
            return mailDto;
        }
    }
}
