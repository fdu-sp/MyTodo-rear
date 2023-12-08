package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.group.TaskGroupSimpleDTO;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import com.zmark.mytodo.bo.group.req.TaskGroupCreateReq;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 19:25
 */
public interface ITaskGroupService {
    List<TaskGroupSimpleDTO> findAll();

    /**
     * @throws RepeatedEntityInDatabase 如果数据库中已经存在同名的分组，则抛出异常
     */
    TaskGroupSimpleDTO createNew(TaskGroupCreateReq createReq) throws RepeatedEntityInDatabase;
}
