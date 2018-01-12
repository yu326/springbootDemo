package com.example.demo.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.MongoServiceConfig;
import com.example.demo.dao.AreaMapper;
import com.example.demo.dao.TaskHistoryMapper;
import com.example.demo.dao.TaskMapper;
import com.example.demo.sdk.Yu;
import com.example.demo.sdk.model.WeiboException;
import com.example.demo.service.WeiboService;
import com.example.demo.util.ArrayHandleUtils;
import com.example.demo.util.TaskAdapter;
import com.example.demo.vo.TaskVO;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.util.HandleDataHelper.handleData2Mongo;
import static com.example.demo.util.HandleDataHelper.sendWbdataToOta;

/**
 * Created by koreyoshi on 2017/12/13.
 */
@Service("executeCurrentTask")
public class WeiboServiceImpl implements WeiboService {
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskHistoryMapper taskHistoryMapper;

    private static final Logger logger = LoggerFactory.getLogger(WeiboServiceImpl.class);
    //ota 配置文件
    private MongoServiceConfig mongoServiceConfig = MongoServiceConfig.getConfig();
    //获取长文本微博id数量限制
    private static final int WEIBO_LONGTEXT_INTERFACE_IDS_NUM_LIMIT = 50;

    public void execute() {
        logger.info("WeiboServiceImpl execute task is begin!");
        List<TaskVO> taskInfosList = taskMapper.getTaskInfosById();
        if (taskInfosList.size() == 0) {
            logger.info("WeiboServiceImpl execute : [ no task to execute! ]");
        } else {
            Yu yuClass = null;
            for (TaskVO taskVO : taskInfosList) {
                taskVO.setStartTime((int) (System.currentTimeMillis() / 1000));
                try {
                    int taskType = taskVO.getTask();
                    if (yuClass == null) {
                        String accessToken = taskVO.getAccessToken();
                        yuClass = new Yu(accessToken);
                    }
                    //获取某条微博的详细信息（长文本微博）
                    if (taskType == 1) {
                        handLongTextWeibo(taskVO, yuClass);
                    } else if (taskType == 2) { //账号
                        weiboListByUserId(taskVO, yuClass);
                    } else if (taskType == 3) { //抓取转发

                    } else if (taskType == 4) { //抓取关键词
                        WeiboListByKeyword(taskVO, yuClass);
                    }
                    taskVO.setEndTime((int) (System.currentTimeMillis() / 1000));
                    boolean insertRes = taskHistoryMapper.insertTaskHistory(taskVO.getId(), taskVO.getTask(), taskVO.getTasklevel(), taskVO.getStartTime(), taskVO.getEndTime(), 1, taskVO.getDataStatus(), taskVO.getTaskparams(), taskVO.getRemarks(), taskVO.getProjectId());
                    if(insertRes){
                        taskMapper.deleteOne(taskVO.getId());
                    }else{
                        TaskAdapter.completeTask(taskVO,taskMapper);
                    }
                } catch (Exception exception) {
                    TaskAdapter.exceptionTask(taskVO, taskMapper);
                    logger.error("WeiboServiceImpl execute Error : [ " + exception.getMessage() + " ]");
                }
            }
        }
        logger.info("WeiboServiceImpl execute task is end!");
    }


    public void handLongTextWeibo(TaskVO taskVO, Yu yuClass) {
        String weiboData = null;
        List sendMongoData = new ArrayList();

        String taskParam = taskVO.getTaskparams();
        JSONObject taskparamJson = (JSONObject) JSONObject.parse(taskParam);
        String ids = taskparamJson.getString("ids");
        JSONArray idsArrs = (JSONArray) JSONArray.parse(ids);

        int trimUser = 0;
        int isGetLongText = 1;

        if (idsArrs.size() < WEIBO_LONGTEXT_INTERFACE_IDS_NUM_LIMIT) { //接口传入的id最多为50个，故在此做判断，大于50分割。
            String[] idsStrArr = (String[]) idsArrs.toArray(new String[idsArrs.size()]);
            ids = String.join(",", idsStrArr);
            try {
                weiboData = yuClass.getDetailedWeiboData(ids, trimUser, isGetLongText);
            } catch (WeiboException e) {
                e.printStackTrace();
            }
            JSONObject data = JSONObject.parseObject(weiboData);
            JSONArray datas = (JSONArray) data.get("statuses");
            Iterator iterator = datas.iterator();
            while (iterator.hasNext()) {
                JSONObject oneData = (JSONObject) iterator.next();
//                getAllWeiboTextById(yuClass, oneData, 0);
                Document sendData = handleData2Mongo(oneData, taskVO,areaMapper);
                sendMongoData.add(sendData);
            }
            storageData(sendMongoData, mongoServiceConfig, taskVO);
        } else {
//            拆分数据，接口限定50条,
            ArrayList newIdsArr = (ArrayList) ArrayHandleUtils.arrayChunk(idsArrs, WEIBO_LONGTEXT_INTERFACE_IDS_NUM_LIMIT);
            for (int i = 0; i < newIdsArr.size(); i++) {
                ArrayList idsArr = (ArrayList) newIdsArr.get(i);
                String idsStr = String.join(",", idsArr);
                try {
                    weiboData = yuClass.getDetailedWeiboData(idsStr, trimUser, isGetLongText);
                } catch (WeiboException e) {
                    e.printStackTrace();
                }
                JSONObject data = JSONObject.parseObject(weiboData);
                JSONArray datas = (JSONArray) data.get("statuses");
                Iterator iterator = datas.iterator();
                while (iterator.hasNext()) {
                    JSONObject oneData = (JSONObject) iterator.next();
//                    getAllWeiboTextById(yuClass, oneData, 0);
                    Document sendData = handleData2Mongo(oneData, taskVO,areaMapper);
                    sendMongoData.add(sendData);
                }
                storageData(sendMongoData, mongoServiceConfig, taskVO);
            }
        }
    }

    /**
     * 根据微博id 获取全部文本并返回
     *
     * @param weiboId 微博id
     * @param yuClass 微博实体类
     * @return 返回全部文本
     */
    public String handLongTextWeiboText(String weiboId, Yu yuClass) {
        int trimUser = 1;
        int isGetLongText = 1;
        String weiboData = null;

        try {
            weiboData = yuClass.getDetailedWeiboData(weiboId, trimUser, isGetLongText);
        } catch (WeiboException e) {
            logger.error("WeiboServiceImpl handLongTextWeiboText Error : [ " + e.getMessage() + " ]");
        }
        JSONObject data = JSONObject.parseObject(weiboData);
        JSONArray datas = (JSONArray) data.get("statuses");
        return ((JSONObject) ((JSONObject) datas.get(0)).get("longText")).getString("longTextContent");
    }


    public void weiboListByUserId(TaskVO taskVO, Yu yuClass) {
        String taskParam = taskVO.getTaskparams();
        JSONObject taskparamJson = (JSONObject) JSONObject.parse(taskParam);
        String userIds = taskparamJson.getString("users");
        JSONArray userIdArr = (JSONArray) JSONArray.parse(userIds);

        int totalDatanum = 0;
        int page = 1;
        int count = 200;
        List eliminateWords = new ArrayList();
        if (taskparamJson.containsKey("eliminate_words") && !StringUtils.isEmpty(taskparamJson.get("eliminate_words"))) {
            eliminateWords = (List) taskparamJson.get("eliminate_words");
        }
        if (taskparamJson.containsKey("count")) {
            count = Integer.parseInt(taskparamJson.getString("count"));
        }
        for (Object uid : userIdArr) {
            List sendMongoData = new ArrayList();
            String StringUid = String.valueOf(uid);
            String response = null;
            try {
                response = yuClass.getUserWeiboListById(StringUid, count, page);
            } catch (WeiboException e) {
                logger.error("WeiboServiceImpl weiboListByUserId Error : [ " + e.getMessage() + " ], uid is: [ " + uid + " ] ");
            }

            JSONObject data = JSONObject.parseObject(response);
            JSONArray datas = (JSONArray) data.get("statuses");
            Iterator iterator = datas.iterator();
            while (iterator.hasNext()) {
                JSONObject oneData = (JSONObject) iterator.next();
                //获取长文本微博
//                getAllWeiboTextById(yuClass, oneData, 1);
//                if (eliminateWords.size() != 0) {
//                    //微博剔除词匹配
//                    boolean filterRes = filterWeiboDataByEliminateWords(oneData.getString("text"), eliminateWords);
//                    if (filterRes) {
//                        continue;
//                    }
//                }
                totalDatanum++;
                Document sendData = handleData2Mongo(oneData, taskVO,areaMapper);
                sendMongoData.add(sendData);
            }
            if (sendMongoData.size() != 0) {
                storageData(sendMongoData, mongoServiceConfig, taskVO);
            }
        }
        taskVO.setDataStatus(totalDatanum);
    }

    /**
     * 数据存储（暂支持两种 0-ota,1-mongodb）
     *
     * @param sendMongoData      数据
     * @param mongoServiceConfig mongodb配置（针对ota的）
     * @param taskVO             任务参数
     */
    public void storageData(List sendMongoData, MongoServiceConfig mongoServiceConfig, TaskVO taskVO) {
        String taskParam = taskVO.getTaskparams();
        JSONObject taskparamJson = (JSONObject) JSONObject.parse(taskParam);
        int storageType = 0;
        if (taskparamJson.containsKey("storageType")) {
            storageType = taskparamJson.getInteger("storageType");
        }
        if (storageType == 0) {
            String res = sendWbdataToOta(sendMongoData, mongoServiceConfig, taskVO);
        } else if (storageType == 1) {
//            MongoCollection mongoCollection = RepositoryFactory.creatMongodbCollectionFromMongoConfig();
//            mongoCollection.insertMany(sendMongoData);
        } else {
            logger.error("WeiboServiceImpl storageData Error : [ Unsupported storageType:" + storageType + " ]");
        }
    }

    /**
     * 搜索关键词接口，返回包含关键词的微博数据
     *
     * @param taskVO  任务参数
     * @param yuClass 微博实体类
     */
    public void WeiboListByKeyword(TaskVO taskVO, Yu yuClass) {
        String taskParam = taskVO.getTaskparams();
        JSONObject taskparamJson = (JSONObject) JSONObject.parse(taskParam);

        int dup = 0;
        int antispam = 0;
        int count = 50;
        int page = 1;
        long starttime = 0;
        long endtime = 0;
        int totalDatanum = 0;
        JSONArray keyWords = null;
        List eliminateWords = new ArrayList();
        if (taskparamJson.containsKey("dup")) {
            dup = taskparamJson.getInteger("dup");
        }
        if (taskparamJson.containsKey("antispam")) {
            antispam = taskparamJson.getInteger("antispam");
        }
        if (taskparamJson.containsKey("count")) {
            count = taskparamJson.getInteger("count");
        }
        if (taskparamJson.containsKey("page")) {
            page = taskparamJson.getInteger("page");
        }
        if (taskparamJson.containsKey("keyWord")) {
            keyWords = (JSONArray) taskparamJson.get("keyWord");
        }else{
            logger.error("WeiboServiceImpl keyWords Error : [ keyWords is null ] ");
            throw new RuntimeException("WeiboServiceImpl keyWords Error : [ keyWords is null ] ");
        }
        if (taskparamJson.containsKey("eliminate_words") && !StringUtils.isEmpty(taskparamJson.get("eliminate_words"))) {
            eliminateWords = (List) taskparamJson.get("eliminate_words");
        }
        if (taskparamJson.containsKey("starttime")) {
            starttime = taskparamJson.getLong("starttime");
        }
        if (taskparamJson.containsKey("endtime")) {
            endtime = taskparamJson.getLong("endtime");
        }

        for (Object keyWord : keyWords) {
            page = 1;
            while (true) {
                List sendMongoData = new ArrayList();
                String response = null;
                try {
                    response = yuClass.getDataByKeyWord((String) keyWord, dup, antispam, count, starttime, endtime, page);
                } catch (WeiboException e) {
                    logger.error("WeiboServiceImpl WeiboListByKeyword Error : [ " + e.getMessage() + " ], keyWord is: [ " + keyWord + " ] ");
                }
                JSONObject data = JSONObject.parseObject(response);
                JSONArray datas = (JSONArray) data.get("statuses");
                if (datas.size() == 0) {
                    break;
                }
                Iterator iterator = datas.iterator();
                while (iterator.hasNext()) {
                    JSONObject oneData = (JSONObject) iterator.next();
                    //长文本微博处理
//                    getAllWeiboTextById(yuClass, oneData, 1);
                    if (eliminateWords.size() != 0) {
                        //微博剔除词匹配
                        boolean filterRes = filterWeiboDataByEliminateWords(oneData.getString("text"), eliminateWords);
                        if (filterRes) {
                            continue;
                        }
                    }
                    totalDatanum++;
                    Document sendData = handleData2Mongo(oneData, taskVO,areaMapper);
                    sendMongoData.add(sendData);
                }
                if (sendMongoData.size() != 0) {
                    storageData(sendMongoData, mongoServiceConfig, taskVO);
                }
                page++;
            }
        }
        taskVO.setDataStatus(totalDatanum);
    }

    /**
     * 判断是否是长文本微博，如果是获取某条的全部数据的则不需要再次调用。不是则调用接口，拿到全部的文本
     *
     * @param yuClass      微博实体类
     * @param data         数据
     * @param isNeedToCraw 是否需要去获取全部文本
     */
    public void getAllWeiboTextById(Yu yuClass, JSONObject data, int isNeedToCraw) {
        if (data.getBoolean("isLongText") == false) {
            return;
        }
        if (isNeedToCraw == 1) {
            String allText = handLongTextWeiboText(data.getString("id"), yuClass);
            data.put("text", allText);
        } else if (isNeedToCraw == 0) {
            String allText = ((JSONObject) data.get("longText")).getString("longTextContent");
            data.put("text", allText);
        }

    }

    /**
     * 正则匹配文本中list中的字符串，返回
     *
     * @param content        文本
     * @param eliminateWords 匹配词列表
     * @return boolean 匹配结果
     */
    public boolean filterWeiboDataByEliminateWords(String content, List eliminateWords) {
        String regex = String.join("|", eliminateWords);
        String re = ".*(" + regex + ").*";
        Pattern p = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(content);
        boolean res = matcher.find();
        return res;
    }
}
