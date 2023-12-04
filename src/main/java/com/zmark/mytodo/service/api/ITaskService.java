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

    @Transactional
    void createNewTask(TaskCreatReq taskCreatReq) throws NewEntityException;
}
