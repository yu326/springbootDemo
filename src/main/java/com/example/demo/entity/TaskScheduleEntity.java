package com.example.demo.entity;

import lombok.Data;

/**
 * Created by koreyoshi on 2017/12/21.
 */
@Data
public class TaskScheduleEntity {
    private int id;

    private int task;
    private int tasklevel;
    private int timeout;
    private int taskstatus;
    private String taskparams;
    private String remarks;

    private int taskType;
    private int startTime;
    private int endTime;
    private int tenantId;
    private int userId;
    private int errorCode;
    private String errorMsg;
    private String crondTime;
    private int lastUpdateTime;
    private int projectId;

}
