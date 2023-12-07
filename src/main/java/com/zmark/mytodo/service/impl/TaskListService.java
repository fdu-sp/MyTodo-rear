package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.TaskGroupDAO;
import com.zmark.mytodo.dao.TaskListDAO;
import com.zmark.mytodo.dto.TaskListDTO;
import com.zmark.mytodo.entity.TaskGroup;
import com.zmark.mytodo.entity.TaskList;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.api.ITaskListService;
import com.zmark.mytodo.service.api.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZMark
 * @date 2023/12/7 19:26
 */
@Service
public class TaskListService implements ITaskListService {

    private final ITaskService taskService;

    private final TaskListDAO taskListDAO;

    private final TaskGroupDAO taskGroupDAO;

    @Autowired
    public TaskListService(TaskListDAO taskListDAO,
                           TaskGroupDAO taskGroupDAO,
                           TaskService taskService) {
        this.taskListDAO = taskListDAO;
        this.taskGroupDAO = taskGroupDAO;
        this.taskService = taskService;
    }

    @Override
    public TaskListDTO findById(long taskListId) throws NoDataInDataBaseException {
        TaskList taskList = taskListDAO.findById(taskListId);
        if (taskList == null) {
            throw new NoDataInDataBaseException("TaskList", taskListId);
        }
        return TaskListDTO.from(taskList, taskService);
    }

    @Override
    public TaskListDTO findByName(String name) throws NoDataInDataBaseException {
        TaskList taskList = taskListDAO.findByName(name);
        if (taskList == null) {
            throw new NoDataInDataBaseException("TaskList", name);
        }
        return TaskListDTO.from(taskList, taskService);
    }

    @Override
    public TaskListDTO createNewTaskList(String name, Long taskGroupId) {
        TaskList taskList = taskListDAO.findByName(name);
        if (taskList != null) {
            return TaskListDTO.from(taskList, taskService);
        }
        taskList = TaskList.builder()
                .name(name)
                .groupId(taskGroupId)
                .build();
        taskListDAO.save(taskList);
        return TaskListDTO.from(taskList, taskService);
    }

    @Override
    public long countByTaskGroup(long tagGroupId) {
        return taskListDAO.countTaskListsByGroupId(tagGroupId);
    }

    @Override
    public long countByTaskGroup(String groupName) {
        TaskGroup taskGroup = taskGroupDAO.findByName(groupName);
        if (taskGroup == null) {
            return 0;
        } else {
            return taskListDAO.countTaskListsByGroupId(taskGroup.getId());
        }
    }
}
