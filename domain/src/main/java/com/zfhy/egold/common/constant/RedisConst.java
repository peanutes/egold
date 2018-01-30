package com.zfhy.egold.common.constant;


public interface RedisConst {
    String TOKEN_KEY_FORMAT = "egold:token:%s";
    String SMS_KEY_FORMAT = "egold:sms:%s";

    
    String GOLD_REAL_PRICE = "egold:goleprice:current";


    
    String GOLD_REAL_PRICEDTO = "egold:goleprice:currentdto";

    
    String GOLD_HOUR_PRICE_FORMAT = "egold:goleprice:hour:%s";
    String GOLD_HOUR_PRICE_LIST = "egold:goleprice:hour:*";

    
    String GOLD_WEEK_PRICE_FORMAT = "egold:goleprice:week:%s";
    String GOLD_WEEK_PRICE_LIST = "egold:goleprice:week:*";

    
    String GOLD_MONTH_PRICE_FORMAT = "egold:goleprice:month:%s";
    String GOLD_MONTH_PRICE_LIST = "egold:goleprice:month:*";


    
    String GOLD_DAILY_PRICE_FORMAT = "egold:goleprice:daily:%s";
    String GOLD_DAILY_PRICE_LIST = "egold:goleprice:daily:*";


    
    String ZHONG_SHENG_TOKEN = "egold:zhongsheng:token";


    
    String ORDER_SN_SERIAL = "egold:order:%s";


    
    String GOOD_ORDER_SN_SERIAL = "egold:goodorder:%s";







}
