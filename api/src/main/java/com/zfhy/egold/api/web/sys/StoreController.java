package com.zfhy.egold.api.web.sys;

import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.dto.StoreDto;
import com.zfhy.egold.domain.sys.entity.Store;
import com.zfhy.egold.domain.sys.service.StoreService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/sys/store")
@Api(value = "StoreController",tags = "StoreController", description = "线下门店")
@Slf4j
@Validated
public class StoreController {
    @Resource
    private StoreService storeService;


    
    @PostMapping("/list")
    public Result<List<StoreDto>> list(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        List<Store> storeList = this.storeService.findAll();
        List<StoreDto> storeDtos = storeList.stream().map(e -> new StoreDto().convertFrom(e)).collect(Collectors.toList());

        return ResultGenerator.genSuccessResult(storeDtos);

    }
}
