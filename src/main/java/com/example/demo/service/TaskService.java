package com.example.demo.service;

import com.example.demo.entity.TaskScheduleEntity;
import org.springframework.stereotype.Service;

/**
 * Created by koreyoshi on 2017/12/13.
 */
@Service
public interface TaskService {
    public void createKeyWordsTask(TaskScheduleEntity taskScheduleEntity,int startTime ,int endTime);
    public void createAccountTask(TaskScheduleEntity  taskScheduleEntity);

}
