package com.example.demo.util;

/**
 * Created by koreyoshi on 2018/1/11.
 */
public class ValidateUtils {
//    public static boolean isNullOrEmpt(Object object) {
//        if (object == null) {
//            return true;
//        }
//        if (object instanceof String) {
//            return ((String) object).length() > 0 ? false : true;
//        } else if (object instanceof Map) {
//            return ((Map) object).size() > 0 ? false : true;
//        } else if (object instanceof List) {
//            return ((List) object).size() > 0 ? false : true;
//        } else if (object instanceof JSONObject) {
//            return ((JSONObject) object).length() > 0 ? false : true;
//        } else if (object instanceof JSONArray) {
//            return ((JSONArray) object).length() > 0 ? false : true;
//        }
//        return false;
//    }
//
//    public static boolean isNullOrEmpt(Map map, String key) {
//        if (isNullOrEmpt(key)) {
//            throw new RuntimeException("isNullOrEmpt for map excption,key is null.");
//        }
//        if (isNullOrEmpt(map)) {
//            return true;
//        }
//        if (map.containsKey(key)) {
//            Object value = map.get(key);
//            return isNullOrEmpt(value);
//        } else {
//            return true;
//        }
//    }
//
//    public static boolean isNullOrEmpt(JSONObject map, String key) throws JSONException {
//        if (isNullOrEmpt(key)) {
//            throw new RuntimeException("isNullOrEmpt for map excption,key is null.");
//        }
//        if (isNullOrEmpt(map)) {
//            return true;
//        }
//        if (!map.isNull(key)) {
//            Object value = map.get(key);
//            return isNullOrEmpt(value);
//        } else {
//            return true;
//        }
//    }
}
