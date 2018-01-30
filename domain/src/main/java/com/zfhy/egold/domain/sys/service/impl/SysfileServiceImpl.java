package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.sys.dao.SysfileMapper;
import com.zfhy.egold.domain.sys.entity.Sysfile;
import com.zfhy.egold.domain.sys.service.SysfileService;
import com.zfhy.egold.gateway.oss.OssApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.UUID;



@Service
@Transactional
@Slf4j
public class SysfileServiceImpl extends AbstractService<Sysfile> implements SysfileService{
    @Autowired
    private SysfileMapper sysfileMapper;


    private String prefix = "sys/sysfile";
    public static String OSS_FILE_URL = "https://egold.oss-cn-shenzhen.aliyuncs.com";



    @Autowired
    private OssApi ossApi;


    @Override
    public String uploadFile(String base64File) {
        String fileName = "upload/" + UUID.randomUUID();

        byte[] fileBytes = Base64Utils.decodeFromString(base64File);
        ossApi.uploadBytes(fileBytes, fileName);


        return String.join("/", OSS_FILE_URL, fileName);
    }
}
