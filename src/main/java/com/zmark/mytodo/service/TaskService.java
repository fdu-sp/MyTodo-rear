package com.zmark.mytodo.service;

import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/3 23:50
 */
@Service
public class TaskService {
    private final TaskDAO taskDAO;

    @Autowired
    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
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
}
