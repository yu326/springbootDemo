package com.example.demo.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.sdk.Yu;
import com.example.demo.sdk.model.WeiboException;

/**
 * Created by koreyoshi on 2017/12/13.
 */
public class Test {

    @org.junit.Test
    public void test() throws WeiboException {
        String access_token = "2.00SdyOsBnp71ED6984ef87900ZfM2B";
        Yu um = new Yu(access_token);
        String uId = "3482893280";
        int page  = 1;
        int count = 20;
        String response = um.getUserWeiboListById(uId,count,page);
//        String response =         um.getDataByKeyWord();
        JSONObject data = JSONObject.parseObject(response);
        JSONArray datas = (JSONArray) data.get("statuses");

    }
}
