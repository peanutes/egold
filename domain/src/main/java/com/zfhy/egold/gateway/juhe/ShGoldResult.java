package com.zfhy.egold.gateway.juhe;

import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.domain.gold.dto.RealtimePriceDto;
import com.zfhy.egold.domain.gold.entity.RealtimePrice;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Created by LAI on 2017/9/24.
 */
@Slf4j
@Setter
@Getter
public class ShGoldResult {

    private String resultcode;
    private String reason;
    private int error_code;
    private List<Map<String, ShGoldPriceDto>> result;



}
