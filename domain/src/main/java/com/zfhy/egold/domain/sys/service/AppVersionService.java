package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.common.core.dto.AppType;
import com.zfhy.egold.domain.sys.entity.AppVersion;
import com.zfhy.egold.common.core.Service;



public interface AppVersionService  extends Service<AppVersion> {

    AppVersion findLatelyVersion(AppType appType);
}
