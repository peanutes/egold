package com.zfhy.egold.domain.job.service;

import com.zfhy.egold.domain.job.entity.ScheduleJob;

/**
 * Created by zhongmin on 2018/1/27.
 */
public interface JobService {
    ScheduleJob queryScheduleJob(String uuid);
    void addJob(ScheduleJob job);
}
