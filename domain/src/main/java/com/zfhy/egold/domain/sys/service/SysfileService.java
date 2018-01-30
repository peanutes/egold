package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.domain.sys.entity.Sysfile;
import com.zfhy.egold.common.core.Service;



public interface SysfileService  extends Service<Sysfile> {

    String uploadFile(String base64File);
}
