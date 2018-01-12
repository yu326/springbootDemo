package com.example.demo.Test;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Iterator;

/**
 * Created by koreyoshi on 2017/12/14.
 */
public class MongodbConnTest {
    @org.junit.Test
    public void insertTest() {
        String serverAdds = "120.27.195.31";
        int port = 40010;
        String serverAdds1 = "127.0.0.1";
        int port1 = 40000;
        String dbName = "3idata";
        String dbName1 = "3idata";
        String tableName = "v3_data_03";
        String tableName1 = "v3_data_03";
        ServerAddress ssAddress = new ServerAddress(serverAdds, port);
        ServerAddress ssAddress1 = new ServerAddress(serverAdds1, port1);

        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).
                threadsAllowedToBlockForConnectionMultiplier(50).build();

        MongoClient mongoClient = null;

        mongoClient = new MongoClient(ssAddress, options);

//        mongoClient.close();

        MongoClient mongoClient1 = new MongoClient(ssAddress1, options);
//        mongoClient1.close();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoDatabase mongoDatabase1 = mongoClient1.getDatabase(dbName1);
        MongoCollection collection = mongoDatabase.getCollection(tableName);
        MongoCollection collection1 = mongoDatabase1.getCollection(tableName1);

        //条件组合
        BasicDBObject searchCond = new BasicDBObject();
        BasicDBList condList = new BasicDBList();
        BasicDBObject cond1 = new BasicDBObject();
        cond1.put("dataSmryInfo.column", "戴森");
        BasicDBObject cond = new BasicDBObject();
        cond.put("dataSmryInfo.column1", "今日头条");
        condList.add(cond);
        condList.add(cond1);

        searchCond.put("$and", condList);


//        long totalNum = collection.count(searchCond);
//        System.out.println(totalNum);


        Iterator<Document> res = collection.find(searchCond).iterator();

        Document taskData1;
        int i = 0;
        while (res.hasNext()) {
            if (i == 200) {
                break;
            }
            taskData1 = (Document) res.next();
            taskData1.remove("_id");
            collection1.insertOne(taskData1);
            i++;
        }
        System.out.println("ending~~");
        mongoClient.close();
        mongoClient1.close();
//        Document mogoDbBean = new Document();
//        mogoDbBean.put("name","yu");
//        mogoDbBean.put("age",25);
//        mogoDbBean.put("desc","姚馥清时醉，边韶白日眠");
//        collection.insertOne(mogoDbBean);
    }

    @org.junit.Test
    public void selectTest() {
//        String serverAdds = "127.0.0.1";
//        int port = 40000;
//        String dbName = "yu";
//        String tableName = "test_yu";
//        ServerAddress ssAddress = new ServerAddress(serverAdds, port);
//        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).
//                threadsAllowedToBlockForConnectionMultiplier(50).build();
//
//        MongoClient mongoClient = null;
//
//        mongoClient = new MongoClient(ssAddress, options);
//
//        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
//        MongoCollection collection = mongoDatabase.getCollection(tableName);

//        Bson fileter1 = Filters.eq("name", "yu"); //没有入库
//        Bson fileter2 = Filters.eq("age", 25);//没有分词
//        Bson conds = Filters.and(fileter1, fileter2);
//        Bson conds = Filters.eq("name","yu");
//        BasicDBObject basicDBObject = new BasicDBObject();
//        BasicDBList basicDBList  = new BasicDBList();
//        BasicDBObject cond = new BasicDBObject();
//        BasicDBObject cond1 = new BasicDBObject();
//        cond.put("name","yu");
//        cond1.put("age",25);
////        cond.put("name",new BasicBSONObject("$regex", Pattern.compile("yu")));
//        basicDBList.add(cond);
//        basicDBList.add(cond1);
//        basicDBObject.put("$and",basicDBList);
//
//        FindIterable iterable = collection.find(basicDBObject);
//        Iterator<Document> iterator = iterable.iterator();
//        Document taskData = null;
//        while (iterator.hasNext()) {
//            taskData = iterator.next();
//            System.out.println(taskData.toString());
//        }
    }
}
