package com.example.demo.dao;

import com.example.demo.vo.AreaVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by koreyoshi on 2018/1/5.
 */
@Mapper
public interface AreaMapper {
    @Select("SELECT country,province,city,district,another_name as anotherName FROM AREA where another_name = #{areaName}")
    List<AreaVo> getAreaCode(@Param("areaName") String areaName);

    @Select("SELECT country,province,city,district,another_name as anotherName FROM AREA where int_province = #{provinceId} and int_city = #{cityId}")
    List<AreaVo> getAreaCodeById(@Param("provinceId") int provinceId, @Param("cityId") int cityId);

}
