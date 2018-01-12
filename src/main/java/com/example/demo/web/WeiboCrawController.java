package com.example.demo.web;

import com.example.demo.domain.BaseResult;
import com.example.demo.service.Impl.WeiboServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by koreyoshi on 2017/12/13.
 */
@RestController
public class WeiboCrawController {

    private static final Logger logger = LoggerFactory.getLogger(WeiboCrawController.class);

    @Autowired
    private WeiboServiceImpl weiboService;

    @GetMapping(value = "executeTask")
    public BaseResult executeTask() {
        logger.info("here is beginning~~");
        BaseResult response = new BaseResult();
        try {
            response = response.success();
            weiboService.execute();
        } catch (Exception ex) {
            response = response.failure("exception: " + ex.getMessage());
            logger.error("executeTask Error : [ " + ex.getMessage() + " ]");
        } finally {
            return response;
        }
    }
}
