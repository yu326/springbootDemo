package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by koreyoshi on 2017/11/10.
 */
public class TimeConvertUtils {
    private static final SimpleDateFormat CHINESE_STANDARD_TIME_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    private static final SimpleDateFormat NORMAL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 中国标准时间转化成long
     *
     * @param waitConverTime
     * @return
     */
    public static long chineseStandardTimeToLong(String waitConverTime) {
        long converTime = -1;
        String dateTimeString = chineseStandardTimeToStringDate(waitConverTime);
        try {
            converTime = NORMAL_DATE_FORMAT.parse(dateTimeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converTime;
    }

    /**
     * 中国标准时间转化成正常date格式
     *
     * @param waitConverTime
     * @return
     */
    public static String chineseStandardTimeToStringDate(String waitConverTime) {
        long converTime = -1;
        String dateTimeString = null;
        try {
            dateTimeString = NORMAL_DATE_FORMAT.format(CHINESE_STANDARD_TIME_FORMAT.parse(waitConverTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeString;
    }


}
