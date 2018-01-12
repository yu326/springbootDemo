package com.example.demo.domain;

import lombok.Data;

/**
 * Created by koreyoshi on 2017/12/13.
 */
@Data
public class BaseResult {
    private boolean success;
    private String message;

    public static BaseResult success() {
        BaseResult baseResult = new BaseResult();
        baseResult.setSuccess(true);
        baseResult.setMessage("");
        return baseResult;
    }
    public static BaseResult failure(String message) {
        BaseResult baseResult = new BaseResult();
        baseResult.setSuccess(false);
        baseResult.setMessage(message);
        return baseResult;
    }
}
