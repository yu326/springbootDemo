package com.example.demo.web;

import com.example.demo.domain.BaseResult;
import com.example.demo.service.Impl.TaskScheduleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by koreyoshi on 2017/12/22.
 */
@Controller
public class CreateTaskController {

    private static final Logger logger = LoggerFactory.getLogger(CreateTaskController.class);
    @Autowired
    private TaskScheduleServiceImpl taskScheduleService;
    @GetMapping("createTask")
    public String createTask(@RequestParam("startTime") int startTime, @RequestParam("endTime") int endTime){
        BaseResult response = new BaseResult();
        try {
            response = response.success();
            taskScheduleService.execute(startTime,endTime);
        } catch (Exception ex) {
            response = response.failure("exception: " + ex.getMessage());
            logger.error("CreateTaskController Error : [ " + ex.getMessage() + " ]");
        } finally {
            return response.toString();
        }
    }
}
