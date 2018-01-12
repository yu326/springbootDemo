package com.example.demo.service.Impl;

import com.example.demo.dao.TaskScheduleMapper;
import com.example.demo.entity.TaskScheduleEntity;
import com.example.demo.service.TaskScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by koreyoshi on 2017/12/21.
 */
@Service("createCurrentTask")
public class TaskScheduleServiceImpl implements TaskScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(TaskScheduleServiceImpl.class);
    //默认时间间隔12H + 10S
    private static final int TWELVE_HOURS_SECOND = 12 * 3600 + 10;

    @Autowired
    private TaskScheduleMapper taskScheduleMapper;
    @Autowired
    private TaskServiceImpl taskServiceImpl;

    public void execute() {
        int endTime = (int) (System.currentTimeMillis() / 1000);
        int startTime = endTime - TWELVE_HOURS_SECOND;
        execute(startTime, endTime);
    }

    public void execute(int startTime, int endTime) {
        try {
            int currentTime = (int) (System.currentTimeMillis() / 1000);
            if (endTime > currentTime) {
                endTime = currentTime;
            }
            List<TaskScheduleEntity> taskScheduleData = taskScheduleMapper.createCurrentTask();
            if (taskScheduleData.size() == 0) {
                logger.info("没有等待执行的任务");
            }
            for (TaskScheduleEntity taskScheduleEntity : taskScheduleData) {
                int taskType = taskScheduleEntity.getTask();
                //获取某条微博的详细信息（长文本微博）
                if (taskType == 1) {
                    // TODO: 2017/12/21 暂时不需要
                } else if (taskType == 2) { //账号
                    taskServiceImpl.createAccountTask(taskScheduleEntity);
                } else if (taskType == 3) { //抓取转发
                    // TODO: 2017/12/21 暂时不需要
                } else if (taskType == 4) { //抓取关键词
                    if (endTime - startTime <= TWELVE_HOURS_SECOND) {
                        logger.info("one way: the startTime is:[ " + startTime + " ],endTime is:[ " + endTime + "]");
                        taskServiceImpl.createKeyWordsTask(taskScheduleEntity, startTime, endTime);
                    } else {
                        int tmpstartTime = 0;
                        int tmpEndTime = 0;
                        while (true) {
                            if (tmpstartTime == 0) {
                                tmpEndTime = startTime + TWELVE_HOURS_SECOND;
                                tmpstartTime = startTime;
                            } else {
                                tmpEndTime = tmpstartTime + TWELVE_HOURS_SECOND;
                            }
                            logger.info("two way: the startTime is:[ " + tmpstartTime + " ],endTime is:[ " + tmpEndTime + "]");
                            taskServiceImpl.createKeyWordsTask(taskScheduleEntity, tmpstartTime, tmpEndTime);
                            tmpstartTime = tmpEndTime;
                            if (tmpstartTime > endTime) {
                                break;
                            }
                        }
                    }
                }
                currentTime = (int) (System.currentTimeMillis() / 1000);
                taskScheduleMapper.updateTaskInfos(currentTime, taskScheduleEntity.getId());
            }
        } catch (Exception exception) {
            logger.error("exception : [ " + exception.getMessage() + " ]");
        }
    }
}

