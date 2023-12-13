package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.dao.*;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.*;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.api.ITagService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final TaskListDAO taskListDAO;
    private final TaskTimeInfoDAO taskTimeInfoDAO;
    private final MyDayTaskDAO myDayTaskDAO;

    private final ITagService tagService;

    @Autowired
    public TaskService(TaskDAO taskDAO,
                       TaskTagMatchDAO taskTagMatchDAO,
                       TagDAO tagDAO,
                       TaskListDAO taskListDAO,
                       TaskTimeInfoDAO taskTimeInfoDAO,
                       MyDayTaskDAO myDayTaskDAO,
                       TagService tagService) {
        this.taskDAO = taskDAO;
        this.taskTagMatchDAO = taskTagMatchDAO;
        this.tagDAO = tagDAO;
        this.taskListDAO = taskListDAO;
        this.taskTimeInfoDAO = taskTimeInfoDAO;
        this.myDayTaskDAO = myDayTaskDAO;
        this.tagService = tagService;
    }

    @Override
    public TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
        List<TaskTagMatch> tagMatches = taskTagMatchDAO.findAllByTaskId(task.getId());
        List<Tag> tags = new ArrayList<>();
        for (TaskTagMatch match : tagMatches) {
            tags.add(tagDAO.findTagById(match.getTagId()));
        }
        TaskList taskList = taskListDAO.findTaskListById(task.getTaskListId());
        if (taskList == null) {
            taskList = TaskList.builder()
                    .id(TaskList.DEFAULT_LIST_ID)
                    .name("默认清单")
                    .build();
        }
        return TaskDTO.from(task, tags, isTaskInMyDay(task.getId()), taskList);
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
    public TaskDTO createNewTask(TaskCreateReq taskCreateReq) throws NewEntityException, NoDataInDataBaseException {
        // 检查taskList是否存在
        Long taskListId = taskCreateReq.getTaskListId();
        taskListId = taskListId == null ? TaskList.DEFAULT_LIST_ID : taskListId;
        Optional<TaskList> taskList = taskListDAO.findById(taskListId);
        if (taskList.isEmpty()) {
            throw new NoDataInDataBaseException("找不到id为" + taskListId + "的任务清单");
        }
        taskCreateReq.setTaskListId(taskListId);
        // 保存tags
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : taskCreateReq.getTagNames()) {
            Tag tag = tagDAO.findByTagName(tagName);
            if (tag == null) {
                tag = tagService.createNewTag(tagName);
            }
            tagList.add(tag);
        }
        // 保存task
        Task task = Task.fromTaskCreatReq(taskCreateReq);
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
        log.info("createNewTask succeed, task: {}", taskCreateReq);
        TaskList taskList1 = taskListDAO.findTaskListById(taskListId);
        if (taskList1 == null) {
            taskList1 = TaskList.builder()
                    .id(TaskList.DEFAULT_LIST_ID)
                    .name("默认清单")
                    .build();
        }
        return TaskDTO.from(task, tagList, isTaskInMyDay(taskId), taskList1);
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

    @Override
    public List<TaskDTO> getTasksEndToday() {
        List<Task> taskList = taskDAO.findAllByTaskTimeInfo_EndDate(TimeUtils.today());
        return taskList.stream().map(this::toDTO).toList();

    }

    @Override
    public List<TaskDTO> getTasksEndBetweenDate(Date endDateStart, Date endDateEnd) {
        List<TaskTimeInfo> taskTimeInfoList
                = taskTimeInfoDAO
                .findAllByEndDateIsGreaterThanEqualAndEndDateIsLessThanEqual(endDateStart, endDateEnd);
        return taskTimeInfoList.stream()
                .map(TaskTimeInfo::getTask)
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> getUncompletedTasksEndBeforeToday() {
        List<TaskTimeInfo> taskTimeInfoList
                = taskTimeInfoDAO.findAllByEndDateIsLessThanAndTaskCompleted(TimeUtils.today(), false);
        return taskTimeInfoList.stream()
                .map(TaskTimeInfo::getTask)
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> getTasksCreatedBetween(Timestamp start, Timestamp end) {
        List<Task> taskList = taskDAO.findAllByCreateTimeIsGreaterThanEqualAndCreateTimeIsLessThanEqual(start, end);
        return taskList.stream().map(this::toDTO).toList();
    }

    private boolean isTaskInMyDay(Long taskId) {
        return myDayTaskDAO.findMyDayTaskByTaskId(taskId) != null;
    }
}
