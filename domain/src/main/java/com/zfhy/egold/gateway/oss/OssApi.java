package com.zfhy.egold.gateway.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.zfhy.egold.common.exception.BusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * Created by LAI on 2017/9/28.
 */
@Component
@Slf4j
public class OssApi {

    @Value("${OSS_ENDPOINT}")
    private String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    @Value("${OSS_ACCESS_ID}")
    private String accessId = "JIxeYIgwUBFOnZnq";
    @Value("${OSS_ACCESS_KEY}")
    private String accessKey = "untK6F5NYRznQbbp1rFBzzOVc3A7O4";
    @Value("${OSS_BUKET}")
    private String bucket = "egold";


    public void uploadBytes(byte[] fileBytes, String filePathWithName) {

        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(endpoint, accessId, accessKey);
            PutObjectResult putObjectResult = ossClient.putObject(bucket, filePathWithName, new ByteArrayInputStream(fileBytes));
            log.info(">>>>>上传结果》》》》》》{}", new Gson().toJson(putObjectResult));
            log.info(">>>>>访问路径》》》》》》{}", "https://egold.oss-cn-shenzhen.aliyuncs.com/" + filePathWithName);

        } catch (Exception e) {
            log.error("OSS上传失败", e);
            throw new BusException("oss 文件上传失败");
        } finally {
            if (Objects.nonNull(ossClient)) {
                ossClient.shutdown();
            }

        }

    }



}
