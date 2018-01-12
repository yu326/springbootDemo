package com.example.demo.service;

import com.example.demo.config.MongoServiceConfig;
import com.example.demo.sdk.Yu;
import com.example.demo.vo.TaskVO;

import java.util.List;

/**
 * Created by koreyoshi on 2017/12/13.
 */
public interface WeiboService {

    public void execute();


    public void handLongTextWeibo(TaskVO taskVO, Yu yuClass);

    public void storageData(List sendMongoData, MongoServiceConfig mongoServiceConfig, TaskVO taskVO);

    public void WeiboListByKeyword(TaskVO taskVO, Yu yuClass);
}
