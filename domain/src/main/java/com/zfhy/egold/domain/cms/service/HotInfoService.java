package com.zfhy.egold.domain.cms.service;

import com.zfhy.egold.domain.cms.dto.PcHomeHotInfoDto;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.invest.dto.ArticleType;

import java.util.List;



public interface HotInfoService  extends Service<HotInfo> {

    void batchDelete(List<Integer> integers);

    List<PcHomeHotInfoDto> findPcHomeHotInfo(ArticleType hostInfo);

    HotInfo findContentById(Integer id);
}
