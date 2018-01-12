package com.example.demo.web;

import com.example.demo.dao.UserAreaCodeMapper;
import com.example.demo.vo.UserAreaCodeVO;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Iterator;
import java.util.List;

/**
 * Created by koreyoshi on 2018/1/9.
 */
@Controller
public class UpdateDocAreaCodeController {
    @Autowired
    private UserAreaCodeMapper userAreaCodeMapper;

    @GetMapping("yi")
    public void test() {
        String serverAdds = "120.27.195.31";
        int port = 40010;
        String dbName = "v3data";
        String tableName = "v3_data";
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
        cond.put("column", "西门子中国");
        cond1.put("province_code", null);
        condList.add(cond);
        condList.add(cond1);
        searchCond.put("$and", condList);
        Iterator<Document> res = collection.find(searchCond).iterator();
        int i = 0;
        while (res.hasNext()) {
            Document data = res.next();


            String screenName = data.getString("original_screen_name");
            List<UserAreaCodeVO> regRes = userAreaCodeMapper.getOne(screenName);

            if (regRes.size() != 0) {
                ObjectId id = data.getObjectId("_id");
                BasicDBObject query = new BasicDBObject();
                query.put("_id", id);
                BasicDBObject keyValue = new BasicDBObject();
                keyValue.put("province_code", regRes.get(0).getProvinceCode());
                keyValue.put("city_code", regRes.get(0).getCityCode());
                BasicDBObject update = new BasicDBObject("$set", keyValue);
                UpdateResult updateResult = collection.updateOne(query, update);
                System.out.println("id is:[ " + id + " ],screenName is:[ " + screenName + " ],the updateRes is:[ " + updateResult + " ]");

            } else {
//                System.out.println("false");
                continue;
            }


        }
    }
}
