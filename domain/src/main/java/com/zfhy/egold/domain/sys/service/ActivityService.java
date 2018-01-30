package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.sys.dto.ActivityType;
import com.zfhy.egold.domain.sys.entity.Activity;
import com.zfhy.egold.common.core.Service;



public interface ActivityService  extends Service<Activity> {

    Activity findByType(ActivityType activityType);
}
