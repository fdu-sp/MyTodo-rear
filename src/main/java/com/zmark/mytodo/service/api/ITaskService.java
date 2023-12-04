package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.vo.task.req.TaskCreatReq;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 15:21
 */
public interface ITaskService {
    TaskDTO findTaskById(Long taskId);

    List<TaskDTO> findAllTasks();

    /**
     * 如果创建成功，返回新创建的任务DTO
     *
     * @param taskCreatReq 创建任务的请求
     * @throws NewEntityException 如果创建失败，抛出异常
     */
    @Transactional
    TaskDTO createNewTask(TaskCreatReq taskCreatReq) throws NewEntityException;
}