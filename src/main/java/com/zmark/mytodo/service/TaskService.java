package com.zmark.mytodo.service;

import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dao.TaskTagMatchDAO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.TaskTagMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/3 23:50
 */
@Service
public class TaskService {
    private final TaskDAO taskDAO;
    private final TaskTagMatchDAO taskTagMatchDAO;

    @Autowired
    public TaskService(TaskDAO taskDAO, TaskTagMatchDAO taskTagMatchDAO) {
        this.taskDAO = taskDAO;
        this.taskTagMatchDAO = taskTagMatchDAO;
    }

    public Task findTaskById(int taskId) {
        return taskDAO.findById(taskId);
    }

    public List<Task> findAllTasks() {
        return taskDAO.findAll();
    }

    public void createNewTask(Task task) {
        taskDAO.save(task);
    }

    public List<Task> findAllTasksByTag(Tag tag) {
        List<Task> taskList = new ArrayList<>();
        List<TaskTagMatch> matchList = taskTagMatchDAO.findAllByTagId(tag.getId());
        for (TaskTagMatch match : matchList) {
            taskList.add(findTaskById(match.getTaskId()));
        }
        return taskList;
    }
}
