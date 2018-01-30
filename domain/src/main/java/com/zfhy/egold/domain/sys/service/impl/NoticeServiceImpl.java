package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.sys.dao.NoticeMapper;
import com.zfhy.egold.domain.sys.entity.Notice;
import com.zfhy.egold.domain.sys.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;



@Service
@Transactional
public class NoticeServiceImpl extends AbstractService<Notice> implements NoticeService{
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private HotInfoService hotInfoService;

    
    @Override
    public List<Notice> listAll() {
        return this.noticeMapper.listAll();
    }

    @Override
    public void saveWithArticle(Notice notice) {
        Integer articelId = null;
        

        HotInfo hotinfo = new HotInfo();

        hotinfo.setStatus(1);
        hotinfo.setDelFlag("0");
        hotinfo.setUpdateDate(new Date());

        hotinfo.setContent(notice.getArticleContent());
        hotinfo.setCreateDate(new Date());
        hotinfo.setSort(1);
        hotinfo.setSource("公告");
        hotinfo.setTitle(notice.getNoticeTitle());
        this.hotInfoService.save(hotinfo);
        articelId = hotinfo.getId();

        notice.setArticleId(articelId);

        this.save(notice);
    }

    @Override
    public void updateWithArticle(Notice notice) {
        HotInfo hotInfo = this.hotInfoService.findById(notice.getArticleId());
        hotInfo.setTitle(notice.getNoticeTitle());
        hotInfo.setUpdateDate(new Date());
        hotInfo.setContent(notice.getArticleContent());
        this.hotInfoService.update(hotInfo);


        this.update(notice);

    }
}
