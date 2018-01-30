package com.zfhy.egold.common.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Slf4j
public class DateUtil {


    
    private static final Pattern pattern1 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    
    private static final Pattern pattern2 = Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
    private static final Pattern pattern3 = Pattern.compile("^\\d{1,2}-[A-Za-z]{3}-\\d{4}$");
    private static final Pattern pattern4 = Pattern.compile("^\\d{1,2}\\s[A-Za-z]{3}\\s\\d{4}$");

    
    private static final Pattern pattern5 = Pattern.compile("^^\\d{1,2}-[A-Za-z]{3}-\\d{4}\\s+\\d{2}:\\d{2}:\\d{2}$");

    public static final String YYYY_MM_DD ="yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS ="yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM ="yyyy-MM-dd HH:mm";
    public static final String YYYY_MMDD_HHMMSS ="yyyyMMddHHmmss";
    public static final String YYYYMMDD_CN ="yyyy年MM月dd日";

    private static final SimpleDateFormat dateformat1 = new SimpleDateFormat(YYYY_MM_DD);
    private static final SimpleDateFormat dateformat2 = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    private static final SimpleDateFormat dateformat3 = new SimpleDateFormat(YYYY_MM_DD_HH_MM);

    private static final Map<String, SimpleDateFormat> formats = new HashMap<String, SimpleDateFormat>();
    private static final Map<String, String> monthDic = new HashMap<String, String>();
    static{
        formats.put(YYYY_MM_DD, dateformat1);
        formats.put(YYYY_MM_DD_HH_MM_SS, dateformat2);
        formats.put(YYYY_MM_DD_HH_MM, dateformat3);

        
        String[] months = new String[]{"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
        for(int i = 0; i < months.length; i++){
            String month = months[i];
            String numStr = i < 10 ? "0" + i : "" + i;
            monthDic.put(month, numStr);
            monthDic.put(month.substring(0, 3), numStr);
        }
    }

    
    public static int getCurrentSecond(){
        return (int)(System.currentTimeMillis()/1000);
    }

    
    public static String secondToString(int second, String format) {
        return toString(new Date(second * 1000L), format);
    }



    public static String toString(Date date){
        return date != null ? dateformat2.format(date) : null;
    }

    public static String toString(Date date, String formatStr) {
        if(formatStr == null){
            return toString(date);
        }
        else{
            SimpleDateFormat df = formats.get(formatStr);
            if(df == null){
                df = new SimpleDateFormat(formatStr);
                formats.put(formatStr, df);
            }

            return df.format(date);
        }
    }


    public static final Date convertStringToDate(String format, String strDate) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(format);

        try {
            date = df.parse(strDate);
        } catch (Exception pe) {
            log.error("ParseException: " + pe);
        }
        return (date);
    }

    public static void main(String[] args) {
        System.out.println(new Date(1498620420L * 1000));

        System.out.println(tomorrow());
        System.out.println(yesterday());
        System.out.println(today());
        System.out.println(maxDay());

        System.out.println(afterDays(new Date(), 2));

    }

    public static Date tomorrow() {
       LocalDate localDate = LocalDate.now().plusDays(1L);
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date afterDays(Integer days) {
        LocalDate localDate = LocalDate.now().plusDays((long)days);
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);

    }

    public static Date afterDays(Date date, Integer days) {

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(days);
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);

    }


    public static Date yesterday() {
        LocalDate localDate = LocalDate.now().plusDays(-1L);
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);

    }

    public static Date today() {
        LocalDate localDate = LocalDate.now();
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);

    }

    public static Date maxDay() {
        LocalDate localDate = LocalDate.now().plusYears(999);
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }



}
