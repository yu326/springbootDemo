package com.example.demo.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.TaskMapper;
import com.example.demo.entity.TaskScheduleEntity;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by koreyoshi on 2017/12/13.
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;

    public void createKeyWordsTask(TaskScheduleEntity taskScheduleEntity,int startTime ,int endTime){
        JSONObject taskParam = (JSONObject) JSONObject.parse(taskScheduleEntity.getTaskparams());
        taskParam.put("starttime",startTime);
        taskParam.put("endtime",endTime);
        taskMapper.createKeyWordsTask(taskScheduleEntity.getTaskType(),taskScheduleEntity.getTask(),taskScheduleEntity.getTasklevel(),taskScheduleEntity.getTaskstatus(),taskParam.toString(),taskScheduleEntity.getRemarks(),taskScheduleEntity.getProjectId());
    }
    public void createAccountTask(TaskScheduleEntity  taskScheduleEntity){
        taskScheduleEntity.setTaskstatus(0);
        taskMapper.createKeyWordsTask(taskScheduleEntity.getTaskType(),taskScheduleEntity.getTask(),taskScheduleEntity.getTasklevel(),taskScheduleEntity.getTaskstatus(),taskScheduleEntity.getTaskparams(),taskScheduleEntity.getRemarks(),taskScheduleEntity.getProjectId());
    }
}
