package com.example.demo.util;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by koreyoshi on 2018/1/11.
 */
public class DocFieldHelper {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Integer EIGHT_HOUR_SECONDS = 8 * 60 * 60 * 1000;

    public static void handleDateField(final String fieldName, Document doc) {
//        if (ValidateUtils.isNullOrEmpt(doc)) {
//            //文章为空
//            throw new RuntimeException("handleDateField excption:[doc is null].");
//        }
//
//        if (ValidateUtils.isNullOrEmpt(fieldName)) {
//            //文章为空
//            throw new RuntimeException("handleDateField excption:[the created_at is null].");
//        }
//
//        if (!doc.containsKey(fieldName)) {
//            //日期字段不存在
//            throw new RuntimeException("handleDateField excption:[the created_at value is null].");
//        }

        Object fieldValue = doc.get(fieldName);
        long timestamp = -1;
        if (fieldValue instanceof String) {
            timestamp = SystemDateFormat.parseDate((String) fieldValue);
        } else if (fieldValue instanceof Long) {
            timestamp = (Long) fieldValue;
        } else if (fieldValue instanceof Integer) {
            timestamp = Long.valueOf(fieldValue.toString()) * 1000;
//            timestamp = (Long) fieldValue;
        }
        if (timestamp == -1) {
            throw new RuntimeException("the time is wrong");
        }
        //mongo 存储datetime格式的时间，因为时区的差距，所以会少八小时。所以先在时间戳这里加上八小时就ok了。
        Date dt = new Date(timestamp + EIGHT_HOUR_SECONDS);

        doc.put(fieldName, timestamp);
        doc.put("created_at_date", dt);
    }
}
