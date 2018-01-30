package com.zfhy.egold.domain.sys.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.sys.entity.Notice;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {


    
    private String noticeTitle;
    

    
    private String noticeContent;
    

    
    private Date noticeTime;
    

    
    private Date invalidTime;
    

    
    private Integer noticeStatus;
    

    
    private Integer adminId;
    

    
    private String remarks;


    
    private String url;
    



    
    public Notice convertTo() {
        return  new NoticeDtoConvert().convert(this);
    }

    
    public NoticeDto convertFrom(Notice notice, String urlPrefix, String terminalType, String terminalId) {
        NoticeDto noticeDto = new NoticeDtoConvert().reverse().convert(notice);

        if (Objects.nonNull(notice.getArticleId())) {
            String url = String.join("", urlPrefix, "/app/newsDetail?id=", notice.getArticleId() + "");
            noticeDto.setUrl(StrFunUtil.assembleUrl(terminalType, terminalId, url));

        }

        return noticeDto;

    }


    private static class NoticeDtoConvert extends  Converter<NoticeDto, Notice> {


        @Override
        protected Notice doForward(NoticeDto noticeDto) {
            Notice notice = new Notice();
            BeanUtils.copyProperties(noticeDto, notice);
            return notice;
        }

        @Override
        protected NoticeDto doBackward(Notice notice) {
            NoticeDto noticeDto = new NoticeDto();
            BeanUtils.copyProperties(notice, noticeDto);

            return noticeDto;
        }
    }
}
