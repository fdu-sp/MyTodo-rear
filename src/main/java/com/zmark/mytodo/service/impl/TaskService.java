package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dao.TaskTagMatchDAO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.TaskTagMatch;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.vo.task.req.TaskCreatReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/3 23:50
 */
@Service
public class TaskService implements ITaskService {
    private final TaskDAO taskDAO;
    private final TaskTagMatchDAO taskTagMatchDAO;
    private final TagDAO tagDAO;

    @Autowired
    public TaskService(TaskDAO taskDAO, TaskTagMatchDAO taskTagMatchDAO, TagDAO tagDAO) {
        this.taskDAO = taskDAO;
        this.taskTagMatchDAO = taskTagMatchDAO;
        this.tagDAO = tagDAO;
    }

    private TaskDTO toDTO(Task task) {
        List<TaskTagMatch> tagMatches = taskTagMatchDAO.findAllByTaskId(task.getId());
        List<Tag> tags = new ArrayList<>();
        for (TaskTagMatch match : tagMatches) {
            tags.add(tagDAO.findTagById(match.getTagId()));
        }
        return TaskDTO.from(task, tags);
    }

    @Override
    public TaskDTO findTaskById(Long taskId) {
        return this.toDTO(taskDAO.findTaskById(taskId));
    }

    @Override
    public List<TaskDTO> findAllTasks() {
        List<Task> taskList = taskDAO.findAll();
        return taskList.stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional
    public void createNewTask(TaskCreatReq taskCreatReq) {
        // 保存tags
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : taskCreatReq.getTagNames()) {
            Tag tag = tagDAO.findByTagName(tagName);
            if (tag == null) {
                tag = Tag.builder()
                        .tagName(tagName)
                        .createTime(new Timestamp(System.currentTimeMillis()))
                        .updateTime(new Timestamp(System.currentTimeMillis()))
                        .build();
                tagDAO.save(tag);
            }
            tagList.add(tag);
        }
        // 保存task
        Task task = taskCreatReq.toTaskDTO();
        taskDAO.save(task);
        // 保存 Tags 和 Task 的关联关系
        Long taskId = task.getId();
        for (Tag tag : tagList) {
            TaskTagMatch match = TaskTagMatch.builder()
                    .taskId(taskId)
                    .tagId(tag.getId())
                    .build();
            taskTagMatchDAO.save(match);
        }
    }

    private List<Task> findAllTasksByTag(Tag tag) {
        List<Task> taskList = new ArrayList<>();
        List<TaskTagMatch> matchList = taskTagMatchDAO.findAllByTagId(tag.getId());
        for (TaskTagMatch match : matchList) {
            taskList.add(taskDAO.findTaskById(match.getTaskId()));
        }
        return taskList;
    }
}
