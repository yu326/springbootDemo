package com.example.demo.web;

import com.example.demo.dao.AreaMapper;
import com.example.demo.dao.UserAreaCodeMapper;
import com.example.demo.vo.AreaVo;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by koreyoshi on 2018/1/8.
 */
@Controller
public class GetUserAreaCodeTest {
    @Autowired
    private UserAreaCodeMapper userAreaCodeMapper;
    @Autowired
    private AreaMapper areaMapper;

    @RequestMapping("yu")
    public void test() {
        String serverAdds = "120.27.195.31";
        int port = 40010;
        String dbName = "3idata";
        String tableName = "data_03";
        ServerAddress ssAddress = new ServerAddress(serverAdds, port);
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).
                threadsAllowedToBlockForConnectionMultiplier(50).build();
        MongoClient mongoClient = null;
        mongoClient = new MongoClient(ssAddress, options);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection collection = mongoDatabase.getCollection(tableName);
        BasicDBObject searchCond = new BasicDBObject();
        BasicDBList condList = new BasicDBList();
        BasicDBObject cond = new BasicDBObject();
        BasicDBObject cond1 = new BasicDBObject();
        cond.put("dataSmryInfo.column1","微博");
        cond1.put("dataSmryInfo.column","微博");
        condList.add(cond);
        condList.add(cond1);
        searchCond.put("$and", condList);
        Iterator<Document> res = collection.find(searchCond).iterator();
        while (res.hasNext()) {
            Document data = res.next();
            if(!data.containsKey("dataSmryInfo")){
                continue;
            }
            if(!(((Document)data.get("dataSmryInfo")).getString("column1").equals("微博"))){
                continue;
            }
            ArrayList datas = (ArrayList) ((Document) data.get("jsonDoc")).get("datas");

            Iterator datasIterator = datas.iterator();
            while (datasIterator.hasNext()) {
                Document oneData = (Document) datasIterator.next();
                if (oneData.containsKey("location")) {
                    System.out.println("userName is:[ " + oneData.get("screen_name") + " ],location is:[ " + oneData.get("location") + " ]");
                    getUserAreaCodeByLocation(oneData);
                }
//                System.out.println(oneData);
            }
        }
    }

    public void getUserAreaCodeByLocation(Document user) {
        if (!StringUtils.isEmpty(user.getString("location"))) {
            String location = user.getString("location");
            String[] areaArr = location.split(" ");
            String areaName = areaArr[areaArr.length - 1];
            List<AreaVo> areaData = areaMapper.getAreaCode(areaName);
            if (areaData.size() > 0) {
                for (AreaVo areaVo : areaData) {
                    if (areaVo.getProvince() != 0 && areaVo.getCity() != 0 && user.containsKey("screen_name")) {
                        System.out.println("name is:[ " + areaVo.getAnotherName() + " ],province_code is:[ " + areaVo.getProvince() + " ],city_code is:[ " + areaVo.getCity() + " ]");
//                        user.get("screen_name");
                        List regRes = userAreaCodeMapper.getOne(user.getString("screen_name"));
                        if (regRes.size() != 0) {
                            continue;
                        }
                        userAreaCodeMapper.insertOne(user.getString("screen_name"), areaVo.getProvince(), areaVo.getCity());
                    }
                }
            }
        }
    }
}
