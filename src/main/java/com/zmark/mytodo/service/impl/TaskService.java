package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dao.TaskTagMatchDAO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.TaskTagMatch;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.api.ITagService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.vo.task.req.TaskCreatReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/3 23:50
 */
@Slf4j
@Service
public class TaskService implements ITaskService {
    private final TaskDAO taskDAO;
    private final TaskTagMatchDAO taskTagMatchDAO;
    private final TagDAO tagDAO;

    private final ITagService tagService;

    @Autowired
    public TaskService(TaskDAO taskDAO, TaskTagMatchDAO taskTagMatchDAO, TagDAO tagDAO, TagService tagService) {
        this.taskDAO = taskDAO;
        this.taskTagMatchDAO = taskTagMatchDAO;
        this.tagDAO = tagDAO;
        this.tagService = tagService;
    }

    private TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
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
    public List<TaskDTO> findAllByTag(Long tagId) {
        Tag tag = tagDAO.findTagById(tagId);
        if (tag == null) {
            return new ArrayList<>();
        }
        List<Task> taskList = this.findAllTasksByTag(tag);
        return taskList.stream().map(this::toDTO).toList();
    }

    @Override
    public List<TaskDTO> findAllTasks() {
        List<Task> taskList = taskDAO.findAll();
        return taskList.stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional
    public TaskDTO createNewTask(TaskCreatReq taskCreatReq) throws NewEntityException {
        // 保存tags
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : taskCreatReq.getTagNames()) {
            Tag tag = tagDAO.findByTagName(tagName);
            if (tag == null) {
                tag = tagService.createNewTag(tagName);
            }
            tagList.add(tag);
        }
        // 保存task
        Task task = Task.fromTaskCreatReq(taskCreatReq);
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
        log.info("createNewTask succeed, task: {}", taskCreatReq);
        return TaskDTO.from(task, tagList);
    }

    @Override
    public void completeTask(Long taskId) throws NoDataInDataBaseException {
        Task task = taskDAO.findTaskById(taskId);
        if (task == null) {
            throw new NoDataInDataBaseException("找不到id为" + taskId + "的任务");
        }
        task.complete();
        taskDAO.save(task);
    }

    @Override
    public void unCompleteTask(Long taskId) throws NoDataInDataBaseException {
        Task task = taskDAO.findTaskById(taskId);
        if (task == null) {
            throw new NoDataInDataBaseException("找不到id为" + taskId + "的任务");
        }
        task.unComplete();
        taskDAO.save(task);
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
