package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.TaskGroupDAO;
import com.zmark.mytodo.dao.TaskListDAO;
import com.zmark.mytodo.dto.list.TaskListDTO;
import com.zmark.mytodo.entity.TaskGroup;
import com.zmark.mytodo.entity.TaskList;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import com.zmark.mytodo.service.api.ITaskListService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.vo.list.req.TaskListCreatReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author ZMark
 * @date 2023/12/7 19:26
 */
@Slf4j
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
    public TaskListDTO createNewTaskList(TaskListCreatReq creatReq) throws NoDataInDataBaseException, RepeatedEntityInDatabase {
        String name = creatReq.getName();
        Long taskGroupId =
                creatReq.getTaskGroupId() == null ? TaskGroup.DEFAULT_GROUP_ID : creatReq.getTaskGroupId();
        Optional<TaskGroup> taskGroup = taskGroupDAO.findById(taskGroupId);
        if (taskGroup.isEmpty()) {
            if (taskGroupId.equals(TaskGroup.DEFAULT_GROUP_ID)) {
                log.warn("创建任务列表失败！默认任务组不存在！");
            }
            throw new NoDataInDataBaseException("TaskGroup", taskGroupId);
        }
        TaskList taskList = taskListDAO.findByName(name);
        if (taskList != null) {
            throw RepeatedEntityInDatabase.RepeatEntityName("TaskList", name);
        }
        taskList = TaskList.builder()
                .name(name)
                .description(creatReq.getDescription())
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
