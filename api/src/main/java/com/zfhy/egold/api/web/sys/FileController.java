package com.zfhy.egold.api.web.sys;

import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.service.SysfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/sys/file")
@Api(value = "FileController",tags = "FileController", description = "文件管理")
@Slf4j
@Validated
public class FileController {


    @Autowired
    private SysfileService sysfileService;


    
    @PostMapping("/uploadFile")
    public Result<String> uploadFile(
            @ApiParam(name = "base64File", value = "文件内容(文件的base64编码)", required = true)
            @NotBlank(message = "文件内容不能为空")
            @RequestBody  String base64File,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {


        String url = sysfileService.uploadFile(base64File);

        return ResultGenerator.genSuccessResult(url);
    }

}
