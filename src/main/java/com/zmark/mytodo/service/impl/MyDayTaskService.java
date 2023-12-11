package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.MyDayTaskDAO;
import com.zmark.mytodo.dto.list.TaskListSimpleDTO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.MyDayTask;
import com.zmark.mytodo.entity.TaskGroup;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.api.IMyDayTaskService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/11 18:03
 */
@Service
public class MyDayTaskService implements IMyDayTaskService {
    private final ITaskService taskService;

    private final MyDayTaskDAO myDayTaskDAO;

    @Autowired
    public MyDayTaskService(ITaskService taskService, MyDayTaskDAO myDayTaskDAO) {
        this.taskService = taskService;
        this.myDayTaskDAO = myDayTaskDAO;
    }

    @Override
    public void addToMyDayList(Long taskId) throws NoDataInDataBaseException, NewEntityException {
        TaskDTO taskDTO = taskService.findTaskById(taskId);
        if (taskDTO == null) {
            throw new NoDataInDataBaseException(String.format("id为%d的task不存在！", taskId));
        }
        MyDayTask myDayTaskInDataBase = myDayTaskDAO.findMyDayTaskByTaskId(taskId);
        if (myDayTaskInDataBase != null) {
            throw new NewEntityException(String.format("id为%d的task已经在我的一天列表中！", taskId));
        }
        MyDayTask myDayTask = MyDayTask.builder()
                .taskId(taskId)
                .build();
        myDayTaskDAO.save(myDayTask);
    }

    @Override
    public void removeFromMyDayList(Long taskId) throws NoDataInDataBaseException {
        MyDayTask myDayTask = myDayTaskDAO.findMyDayTaskByTaskId(taskId);
        if (myDayTask != null) {
            myDayTaskDAO.delete(myDayTask);
        } else {
            throw new NoDataInDataBaseException(String.format("id为%d的task不存在！", taskId));
        }
    }

    @Override
    public void clearMyDayList() {
        myDayTaskDAO.deleteAll();
    }

    @Override
    public List<TaskDTO> getMyDayList() {
        List<MyDayTask> myDayTaskList = myDayTaskDAO.findAll();
        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (MyDayTask myDayTask : myDayTaskList) {
            TaskDTO taskDTO = taskService.findTaskById(myDayTask.getTaskId());
            taskDTOList.add(taskDTO);
        }
        return taskDTOList;
    }

    @Override
    public TaskListSimpleDTO getMyDayTaskList() {
        return TaskListSimpleDTO.builder()
                .id(0L)
                .name("我的一天")
                .count(myDayTaskDAO.count())
                .description("")
                .groupId(TaskGroup.DEFAULT_GROUP_ID)
                .createTime(TimeUtils.now())
                .updateTime(TimeUtils.now())
                .build();
    }
}
