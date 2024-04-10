package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.bo.list.req.TaskListCreateReq;
import com.zmark.mytodo.bo.list.req.TaskListUpdateReq;
import com.zmark.mytodo.dao.TaskGroupDAO;
import com.zmark.mytodo.dao.TaskListDAO;
import com.zmark.mytodo.dto.list.TaskListDTO;
import com.zmark.mytodo.entity.TaskGroup;
import com.zmark.mytodo.entity.TaskList;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import com.zmark.mytodo.service.api.ITaskListService;
import com.zmark.mytodo.service.api.ITaskService;
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
    public TaskListDTO createNewTaskList(TaskListCreateReq creatReq) throws NoDataInDataBaseException, RepeatedEntityInDatabase {
        String name = creatReq.getName();
        Long taskGroupId =
                creatReq.getTaskGroupId() == null ? TaskGroup.DEFAULT_GROUP_ID : creatReq.getTaskGroupId();
        TaskGroup taskGroup = taskGroupDAO.findById(taskGroupId.longValue());
        if (taskGroup == null) {
            if (taskGroupId.equals(TaskGroup.DEFAULT_GROUP_ID)) {
                log.warn("创建任务列表失败！默认任务组不存在！");
            }
            throw new NoDataInDataBaseException("TaskGroup", taskGroupId);
        }
        TaskList taskList = taskListDAO.findByNameAndGroupId(name, taskGroupId);
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

    @Override
    public TaskListDTO updateTaskList(TaskListUpdateReq updateReq) throws NoDataInDataBaseException, RepeatedEntityInDatabase {
        Optional<TaskList> taskListInDataBase = taskListDAO.findById(updateReq.getId());
        if (taskListInDataBase.isEmpty()) {
            throw new NoDataInDataBaseException("TaskList", updateReq.getId());
        }
        TaskList taskList = taskListInDataBase.get();
        
        // name发生了变化
        if (isNameChanged(taskList, updateReq.getName())) {
            TaskList taskListByName = taskListDAO.findByNameAndGroupId(updateReq.getName(), updateReq.getTaskGroupId());
            if (taskListByName != null) {
                throw RepeatedEntityInDatabase.RepeatEntityName("TaskList", updateReq.getName());
            }
            taskList.setName(updateReq.getName());
        }

        // 分组发生了变化
        if (!taskList.getGroupId().equals(updateReq.getTaskGroupId())) {
            Optional<TaskGroup> taskGroup = taskGroupDAO.findById(updateReq.getTaskGroupId());
            if (taskGroup.isEmpty()) {
                throw new NoDataInDataBaseException("TaskGroup", updateReq.getTaskGroupId());
            }
            taskList.setGroupId(updateReq.getTaskGroupId());
        }

        // description发生了变化
        taskList.setDescription(updateReq.getDescription());

        // 更新数据库
        taskListDAO.save(taskList);
        return TaskListDTO.from(taskList, taskService);
    }

    private boolean isNameChanged(TaskList taskList, String newName) {
        return !taskList.getName().equals(newName);
    }
}
