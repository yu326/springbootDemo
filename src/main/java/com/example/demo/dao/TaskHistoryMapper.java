package com.example.demo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by koreyoshi on 2017/12/27.
 */
@Mapper
public interface TaskHistoryMapper {
    @Insert("insert into task_history (id,task,tasklevel,starttime,endtime,taskstatus,datastatus,taskparams,remarks,project_id) values(#{id},#{task},#{tasklevel},#{starttime},#{endtime},#{taskstatus},#{datastatus},#{taskparams},#{remarks},#{project_id})")
    boolean insertTaskHistory(@Param("id") int id, @Param("task") int task,@Param("tasklevel") int tasklevel, @Param("starttime") int starttime, @Param("endtime") int endtime, @Param("taskstatus") int taskstatus, @Param("datastatus") int datastatus, @Param("taskparams") String taskparams, @Param("remarks") String remarks, @Param("project_id") int project_id);
}
