package com.zmark.mytodo.service.api;

import com.zmark.mytodo.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.bo.task.req.TaskUpdateReq;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 15:21
 */
public interface ITaskService {
    TaskDTO toDTO(Task task);

    TaskDTO findTaskById(Long taskId);

    /**
     * 获取指定标签下的所有任务
     * <p>
     * 如果标签不存在，返回空列表
     *
     * @param tagId 标签id
     */
    List<TaskDTO> findAllByTag(Long tagId);


    List<TaskDTO> findAllTasks();

    /**
     * 如果创建成功，返回新创建的任务DTO
     *
     * @param taskCreateReq 创建任务的请求
     * @throws NewEntityException 如果创建失败，抛出异常
     */
    @Transactional
    TaskDTO createNewTask(TaskCreateReq taskCreateReq) throws NewEntityException, NoDataInDataBaseException;

    @Transactional
    TaskDTO updateTask(TaskUpdateReq taskUpdateReq) throws NoDataInDataBaseException, NewEntityException;

    @Transactional
    void deleteTaskById(Long taskId) throws NoDataInDataBaseException;

    /**
     * 标记任务为已完成
     *
     * @param taskId 任务id
     * @throws NoDataInDataBaseException 如果任务不存在，抛出异常
     */
    void completeTask(Long taskId) throws NoDataInDataBaseException;

    /**
     * 标记任务为未完成
     *
     * @param taskId 任务id
     * @throws NoDataInDataBaseException 如果任务不存在，抛出异常
     */
    void unCompleteTask(Long taskId) throws NoDataInDataBaseException;

    /**
     * 截止日期为今天的任务
     */
    List<TaskDTO> getTasksEndToday();

    /**
     * 截止日期在指定日期之间的任务
     */
    List<TaskDTO> getTasksEndBetweenDate(Date endDateStart, Date endDateEnd);
    
    /**
     * 已经过期（截止日期、提醒时间、规划执行时间），但是没有完成的任务
     */
    List<TaskDTO> getUncompletedTasksBefore(Date date);

    /**
     * 指定时间段内创建的任务
     */
    List<TaskDTO> getTasksCreatedBetween(Timestamp start, Timestamp end);
}
