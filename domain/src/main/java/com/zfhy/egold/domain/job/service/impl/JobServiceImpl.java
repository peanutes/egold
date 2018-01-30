package com.zfhy.egold.domain.job.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.job.dao.JobMapper;
import com.zfhy.egold.domain.job.entity.ScheduleJob;
import com.zfhy.egold.domain.job.service.JobService;
import com.zfhy.egold.domain.order.entity.MyorderAttr;
import com.zfhy.egold.domain.order.service.MyorderAttrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhongmin on 2018/1/27.
 */
@Service
@Slf4j
public class JobServiceImpl  implements JobService {
    @Autowired
    private  JobMapper jobMapper;
    @Override
    public ScheduleJob queryScheduleJob(String uuid) {
        return jobMapper.queryScheduleJob(uuid);
    }

    @Override
    public void addJob(ScheduleJob job) {
        jobMapper.addJob(job);
    }
}
