package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.sys.dao.ActivityMapper;
import com.zfhy.egold.domain.sys.dto.ActivityType;
import com.zfhy.egold.domain.sys.entity.Activity;
import com.zfhy.egold.domain.sys.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;



@Service
@Transactional
@Slf4j
public class ActivityServiceImpl extends AbstractService<Activity> implements ActivityService{
    @Autowired
    private ActivityMapper activityMapper;

    @Transactional(readOnly = true)
    @Override
    public Activity findByType(ActivityType activityType) {

        Condition condition = new Condition(Activity.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("type", activityType.getCode())
                .andLessThan("beginDate", new Date())
                .andGreaterThan("endDate", new Date());

        condition.orderBy("beginDate");


        List<Activity> activityList = this.findByCondition(condition);


        if (CollectionUtils.isEmpty(activityList)) {
            return null;
        }

        return activityList.get(0);
    }
}
