package com.park.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 时间格式转换工具类
 * Created by Park on 2018-12-2.
 */
public class DateTimeUtil {
    public static  final  String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 字符串转换成日期格式
     * @param dateStr
     * @param formateStr
     * @return
     */
    public static Date strToDate(String dateStr,String formateStr){
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(formateStr);
        DateTime dateTime = dateTimeFormat.parseDateTime(dateStr);
        return dateTime.toDate();
    }
    public static Date strToDate(String dateStr){
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormat.parseDateTime(dateStr);
        return dateTime.toDate();
    }

    /**
     * 日期转换成字符串
     * @param date
     * @param formateStr
     * @return
     */
    public static  String dateToString(Date date , String formateStr){
        if(date == null){
        return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formateStr);
    }
    public static  String dateToString(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
}
