package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.group.TaskGroupSimpleDTO;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import com.zmark.mytodo.vo.group.req.TaskGroupCreateReq;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 19:25
 */
public interface ITaskGroupService {
    List<TaskGroupSimpleDTO> findAll();

    TaskGroupSimpleDTO createNew(TaskGroupCreateReq createReq) throws RepeatedEntityInDatabase;
}
