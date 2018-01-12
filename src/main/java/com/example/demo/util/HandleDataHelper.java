package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.MongoServiceConfig;
import com.example.demo.dao.AreaMapper;
import com.example.demo.vo.AreaVo;
import com.example.demo.vo.TaskVO;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.util.HttpUtils.CONTENT_TYPE_TEXT_XML;
import static com.example.demo.util.TimeConvertUtils.chineseStandardTimeToLong;
import static com.example.demo.util.WeiboId2MidUtil.Mid2Uid;


/**
 * Created by koreyoshi on 2017/11/10.
 */
public class HandleDataHelper {
    private static final Logger logger = LoggerFactory.getLogger(HandleDataHelper.class);

    //文章中需要的字段
    private static final String[] WEIBO_TEXT_FIELDS = {"text", "reposts_count", "comments_count", "attitudes_count", "favorited", "id"};
    private static final String[] WEIBO_TEXT_FIELDS_MAPPING = {"text", "reposts_count", "comments_count", "praises_count", "isFavorite", "id"};
    //用户中需要的字段
    private static final String[] WEIBO_USER_FIELDS = {"id", "screen_name", "description", "gender", "location", "verified", "verified_type", "verified_reason", "followers_count"};
    private static final String[] WEIBO_USER_FIELDS_MAPPING = {"userid", "screen_name", "description", "sex", "location", "verify", "verified_type", "verified_reason", "followers_count"};

    //转发中的原创微博需要的字段
    private static final String[] WEIBO_REPOST_FIELDS = {"id", "text"};
    private static final String[] WEIBO_REPOST_FIELDS_MAPPING = {"father_id", "original_text"};

    //微博page_url前缀
    private static final String PAGE_URL_PREFIX = "http://weibo.com";


    public static Document handleData2Mongo(JSONObject data, TaskVO taskVO, AreaMapper areaMapper) {
        Document sendData = new Document();

        //处理文本中的字段
        for (int i = 0; i < WEIBO_TEXT_FIELDS.length; i++) {
            if (data.containsKey(WEIBO_TEXT_FIELDS[i])) {
                sendData.put(WEIBO_TEXT_FIELDS_MAPPING[i], data.get(WEIBO_TEXT_FIELDS[i]));
            }
        }
        //处理用户字段
        if (data.containsKey("user")) {
            JSONObject user = data.getJSONObject("user");
            for (int i = 0; i < WEIBO_USER_FIELDS.length; i++) {
                if (i == 0) {
                    getAreaCode(user, areaMapper, sendData);
                }
                if (user.containsKey(WEIBO_USER_FIELDS[i])) {
                    sendData.put(WEIBO_USER_FIELDS_MAPPING[i], user.get(WEIBO_USER_FIELDS[i]));
                }
            }
            //生成page_url
            if (data.containsKey("mid")) {
                String pageUrlSuffix = Mid2Uid(data.getString("mid"));
                sendData.put("page_url", PAGE_URL_PREFIX + "/" + user.get("id") + "/" + pageUrlSuffix);
            }
            //生成用户地址
            if (data.containsKey("id")) {
                sendData.put("user_page_url", PAGE_URL_PREFIX + "/u/" + user.get("id"));
            }
        }
        //处理原创信息  retweeted_status
        if (data.containsKey("retweeted_status")) {
            JSONObject original_data = data.getJSONObject("retweeted_status");
            for (int i = 0; i < WEIBO_REPOST_FIELDS.length; i++) {
                if (original_data.containsKey(WEIBO_REPOST_FIELDS[i])) {
                    sendData.put(WEIBO_REPOST_FIELDS_MAPPING[i], original_data.get(WEIBO_REPOST_FIELDS[i]));
                }
            }
            //生成转发中的原创url
            if (original_data.containsKey("mid")) {
                String pageUrlSuffix = Mid2Uid(original_data.getString("mid"));
                if (original_data.containsKey("User")) {
                    if (original_data.getJSONObject("User").containsKey("id")) {
                        sendData.put("original_url", PAGE_URL_PREFIX + "/" + original_data.getJSONObject("User").get("id") + "/" + pageUrlSuffix);
                    }
                }
            }
            sendData.put("content_type", 1);
        } else {
            sendData.put("content_type", 0);
        }
        //处理created_at字段。中国标准时间转化成long类型
        if (data.containsKey("created_at")) {
            long longTime = chineseStandardTimeToLong(data.getString("created_at"));
            sendData.put("created_at", longTime);
        }
        //过滤<a></a>标签中的source
        if (data.containsKey("source")) {
            String repDocText = data.getString("source").replaceAll("<a.*?>|</a>", "");
            sendData.put("source", repDocText);
        }
        //设置column和column1
        sendData.put("column", taskVO.getColumn());
        sendData.put("column1", taskVO.getColumn1());


        return sendData;
    }

    /**
     * 发送数据到ota项目然后入库
     *
     * @param sendMongoData
     * @param mongoServiceConfig
     * @param taskVO
     * @return
     */
    public static String sendWbdataToOta(List sendMongoData, MongoServiceConfig mongoServiceConfig, TaskVO taskVO) {
        Map<String, Object> sendDataMap = new HashMap<String, Object>(5);
        sendDataMap.put("currentHost", "192.168.0.165");
        sendDataMap.put("currentPort", "8081");
        sendDataMap.put("page_url", "xxx");
        sendDataMap.put("dictPlan", "[[]]");
        sendDataMap.put("datas", sendMongoData);

        String sendDataString = JSON.toJSONString(sendDataMap);
        String charset = "utf8";
        String content_type = CONTENT_TYPE_TEXT_XML;
        String cache_name;
        if (Integer.parseInt(taskVO.getCacheName()) == 0) {
            cache_name = mongoServiceConfig.getOtaCachename();
        } else {
            cache_name = "cache0" + taskVO.getCacheName();
        }
//        String requstUrl = "http://192.168.0.241:7080/otaapi/DataClean/cleandoc?type=insert&cacheServerName=cache01&isWeibo=true";
        String requstUrl = "http://" + mongoServiceConfig.getOtaUrl() + ":" + mongoServiceConfig.getOtaPort() + mongoServiceConfig.getOtaInterceceName() + mongoServiceConfig.getOtaInterfaceParam() + cache_name;
        String res = HttpUtils.executePost(sendDataString, charset, requstUrl, 3000 * 1000, content_type);
        System.out.println(res);
        return res;
    }

    /**
     * 获取省市的areaCode
     *
     * @param user
     * @param areaMapper
     * @param sendData
     */
    public static void getAreaCode(JSONObject user, AreaMapper areaMapper, Document sendData) {
        if (user.containsKey("province") && user.containsKey("city")) {
            int province = user.getInteger("province");
            int city = user.getInteger("city");
            logger.info(" the province id is:[ " + province + " ],the city id is:[ " + city + " ]");
            List<AreaVo> areaData = areaMapper.getAreaCodeById(province, user.getInteger("city"));
            if (areaData.size() > 0) {
                for (AreaVo areaVo : areaData) {
                    if (areaVo.getProvince() != 0 && areaVo.getCity() != 0) {
                        logger.info("name is:[ " + areaVo.getAnotherName() + " ],province_code is:[ " + areaVo.getProvince() + " ],city_code is:[ " + areaVo.getCity() + " ]");
                        sendData.put("province_code", areaVo.getProvince());
                        sendData.put("city_code", areaVo.getCity());
                    }
                }
            }
        } else if (user.containsKey("location")) {
            if (!StringUtils.isEmpty(user.getString("location"))) {
                String location = user.getString("location");
                String[] areaArr = location.split(" ");
                String areaName = areaArr[areaArr.length - 1];
                List<AreaVo> areaData = areaMapper.getAreaCode(areaName);
                if (areaData.size() > 0) {
                    for (AreaVo areaVo : areaData) {
                        if (areaVo.getProvince() != 0 && areaVo.getCity() != 0) {
                            System.out.println("name is:[ " + areaVo.getAnotherName() + " ],province_code is:[ " + areaVo.getProvince() + " ],city_code is:[ " + areaVo.getCity() + " ]");
                            sendData.put("province_code", areaVo.getProvince());
                            sendData.put("city_code", areaVo.getCity());
                        }
                    }
                }
            }
        }
    }


}
