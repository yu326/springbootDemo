package com.example.demo.vo;

import lombok.Data;

/**
 * Created by koreyoshi on 2017/12/13.
 */
@Data
public class TaskVO {

    private int id;
    private int task;
    private String taskparams;
    private int startTime;
    private int endTime;
    private String errorMsg;
    private int dataStatus;
    private String remarks;
    private int projectId;
    //@JsonProperty("cache_name")
    private String cacheName;
    private String column;
    private String column1;
    //@JsonProperty("access_token")
    private String accessToken;
    private int tasklevel;


}
