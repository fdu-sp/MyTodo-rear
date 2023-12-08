package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.TaskGroupDAO;
import com.zmark.mytodo.dto.group.TaskGroupSimpleDTO;
import com.zmark.mytodo.entity.TaskGroup;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import com.zmark.mytodo.service.api.ITaskGroupService;
import com.zmark.mytodo.vo.group.req.TaskGroupCreateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/8 14:39
 */
@Service
public class TaskGroupService implements ITaskGroupService {

    private final TaskGroupDAO taskGroupDAO;

    @Autowired
    public TaskGroupService(TaskGroupDAO taskGroupDAO) {
        this.taskGroupDAO = taskGroupDAO;
    }

    @Override
    public List<TaskGroupSimpleDTO> findAll() {
        List<TaskGroup> taskGroupList = taskGroupDAO.findAll();
        return TaskGroup.toSimpleDTOList(taskGroupList);
    }

    @Override
    public TaskGroupSimpleDTO createNew(TaskGroupCreateReq createReq) throws RepeatedEntityInDatabase {
        TaskGroup taskGroup = taskGroupDAO.findByName(createReq.getName());
        if (taskGroup != null) {
            throw RepeatedEntityInDatabase.RepeatEntityName("TaskGroup", createReq.getName());
        }
        taskGroup = TaskGroup.builder()
                .name(createReq.getName())
                .description(createReq.getDescription())
                .build();
        taskGroupDAO.save(taskGroup);
        return taskGroup.toSimpleDTO();
    }
}
