package com.example.demo.dao;

import com.example.demo.entity.TaskScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by koreyoshi on 2017/12/21.
 */
@Mapper
public interface TaskScheduleMapper {
    @Select("select id,task,tasktype as taskType,tasklevel, taskparams,remarks,project_id as projectId from task_schedule where taskstatus =0")
    List<TaskScheduleEntity> createCurrentTask();

    @Update("update task_schedule set last_update_time = #{updateTime} where id = #{id}")
    void updateTaskInfos(@Param("updateTime") int updateTime, @Param("id") int id);
}
