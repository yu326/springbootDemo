package com.example.demo.Test;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BasicBSONObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by koreyoshi on 2018/1/11.
 */
public class UpdateMongodbDateFormat {
    @org.junit.Test
    public void test() {
        String serverAdds = "120.27.195.31";
        int port = 40010;
        String dbName = "3idata";
        String tableName = "data_04";
        ServerAddress ssAddress = new ServerAddress(serverAdds, port);

        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).
                threadsAllowedToBlockForConnectionMultiplier(50).build();

        MongoClient mongoClient = null;

        mongoClient = new MongoClient(ssAddress, options);

//        mongoClient.close();

//        mongoClient1.close();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection collection = mongoDatabase.getCollection(tableName);

        //条件组合
        BasicDBList finalCondList = new BasicDBList();

        BasicDBObject searchCond = new BasicDBObject();
        BasicDBList condList = new BasicDBList();
        BasicDBList childCond = new BasicDBList();
        childCond.add("西门子家电-西门子家电官方旗舰店");
        childCond.add("西门子家电-西门子百诚专卖店");


        long startTime = 1505491200000L;
//        cond1.put("cacheDataTime", new BasicDBObject("$gte", startTime));
        BasicDBObject cond = new BasicDBObject();
        cond.put("dataSmryInfo.column1", "天猫");
        condList.add(cond);
        BasicDBObject cond1 = new BasicDBObject();
        cond1.put("dataSmryInfo.column", new BasicBSONObject("$in", childCond));
        condList.add(cond1);
        BasicDBObject cond2 = new BasicDBObject();
        cond2.put("cacheDataTime", new BasicDBObject("$gte", startTime));
        condList.add(cond2);


        searchCond.put("$and", condList);


        long totalNum = collection.count(searchCond);
        System.out.println(totalNum);


        Iterator<Document> res = collection.find(searchCond).iterator();

        Document taskData1;

        while (res.hasNext()) {

            taskData1 = (Document) res.next();
            if(taskData1.containsKey("jsonDoc")){
                Document jsonDoc = (Document) taskData1.get("jsonDoc");
                if(jsonDoc.containsKey("datas")){
                    ArrayList docDatas = (ArrayList) jsonDoc.get("datas");

                }
            }

        }
        System.out.println("ending~~");
    }
}
