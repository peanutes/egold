package com.zfhy.egold.gateway.juhe;

import com.google.gson.Gson;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.HttpClientUtil;
import com.zfhy.egold.domain.gold.dto.RealtimePriceDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by LAI on 2017/9/24.
 */
@Slf4j
@Component
public class JuheApi {
    public static final String SUCCESS = "200";
    public static final String AU_9999 = "Au99.99";

    @Value("${JUHE_APPKEY}")
    private String juheAppKey;



    public RealtimePriceDto getShGoldPrice() {

        String urlFormat = "http://web.juhe.cn:8080/finance/gold/shgold?key=%s&v=1";


        String result = HttpClientUtil.doGet(String.format(urlFormat, juheAppKey));

        ShGoldResult shGoldResult = new Gson().fromJson(result, ShGoldResult.class);

        if (!Objects.equals(SUCCESS, shGoldResult.getResultcode())) {
            log.error("获取上海黄金价格失败，失败编码：{}, 失败原因：{}", shGoldResult.getError_code(), shGoldResult.getReason());
            throw new BusException("获取黄金价格失败");
        }

        List<Map<String, ShGoldPriceDto>> resultList = shGoldResult.getResult();
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        Map<String, ShGoldPriceDto> resultMap = resultList.get(0);
        if (!resultMap.containsKey(AU_9999)) {
            return null;
        }


        return new RealtimePriceDto().convertFromShGoldPriceDto(resultMap.get(AU_9999));

    }


    public static void main(String[] args) {
        RealtimePriceDto shGoldPrice = new JuheApi().getShGoldPrice();
        log.info(">>>>>>>{}", new Gson().toJson(shGoldPrice));

    }


}
