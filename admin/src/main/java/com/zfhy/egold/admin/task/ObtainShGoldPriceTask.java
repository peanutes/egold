package com.zfhy.egold.admin.task;

import com.google.common.collect.ImmutableMap;
import com.zfhy.egold.common.constant.RedisConst;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.gold.dao.DailyPriceMapper;
import com.zfhy.egold.domain.gold.dao.MonthPriceMapper;
import com.zfhy.egold.domain.gold.dao.RealtimePriceMapper;
import com.zfhy.egold.domain.gold.dao.WeekPriceMapper;
import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.dto.RealtimePriceDto;
import com.zfhy.egold.domain.gold.entity.DailyPrice;
import com.zfhy.egold.domain.gold.entity.MonthPrice;
import com.zfhy.egold.domain.gold.entity.RealtimePrice;
import com.zfhy.egold.domain.gold.entity.WeekPrice;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import com.zfhy.egold.gateway.juhe.JuheApi;
import com.zfhy.egold.schedule.config.ScheduledJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * 参考接口文档：https://www.juhe.cn/docs/api/id/29
 * 参考报文：{"resultcode":"200","reason":"SUCCESSED!","result":[{"Au100g":{"variety":"Au100g","latestpri":"278.00","openpri":"278.00","maxpri":"278.00","minpri":"278.00","limit":"0.26%","yespri":"277.29","totalvol":"4.00","time":"2018-01-26 22:56:01"},"Au(T+N1)":{"variety":"Au(T+N1)","latestpri":"280.00","openpri":"280.00","maxpri":"280.00","minpri":"280.00","limit":"0.05%","yespri":"279.85","totalvol":"4.00","time":"2018-01-26 23:22:11"},"Au(T+D)":{"variety":"Au(T+D)","latestpri":"277.15","openpri":"277.18","maxpri":"277.38","minpri":"276.50","limit":"-0.08%","yespri":"277.37","totalvol":"10970.00","time":"2018-01-26 23:59:05"},"Au99.99":{"variety":"Au99.99","latestpri":"277.60","openpri":"277.48","maxpri":"277.87","minpri":"276.80","limit":"0.04%","yespri":"277.50","totalvol":"1105.00","time":"2018-01-26 23:49:53"},"Au99.95":{"variety":"Au99.95","latestpri":"--","openpri":"--","maxpri":"--","minpri":"--","limit":"--","yespri":"277.31","totalvol":"--","time":"2018-01-26 23:51:40"},"Au50g":{"variety":"Au50g","latestpri":"--","openpri":"--","maxpri":"--","minpri":"--","limit":"--","yespri":"255.00","totalvol":"--","time":"2018-01-26 19:50:10"},"Ag99.99":{"variety":"Ag99.99","latestpri":"--","openpri":"--","maxpri":"--","minpri":"--","limit":"--","yespri":"3860.00","totalvol":"--","time":"2018-01-26 19:50:10"},"Ag(T+D)":{"variety":"Ag(T+D)","latestpri":"3796.00","openpri":"3793.00","maxpri":"3803.00","minpri":"3788.00","limit":"-0.13%","yespri":"3801.00","totalvol":"913152.00","time":"2018-01-26 23:59:05"},"Au(T+N2)":{"variety":"Au(T+N2)","latestpri":"284.00","openpri":"283.20","maxpri":"284.00","minpri":"283.20","limit":"-0.14%","yespri":"284.40","totalvol":"32.00","time":"2018-01-26 23:17:02"},"Pt99.95":{"variety":"Pt99.95","latestpri":"--","openpri":"--","maxpri":"--","minpri":"--","limit":"--","yespri":"221.55","totalvol":"--","time":"2018-01-26 22:57:10"},"AU995":{"variety":"AU995","latestpri":"--","openpri":"--","maxpri":"--","minpri":"--","limit":"--","yespri":"275.70","totalvol":"--","time":"2018-01-26 19:50:10"},"AU99.99":{"variety":"AU99.99","latestpri":"277.60","openpri":"277.48","maxpri":"277.87","minpri":"276.80","limit":"0.04%","yespri":"277.50","totalvol":"11054.00","time":"2018-01-26 23:49:53"},"MAUTD":{"variety":"MAUTD","latestpri":"277.11","openpri":"277.00","maxpri":"277.35","minpri":"276.66","limit":"-0.09%","yespri":"277.35","totalvol":"14382.00","time":"2018-01-26 23:58:55"},"IAU99.99":{"variety":"IAU99.99","latestpri":"--","openpri":"--","maxpri":"--","minpri":"--","limit":"--","yespri":"275.79","totalvol":"--","time":"2018-01-26 23:15:15"},"IAU100G":{"variety":"IAU100G","latestpri":"--","openpri":"--","maxpri":"--","minpri":"--","limit":"--","yespri":"275.00","totalvol":"--","time":"2018-01-26 19:59:43"},"IAU99.5":{"variety":"IAU99.5","latestpri":"--","openpri":"--","maxpri":"--","minpri":"--","limit":"--","yespri":"237.80","totalvol":"--","time":"2018-01-26 19:50:10"}}],"error_code":0}
 */
//@Component("obtainShGoldPriceTask")
@Component
@Slf4j
@ScheduledJob(orderNum = 6,name = "定时获取黄金价格", cron = "0 0/1 * * * ?", method = "obtainShGoldPrice",autoStartup = true)
public class ObtainShGoldPriceTask {
    @Autowired
    private JuheApi juheApi;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RealtimePriceMapper realtimePriceMapper;

    @Autowired
    private WeekPriceMapper weekPriceMapper;


    @Autowired
    private MonthPriceMapper monthPriceMapper;

    @Autowired
    private DailyPriceMapper dailyPriceMapper;


    @Autowired
    private DictService dictService;

    public static Map<Integer, Integer> minuteMap = new ImmutableMap.Builder()
            .put(00, 00).put(01, 00).put(02, 00).put(03, 00).put(04, 00)
            .put(10, 10).put(11, 10).put(12, 10).put(13, 10).put(14, 10)
            .put(20, 20).put(21, 20).put(22, 20).put(23, 20).put(24, 20)
            .put(30, 30).put(31, 30).put(32, 30).put(33, 30).put(34, 30)
            .put(40, 40).put(41, 40).put(42, 40).put(43, 40).put(44, 40)
            .put(50, 50).put(51, 50).put(52, 50).put(53, 50).put(54, 50)
            .build();




    public void obtainShGoldPrice() {
        LocalDate nowDate = LocalDate.now();
        String nowDateStr = DateTimeFormatter.BASIC_ISO_DATE.format(nowDate);
        String yesterdayStr = DateTimeFormatter.BASIC_ISO_DATE.format(nowDate.plusDays(-1));


        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();


        Integer begin = dictService.findIntByType(DictType.GOLD_TRACE_BEGIN);
        Integer end = dictService.findIntByType(DictType.GOLD_TRACE_END);

        RealtimePriceDto shGoldPrice = null;
        if (hour > begin && hour < end) {
            
            shGoldPrice = this.juheApi.getShGoldPrice();
            this.redisService.setRealTimePriceDto(shGoldPrice);

        } else {
            
            shGoldPrice = this.redisService.getRealTimePriceDto();
        }


        if (Objects.isNull(shGoldPrice)) {
            log.error("从聚合接口中获取黄金价格失败");
            return;
        }


        if (Objects.isNull(shGoldPrice.getLatestPrice())) {
            
            log.error("最新价格为空");
            shGoldPrice.setLatestPrice(shGoldPrice.getYesPrice());
        }
        Double latestPrice = shGoldPrice.getLatestPrice();



        
        Double oldPrice = this.redisService.getAndSetRealTimePrice(latestPrice);


        if (Objects.nonNull(oldPrice) && Math.abs(oldPrice - latestPrice) > 20) {
            log.error("原来的黄金价格：{}, 现在的黄金价格：{}，相差太大!", oldPrice, latestPrice);
            
    }


        
        if (minuteMap.containsKey(minute)) {
            
            hourPrice(nowDate, localTime, shGoldPrice);
        }

        
        ZonedDateTime recordDateTime = LocalDateTime.of(nowDate, LocalTime.of(hour, 0)).atZone(ZoneId.systemDefault());

        
        
        if (minute <= 20) {
            weekPrice(nowDateStr, hour, shGoldPrice, recordDateTime);
        }


        
        if (0 == hour || (hour % 4 == 0)) {
            monthPrice(nowDateStr, hour, shGoldPrice, recordDateTime);
        }

        
        if (0 == hour) {
            
            dailyPrice(yesterdayStr, hour, shGoldPrice, recordDateTime);

        }


    }



    private void weekPrice(String nowDateStr, int hour, RealtimePriceDto shGoldPrice, ZonedDateTime recordDateTime) {
        String weekKey = String.format(RedisConst.GOLD_WEEK_PRICE_FORMAT, String.format("%s%02d%s",nowDateStr, hour, "0000"));
        java.util.Date weekPriceCreateDate = Date.from(recordDateTime.toInstant());
        java.util.Date weekPriceExpiredAt = Date.from(recordDateTime.plusDays(30).toInstant());

        boolean notExistsWeekPrice = this.redisService.setIfAbsentWeekPrice(weekKey, shGoldPrice.getLatestPrice(), weekPriceExpiredAt);
        if (notExistsWeekPrice) {
            
            WeekPrice weekPrice = new WeekPrice();
            weekPrice.setPrice(shGoldPrice.getLatestPrice());
            weekPrice.setUpdateTime(shGoldPrice.getUpdateTime());
            weekPrice.setCreateDate(weekPriceCreateDate);

            this.weekPriceMapper.insert(weekPrice);
        }
    }



    private void dailyPrice(String yesterdayStr, int hour, RealtimePriceDto shGoldPrice, ZonedDateTime recordDateTime) {
        String dailyKey = String.format(RedisConst.GOLD_DAILY_PRICE_FORMAT, String.format("%s%02d%s",yesterdayStr, hour, "0000"));
        boolean notExistsDailyPrice = this.redisService.setIfAbsentDailyPrice(dailyKey, shGoldPrice.getLatestPrice());
        if (notExistsDailyPrice) {
            DailyPrice dailyPrice = new DailyPrice();
            dailyPrice.setPrice(shGoldPrice.getLatestPrice());
            dailyPrice.setUpdateDate(shGoldPrice.getUpdateTime());
            dailyPrice.setCreateDate(Date.from(recordDateTime.plusDays(-1).toInstant()));

            this.dailyPriceMapper.insert(dailyPrice);
        }
    }



    private void monthPrice(String nowDateStr, int hour, RealtimePriceDto shGoldPrice, ZonedDateTime recordDateTime) {
        String monthKey = String.format(RedisConst.GOLD_MONTH_PRICE_FORMAT, String.format("%s%02d%s", nowDateStr, hour, "0000"));
        java.util.Date monthPriceExpiredAt = Date.from(recordDateTime.plusMonths(6).toInstant());

        
        boolean notExistsMonthPrice = this.redisService.setIfAbsentMonthPrice(monthKey, shGoldPrice.getLatestPrice(), monthPriceExpiredAt);
        if (notExistsMonthPrice) {
            MonthPrice monthPrice = new MonthPrice();
            monthPrice.setCreateDate(Date.from(recordDateTime.toInstant()));
            monthPrice.setPrice(shGoldPrice.getLatestPrice());
            monthPrice.setUpdateTime(shGoldPrice.getUpdateTime());
            this.monthPriceMapper.insert(monthPrice);
        }
    }


    private void hourPrice(LocalDate nowDate,  LocalTime localTime, RealtimePriceDto shGoldPrice) {
        String nowDateStr = DateTimeFormatter.BASIC_ISO_DATE.format(nowDate);

        String hourKey = String.format(RedisConst.GOLD_HOUR_PRICE_FORMAT, String.format("%s%02d%02d%s", nowDateStr, localTime.getHour(), minuteMap.get(localTime.getMinute()), "00"));
        ZonedDateTime recordDateTime = LocalDateTime.of(nowDate, LocalTime.of(localTime.getHour(), minuteMap.get(localTime.getMinute()))).atZone(ZoneId.systemDefault());
        java.util.Date realTimePriceCreateDate = Date.from(recordDateTime.toInstant());
        
        java.util.Date weekPriceExpiredAt = Date.from(recordDateTime.plusMinutes(1441).toInstant());

        PriceDto priceDto = new PriceDto();
        priceDto.setCreateDate(DateUtil.toString(realTimePriceCreateDate, DateUtil.YYYY_MM_DD_HH_MM_SS));
        priceDto.setPrice(shGoldPrice.getLatestPrice());

        boolean notExists = this.redisService.setIfAbsentRealTimePrice(hourKey, priceDto, weekPriceExpiredAt);
        if (notExists) {
            
            RealtimePrice realtimePrice = shGoldPrice.convertTo();
            realtimePrice.setCreateDate(realTimePriceCreateDate);
            this.realtimePriceMapper.insert(realtimePrice);
        }
    }

    public static void main(String[] args) {
        LocalDate nowDate = LocalDate.now();
        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();

        String localDateStr = DateTimeFormatter.BASIC_ISO_DATE.format(nowDate);
        System.out.println(localDateStr);

        java.util.Date from = Date.from(LocalDateTime.of(nowDate, LocalTime.of(hour, 0)).atZone(ZoneId.systemDefault()).toInstant());


        System.out.println(DateUtil.toString(from));

        LocalTime of = LocalTime.of(0, 9);
        System.out.println(of.getHour());

        LocalTime localTime = LocalTime.now().plusHours(8);
        System.out.println(localTime.getHour());

        System.out.println(String.format("%02d", 1));


        System.out.println(System.currentTimeMillis());

    }

}
