package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

/**
 * Created by koreyoshi on 2017/12/13.
 */
@Component("MongoQueryService")
public class MongoQueryService {
    @Autowired
    private MongoOperations mongoOperations;


}
