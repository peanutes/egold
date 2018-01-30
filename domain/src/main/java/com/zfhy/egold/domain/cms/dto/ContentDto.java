package com.zfhy.egold.domain.cms.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.cms.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto {


    
    private String title;
    

    
    private String slug;
    

    
    private Long created;
    

    
    private Long modified;
    

    
    private String content;
    

    
    private String type;
    

    
    private String tags;
    

    
    private String categories;
    

    
    private Integer hits;
    

    
    private Integer commentsNum;
    

    
    private Integer allowComment;
    

    
    private Integer allowPing;
    

    
    private Integer allowFeed;
    

    
    private Integer status;
    

    
    private String author;
    

    
    private Date gtmCreate;
    

    
    private Date gtmModified;
    



    
    public Content convertTo() {
        return  new ContentDtoConvert().convert(this);
    }

    
    public ContentDto convertFrom(Content content) {
        return new ContentDtoConvert().reverse().convert(content);

    }


    private static class ContentDtoConvert extends  Converter<ContentDto, Content> {


        @Override
        protected Content doForward(ContentDto contentDto) {
            Content content = new Content();
            BeanUtils.copyProperties(contentDto, content);
            return content;
        }

        @Override
        protected ContentDto doBackward(Content content) {
            ContentDto contentDto = new ContentDto();
            BeanUtils.copyProperties(content, contentDto);
            return contentDto;
        }
    }
}
