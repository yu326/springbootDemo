package com.example.demo.entity;

import lombok.Data;

/**
 * Created by koreyoshi on 2017/12/27.
 */
@Data
public class taskHistoryEntity {
    public int id;
    public int taskType;
    public int task;
    public int tasklevel;
    public int timeout;
    public int startTime;
    public int endTime;
    public int taskStatus;
    public int dataStatus;
    public String taskparams;
    public String remarks;
    public int tenantid;
    public int userId;
    public int errorCode;
    public String errorMsg;
    public int projectId;

}
