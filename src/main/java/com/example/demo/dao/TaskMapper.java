package com.example.demo.dao;

import com.example.demo.vo.TaskVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by korey on 2017/12/9.
 */
@Mapper
public interface TaskMapper {
    @Select("select task.id,task.task,task.remarks,task.project_id as projectId,task.tasklevel,task.taskparams,task.datastatus as dataStatus,task.starttime as startTime,task.endtime as endTime,task.error_msg as errorMsg, project.cache_name as cacheName ,project.`column`,project.column1,project.access_token as accessToken from task left join project on task.project_id = project.id where task.taskstatus = 0 and project.is_avaliable = 1  order by task.tasklevel desc,id asc limit 10")
    List<TaskVO> getTaskInfosById();

    @Update("update task set starttime = #{startTime}, endtime = #{endTime} , taskstatus =1,datastatus = #{totalDatanum} where id = #{id}")
    void completeTask(@Param("startTime") int startTime, @Param("endTime") int endTime, @Param("id") int id, @Param("totalDatanum") int totalDatanum);

    @Update("update task set starttime = #{startTime} , endtime = #{endTime} ,  taskstatus =2 where id = #{id}")
    void exceptionTask(@Param("startTime") int startTime, @Param("endTime") int endTime, @Param("id") int id);

    @Insert("insert into task(tasktype,task,tasklevel,taskstatus,taskparams,remarks,project_id) values(#{tasktype},#{task},#{tasklevel},#{taskstatus},#{taskparams},#{remarks},#{projectId})")
    void createKeyWordsTask(@Param("tasktype") int tasktype, @Param("task") int task, @Param("tasklevel") int tasklevel, @Param("taskstatus") int taskstatus, @Param("taskparams") String taskparams, @Param("remarks") String remarks, @Param("projectId") int projectId);

    @Insert("delete from task where id = #{id}")
    void deleteOne(@Param("id") int id);


}
