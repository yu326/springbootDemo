package com.example.demo.dao;

import com.example.demo.vo.UserAreaCodeVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by koreyoshi on 2018/1/8.
 */
@Mapper
public interface UserAreaCodeMapper {
    @Insert("insert into user_areacode_temporary(screen_name,province_code,city_code) values(#{screenName},#{province_code},#{city_code})")
    boolean insertOne(@Param("screenName") String screenName,@Param("province_code") int province_code,@Param("city_code") int city_code );

    @Select("select screen_name as screenName,province_code as provinceCode,city_code as cityCode from user_areacode_temporary where screen_name = #{screenName}")
    List<UserAreaCodeVO> getOne(@Param("screenName") String screenName);
}
