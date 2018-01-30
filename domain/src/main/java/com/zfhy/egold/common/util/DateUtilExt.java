package com.zfhy.egold.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public final class DateUtilExt {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final DateTimeFormatter LOCALDATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter LOCALDATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter INT_LOCALDATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter INT_LOCALDATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final DateTimeFormatter LOCALTIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");


    private DateUtilExt(){}

    /**
     * 把字符串转化成LocalDate
     * @param dateStr
     * @return
     */
    public static LocalDate strToLocalDate(String dateStr) {
        return strToLocalDate(dateStr, LOCALDATE_FORMAT);
    }

    /**
     * 把字符串转化成LocalDate
     * @param dateStr
     * @param formatter
     * @return
     */
    public static LocalDate strToLocalDate(String dateStr, DateTimeFormatter formatter) {
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * 把字符串转化成LocalDateTime
     * @param dateStr
     * @return
     */
    public static LocalDateTime strToLocalDateTime(String dateStr) {
        return strToLocalDateTime(dateStr, LOCALDATETIME_FORMAT);
    }

    /**
     * 把字符串转化成LocalDateTime
     * @param dateStr
     * @return
     */
    public static LocalDateTime strToLocalDateTime(String dateStr, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateStr, formatter);
    }

    /**
     * 把LocalDate转化成字符串
     * @param localDate
     * @return
     */
    public static String dateToStr(LocalDate localDate) {
        if(null == localDate){
            return null;
        }
        return dateToStr(localDate, LOCALDATE_FORMAT);
    }

    /**
     * 把LocalDate转化成字符串
     * @param localDate
     * @param formatter
     * @return
     */
    public static String dateToStr(LocalDate localDate, DateTimeFormatter formatter) {
        return localDate.format(formatter);
    }

    /**
     * 把LocalDateTime转化成字符串
     * @param localDateTime
     * @return
     */
    public static String dateToStr(LocalDateTime localDateTime) {
        return dateToStr(localDateTime, LOCALDATETIME_FORMAT);
    }

    /**
     * 把LocalDateTime转化成字符串
     * @param localDateTime
     * @return
     */
    public static String dateToStr(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static int getDay(LocalDateTime date) {
        return date.getDayOfMonth();
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static int getDay(LocalDate date) {
        return date.getDayOfMonth();
    }

    /**
     * 判断是否是日期
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        return isDate(date, LOCALDATE_FORMAT);
    }

    /**
     * 判断是否是时间
     * @param date
     * @return
     */
    public static boolean isDateTime(String date) {
        return isDate(date, LOCALDATETIME_FORMAT);
    }

    /**
     * 判断是否是日期
     * @param date
     * @param format
     * @return
     */
    public static boolean isDate(String date, DateTimeFormatter format) {
        format.toString();//avoid NullPointerException
        try {
            format.parse(date);
            return true;
        } catch (Exception e) {
        	logger.error("判断是否日期异常",e.getMessage(),e);
            return false;
        }
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static int getMonth(LocalDate date) {
        return date.getMonthValue();
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static int getMonth(LocalDateTime date) {
        return date.getMonthValue();
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static int getYear(LocalDate date) {
        return date.getYear();
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static int getYear(LocalDateTime date) {
        return date.getYear();
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean eq(LocalDate date1, LocalDate date2) {
        return date1.equals(date2);
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean eq(LocalDateTime date1, LocalDateTime date2) {
        return date1.equals(date2);
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean ne(LocalDate date1, LocalDate date2) {
        return !date1.equals(date2);
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean ne(LocalDateTime date1, LocalDateTime date2) {
        return !date1.equals(date2);
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean lt(LocalDate date1, LocalDate date2) {
        return date1.compareTo(date2) < 1;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean lt(LocalDateTime date1, LocalDateTime date2) {
    	return date1.compareTo(date2) < 1;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean le(LocalDate date1, LocalDate date2) {
        return date1.compareTo(date2) <= 1;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean le(LocalDateTime date1, LocalDateTime date2) {
        return date1.compareTo(date2) <= 1;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean gt(LocalDate date1, LocalDate date2) {
        return date1.compareTo(date2) > 0;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean gt(LocalDateTime date1, LocalDateTime date2) {
    	return date1.compareTo(date2) > 0;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean ge(LocalDate date1, LocalDate date2) {
    	return date1.compareTo(date2) >= 0;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean ge(LocalDateTime date1, LocalDateTime date2) {
    	return date1.compareTo(date2) >= 0;
    }

    /**
     * 获取当前时间（年月日时分秒）
     * @return
     */
    /*public static LocalDateTime getNowLocalDateTime() {
        return dateSupplier.getNowLocalDateTime();
    }*/

    /**
     * 获取当前日期（年月日）
     * @return
     */
    /*public static LocalDate getNowLocalDate() {
        return dateSupplier.getNowLocalDate();
    }*/

    /**
     * 获取两个日期的月份数
     * @param startDate
     * @param endDate
     * @return
     */
    public static Integer getBetweenMonths(LocalDate startDate, LocalDate endDate) {
        Period pe = Period.between(startDate, endDate);
        return pe.getYears() * 12 + pe.getMonths();
    }
    
    /**
     * 获取两个日期的年份数
     * @param startDate
     * @param endDate
     * @return
     */
    public static Integer getBetweenYears(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getYears();
    }

    /**
     * 获取两个日期的天数
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getBetweenDays(LocalDate startDate, LocalDate endDate) {
    	return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 获取两个日期的秒数
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long getBetweenSeconds(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).getSeconds();
    }
    
    /**
     * 添加月份
     * @param startDate
     * @param month
     * @return
     */
    public static LocalDate addMonth(LocalDate startDate, int month) {
        return startDate.plusMonths(month);
    }
    
    /**
     * 添加月份
     * @param startDate
     * @param month
     * @return
     */
    public static LocalDateTime addMonth(LocalDateTime startDate, int month) {
    	return startDate.plusMonths(month);
    }

    /**
     * 获得指定日期的前一天
     * @param date
     * @return
     */
    public static LocalDate getSpecifiedDayBefore(LocalDate date) {
        return date.minusDays(1L);
    }
    
    /**
     * 获得指定日期的后一天
     * @param date
     * @return
     */
    public static LocalDate getSpecifiedDayAfter(LocalDate date) {
    	return date.plusDays(1l);
    }
    
    /**
     * 添加天数
     * @param date
     * @param days
     * @return
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }
    
    /**
     * 添加天数
     * @param date
     * @param days
     * @return
     */
    public static LocalDateTime addDays(LocalDateTime date, int days) {
        return date.plusDays(days);
    }
    
    /**
     * 获取当前日期所在月的最后一天
     * @param date
     * @return
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return LocalDate.from(TemporalAdjusters.lastDayOfMonth().adjustInto(date));
    }
    
    /**
     * 获取当前日期所在月的最后一天
     * @param date
     * @return
     */
    public static LocalDateTime getLastDayOfMonth(LocalDateTime date) {
        return LocalDateTime.from(TemporalAdjusters.lastDayOfMonth().adjustInto(date));
    }

    /**
     * 判断 compareTime 是否在 startTime，endTime 之间
     * @param compareTime
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isBetweenTimes(LocalTime compareTime, LocalTime startTime, LocalTime endTime) {
        return compareTime.compareTo(startTime) >= 0 && (compareTime.compareTo(endTime)) <= 0;
    }
    
    /**
     * 判断当前为周几
     * @return
     */
    public static Integer getNowWeek() {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
	}
    
    /**
     * 判断当前为周几
     * @return
     */
    public static String getNowWeekName() {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w == 1){
			return "Monday";
		}
		if (w == 2){
			return "Tuesday";
		}
		if (w == 3){
			return "Wednesday";
		}
		if (w == 4){
			return "Thursday";
		}
		if (w == 5){
			return "Friday";
		}
		if (w == 6){
			return "Saturday";
		}
		return "Sunday";
	}

}
