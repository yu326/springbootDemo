package com.example.demo.service;

import com.mongodb.client.MongoCollection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by koreyoshi on 2017/12/14.
 */
public abstract class RepositoryFactory {
    private static Map<String, MongoCollection> mogoClientsMap = new HashMap<String, MongoCollection>(8);

    private static MongoCollection MONGOCOLLECTION = null;
//    public static MongoCollection getMongoClient(String dbName, String tableName, String userName, String password, final String serverAdds, final int port) throws UnknownHostException {
//        String repositoryName = dbName + "_+_" + tableName;
//        if (mogoClientsMap.containsKey(repositoryName)) {
//            return mogoClientsMap.get(repositoryName);
//        }
//        synchronized (mogoClientsMap) {
//            if (!mogoClientsMap.containsKey(repositoryName)) {
//                //MongoRepositoryFactory factory = MongoRepositoryFactory.fromDefaultDb(dbName);
//                //40000
//                //MongoRepositoryFactory.fromDefaultDb(dbName);
//                //"mongodb://localhost/" + dbName;
//                // DBCollection collection = initClient(dbName, tableName);
//                MongoCollection collection = createMongoCollection(dbName, tableName, userName, password, serverAdds, port);
//                mogoClientsMap.put(repositoryName, collection);
//                return collection;
//            } else {
//                return mogoClientsMap.get(repositoryName);
//            }
//        }
//    }
//
//    public static MongoCollection getMongoClient(String dbName, String tableName, final String serverAdds, final int port) throws UnknownHostException {
//        return getMongoClient(dbName, tableName, null, null, serverAdds, port);
//    }
//
//    public static void destoryAllClient() {
//        Iterator<String> iterator = mogoClientsMap.keySet().iterator();
//    }
//
//    public static MongoCollection createMongoCollection(String dbName, String tableName, String userName, String password, final String serverAdds, final int port) {
//        ServerAddress ssAddress = new ServerAddress(serverAdds, port);
//        //ServerAddress ssAddress = new ServerAddress("192.168.0.20", 40000);
//        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).
//                threadsAllowedToBlockForConnectionMultiplier(50).build();
//
//        List<ServerAddress> seeds = new ArrayList<ServerAddress>(1);
//        seeds.add(ssAddress);
//
//        MongoClient mongoClient = null;
//
//        if (null != userName && 0 < userName.length() && null != password && 0 < password.length()) {
//            //创建用户名密码
//            List<MongoCredential> credentials = new ArrayList<MongoCredential>(1);
//            //MongoCredential.createMongoCRCredential(userName, dbName, password.toCharArray());
//            MongoCredential credential = MongoCredential.createCredential(userName, dbName, password.toCharArray());
//            credentials.add(credential);
//            //创建安全连接
//            mongoClient = new MongoClient(seeds, credentials, options);
//        } else {
//            mongoClient = new MongoClient(ssAddress, options);
//        }
//        // 连接到数据库
//        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
//        MongoCollection collection = mongoDatabase.getCollection(tableName);
//        return collection;
//    }
//
//    public static MongoCollection creatMongodbCollectionFromMongoConfig(){
//        if(MONGOCOLLECTION == null){
//            String sourceName = DBConfigConstant.MONGO_CONFIG_PREFIX;
//            Map map = MongoServiceConfig.getConfigBySourceName(sourceName);
//            String url = (String) map.get(DBConfigConstant.URL);
//            int port = Integer.valueOf((String) map.get(DBConfigConstant.PORT));
//            String dbName = (String) map.get(DBConfigConstant.DBNAME);
//            String table = (String) map.get(DBConfigConstant.TABLE);
//
//            try {
//                MONGOCOLLECTION = RepositoryFactory.getMongoClient(dbName,table,url,port);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//        }else{
//            return MONGOCOLLECTION;
//        }
//        return MONGOCOLLECTION;
//    }
}
