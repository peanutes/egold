package com.zfhy.egold.wap.web.gold;

import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.gold.dto.PriceSerialDto;
import com.zfhy.egold.domain.gold.service.*;
import com.zfhy.egold.domain.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/gold/price")
@Api(value = "PriceController", tags = "PriceController", description = "黄金价格")
@Slf4j
@Validated
public class PriceController {
    @Resource
    private RealtimePriceService realtimePriceService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WeekPriceService weekPriceService;

    @Autowired
    private MonthPriceService monthPriceService;

    @Autowired
    private DailyPriceService dailyPriceService;

    @Autowired
    private PriceService priceService;


    
    @PostMapping("/hourGoldPrices")
    public Result<PriceSerialDto> hourGoldPrices(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        PriceSerialDto priceSerialDto = this.priceService.hourGoldPrices();

        return ResultGenerator.genSuccessResult(priceSerialDto);
    }




    
    @PostMapping("/weekGoldPrices")
    public Result<PriceSerialDto> weekGoldPrices(SysParameterWithoutToken sysParameterWithoutToken) {


        PriceSerialDto priceSerialDto = this.priceService.weekGoldPrices();

        return ResultGenerator.genSuccessResult(priceSerialDto);
    }


    
    @PostMapping("/monthGoldPrices")
    public Result<PriceSerialDto> monthGoldPrices(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        PriceSerialDto priceSerialDto = this.priceService.monthPriceService();

        return ResultGenerator.genSuccessResult(priceSerialDto);
    }

    
    @PostMapping("/dailyGoldPrices")
    public Result<PriceSerialDto> dailyGoldPrices(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {



        PriceSerialDto priceSerialDto = this.priceService.dailyPrices();

        return ResultGenerator.genSuccessResult(priceSerialDto);
    }

    
    @PostMapping("/realTimePrice")
    public Result<Double> realTimePrice(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {
        Double price = this.redisService.getRealTimePrice();

        return ResultGenerator.genSuccessResult(price);

    }


}
