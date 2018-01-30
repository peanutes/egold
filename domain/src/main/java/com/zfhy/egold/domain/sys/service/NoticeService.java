package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.sys.entity.Notice;

import java.util.List;



public interface NoticeService  extends Service<Notice> {

    List<Notice> listAll();

    void saveWithArticle(Notice notice);

    void updateWithArticle(Notice notice);

}
