package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.sys.entity.Config;
import com.zfhy.egold.common.core.Service;



public interface ConfigService  extends Service<Config> {

    Config findByType(String type);
}
