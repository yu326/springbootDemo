package com.example.demo.vo;

/**
 * Created by koreyoshi on 2018/1/5.
 */

import lombok.Data;

/**
 * 地区表vo
 */
@Data
public class AreaVo {
    //地区码
    private int areaCode;
    //国家
    private String country;
    //省
    private int province;
    //市
    private int city;
    //区
    private int district;
    //名称
    private String name;
    //别名
    private String anotherName;
    //简称
    private String shortName;
}
