package com.example.demo.util;


import com.example.demo.dao.TaskMapper;
import com.example.demo.vo.TaskVO;

/**
 * Created by korey on 2017/11/12.
 */
public class TaskAdapter {

    /**
     * 修改异常任务状态
     *
     * @param taskVO
     * @param taskMapper
     */
    public static void exceptionTask(TaskVO taskVO, TaskMapper taskMapper) {
        taskMapper.exceptionTask(taskVO.getStartTime(), taskVO.getEndTime(), taskVO.getId());

    }

    /**
     * 修改正常任务状态
     *
     * @param taskVO
     * @param taskMapper
     */
    public static void completeTask(TaskVO taskVO, TaskMapper taskMapper) {
        taskMapper.completeTask(taskVO.getStartTime(), taskVO.getEndTime(), taskVO.getId(),taskVO.getDataStatus());
    }
}
