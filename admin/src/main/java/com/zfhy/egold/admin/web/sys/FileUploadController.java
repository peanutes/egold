package com.zfhy.egold.admin.web.sys;

import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.gateway.oss.OssApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by LAI on 2017/9/26.
 */
@RequestMapping("/sys/upload")
@Controller
@Slf4j
public class FileUploadController {
    @Autowired
    private OssApi ossApi;

    @PostMapping("/singleFileUpload")
    @ResponseBody
    public boolean singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) {

        if (Objects.isNull(file)) {
            throw new BusException("请上传文件");
        }

        String filePathWithName = StringUtils.isNotBlank(folder) ? String.join("", StringUtils.trimToEmpty(folder), file.getOriginalFilename()) : file.getOriginalFilename();

        try {
            byte[] fileBytes = file.getBytes();
            ossApi.uploadBytes(fileBytes, filePathWithName);
        } catch (IOException e) {
            log.error("上传失败", e);
            throw new BusException("上传文件失败");
        }
        return true;
    }



}
