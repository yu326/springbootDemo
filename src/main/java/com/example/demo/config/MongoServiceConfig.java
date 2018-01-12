package com.example.demo.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


/**
 * Created by koreyoshi on 2017/11/9.
 */
public class MongoServiceConfig {

    private static Resource resource = new ClassPathResource("/mongoService.properties");

    private static MongoServiceConfig config;


    private static String OTA_URL;
    private static String OTA_PORT;
    private static String OTA_INTERCECE_NAME;
    private static String OTA_INTERFACE_PARAM;
    private static String OTA_CACHENAME;


    public static void main(String[] args) {
        config.getConfig();
    }

    /**
     * 获取配置文件中 ota 的信息
     *
     * @return
     */
    public static MongoServiceConfig getConfig() {
        synchronized (MongoServiceConfig.class) {
            if (config != null) {
                return config;
            }
        }
        try {
            config = new MongoServiceConfig();
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            Iterator it = props.keySet().iterator();
            String key;
            Object data;
            while (it.hasNext()) {
                key = (String) it.next();
                if (key.contains("otaUrl")) {
                    OTA_URL = (String) props.get(key);
                } else if (key.contains("otaPort")) {
                    OTA_PORT = (String) props.get(key);
                } else if (key.contains("otaInterfaceceName")) {
                    OTA_INTERCECE_NAME = (String) props.get(key);
                } else if (key.contains("otaInterfaceParam")) {
                    OTA_INTERFACE_PARAM = (String) props.get(key);
                } else if (key.contains("otaCacheName")) {
                    OTA_CACHENAME = (String) props.get(key);
                } else if (key.contains("mongo")) {
                    getKeyAndPutCacheMap(key, (String) props.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    //获取di.mongo1.url 中的mongo1作为一级map中key值，url为嵌套map中key值
    public static void getKeyAndPutCacheMap(String key, Object value) {
        int start = key.indexOf(".") + 1;
        int middle = key.lastIndexOf(".");
        String mapKey = key.substring(start, middle);

        int end = key.length();
        String nestMapKey = key.substring(middle + 1, end);
        if (MongoServiceConfig.containsKey(mapKey)) {
            HashMap nestMap = (HashMap) MongoServiceConfig.get(mapKey);
            nestMap.put(nestMapKey, value);
        } else {
            HashMap map = new HashMap<String, String>();
            map.put(nestMapKey, value);
            MongoServiceConfig.put(mapKey, map);
        }
    }

    //mongoConfigCacheMap
    private static Map<String, Map<String, String>> MongoServiceConfig = new HashMap<String, Map<String, String>>(4);

    public String getOtaUrl() {
        return OTA_URL;
    }

    public String getOtaPort() {
        return OTA_PORT;
    }

    public String getOtaInterceceName() {
        return OTA_INTERCECE_NAME;
    }

    public String getOtaInterfaceParam() {
        return OTA_INTERFACE_PARAM;
    }

    public String getOtaCachename() {
        return OTA_CACHENAME;
    }

    public static Map getConfigBySourceName(String sourceName) {
        Map rs = MongoServiceConfig.get(sourceName);
        if (rs != null) {
            return rs;
        }
        return rs;
    }
}
