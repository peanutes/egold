package com.zfhy.egold.domain.cms.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.cms.dao.HotInfoMapper;
import com.zfhy.egold.domain.cms.dto.PcHomeHotInfoDto;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.invest.dto.ArticleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
@Slf4j
public class HotInfoServiceImpl extends AbstractService<HotInfo> implements HotInfoService{
    @Autowired
    private HotInfoMapper hotInfoMapper;

    @Override
    public void batchDelete(List<Integer> ids) {
        this.hotInfoMapper.batchDelete(ids);
    }

    @Override
    public List<PcHomeHotInfoDto> findPcHomeHotInfo(ArticleType articleType) {
        return this.hotInfoMapper.findPcHomeHotInfo(articleType.getCode(),6);

    }

    @Override
    public HotInfo findContentById(Integer id) {

        return this.findById(id);
    }
}
