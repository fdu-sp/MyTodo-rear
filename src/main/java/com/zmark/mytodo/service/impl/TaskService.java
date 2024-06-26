package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.bo.task.req.TaskQueryByTagsReq;
import com.zmark.mytodo.bo.task.req.TaskUpdateReq;
import com.zmark.mytodo.bo.timer.req.TimerUpdateReq;
import com.zmark.mytodo.dao.*;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.dto.timer.TimerDTO;
import com.zmark.mytodo.entity.*;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.UpdateEntityException;
import com.zmark.mytodo.service.api.ITagService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.service.api.ITimerService;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

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
    private final ITimerService timerService;

    @Autowired
    public TaskService(TaskDAO taskDAO,
                       TaskTagMatchDAO taskTagMatchDAO,
                       TagDAO tagDAO,
                       TaskListDAO taskListDAO,
                       TaskTimeInfoDAO taskTimeInfoDAO,
                       MyDayTaskDAO myDayTaskDAO,
                       TagService tagService,
                       TimerService timerService) {
        this.taskDAO = taskDAO;
        this.taskTagMatchDAO = taskTagMatchDAO;
        this.tagDAO = tagDAO;
        this.taskListDAO = taskListDAO;
        this.taskTimeInfoDAO = taskTimeInfoDAO;
        this.myDayTaskDAO = myDayTaskDAO;
        this.tagService = tagService;
        this.timerService = timerService;
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
    public List<TaskDTO> findAllByTags(TaskQueryByTagsReq queryReq) {
        // 根据标签过滤（同时具有多个标签的）待办事项
        List<Long> tagList = queryReq.getTagIds();
        List<Task> taskList = new ArrayList<>();
        for (Long tagId : tagList) {
            Tag tag = tagDAO.findTagById(tagId);
            if (tag == null) {
                continue;
            }
            List<Task> tasks = this.findAllTasksByTag(tag);
            if (taskList.isEmpty()) {
                taskList.addAll(tasks);
            } else {
                taskList.retainAll(tasks);
            }
        }
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
        TaskDTO taskDTO = this.saveOrUpdate(Task.fromTaskCreatReq(taskCreateReq), taskCreateReq.getTagNames(), taskCreateReq.getInMyDay());
        log.info("createNewTask succeed, task: {}", taskCreateReq);
        return taskDTO;
    }

    private TaskDTO saveOrUpdate(Task task, List<String> tagNameList, Boolean inMyDay) throws NoDataInDataBaseException, NewEntityException {
        // 检查taskList是否存在
        Long taskListId = task.getTaskListId();
        taskListId = taskListId == null ? TaskList.DEFAULT_LIST_ID : taskListId;
        TaskList taskList = taskListDAO.findTaskListById(taskListId);
        if (taskList == null) {
            throw new NoDataInDataBaseException("找不到id为" + taskListId + "的任务清单");
        }
        task.setTaskListId(taskListId);
        // 保存新增的 tags
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tagNameList) {
            Tag tag = tagDAO.findByTagName(tagName);
            if (tag == null) {
                tag = tagService.createNewTag(tagName);
            }
            tagList.add(tag);
            // 父级tag也和当前任务建立match关系
            while (tag.getParentTag() != null) {
                tag = tag.getParentTag();
                tagList.add(tag);
            }
        }
        // 保存task
        taskDAO.save(task);
        // 保存 Tags 和 Task 的关联关系
        Long taskId = task.getId();
        for (Tag tag : tagList) {
            // 如果已经存在关联关系，跳过
            List<TaskTagMatch> taskTagMatchList = taskTagMatchDAO.findAllByTagIdAndTaskId(tag.getId(), taskId);
            if (!taskTagMatchList.isEmpty()) {
                continue;
            }
            TaskTagMatch match = TaskTagMatch.builder()
                    .taskId(taskId)
                    .tagId(tag.getId())
                    .build();
            taskTagMatchDAO.save(match);
        }
        // 如果是 MyDayTask，保存 MyDayTask
        if (inMyDay != null && inMyDay
                && !isTaskInMyDay(taskId)) {
            MyDayTask myDayTask = MyDayTask.builder()
                    .taskId(taskId)
                    .build();
            myDayTaskDAO.save(myDayTask);
        }
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
    @Transactional
    public TaskDTO updateTask(TaskUpdateReq taskUpdateReq) throws NoDataInDataBaseException, NewEntityException {
        List<String> tagNameList = taskUpdateReq.getTags().stream().map(TagSimpleResp::getTagPath).toList();
        TaskDTO taskDTO = this.saveOrUpdate(Task.fromTaskUpdateReq(taskUpdateReq), tagNameList, taskUpdateReq.getInMyDay());
        log.info("updateTask succeed, task: {}", taskUpdateReq);
        // 更新“我的一天”任务清单
        // 在我的一天中，已经存在该任务，因此直接返回
        if (myDayTaskDAO.existsByTaskId(taskDTO.getId())) {
            return taskDTO;
        }
        Date today = TimeUtils.today();
        TaskTimeInfo taskTimeInfo = taskDTO.getTaskTimeInfo();
        Date endDate = taskTimeInfo.getEndDate();
        Timestamp reminderTimestamp = taskTimeInfo.getReminderTimestamp();
        boolean isEndToday = TimeUtils.isSameDay(today, endDate);
        boolean isReminderToday = reminderTimestamp != null
                && TimeUtils.isSameDay(today, new Date(reminderTimestamp.getTime()));
        Date expectedExecutionDate = taskTimeInfo.getExpectedExecutionDate();
        boolean isExpectedToday = expectedExecutionDate != null
                && TimeUtils.isSameDay(today, expectedExecutionDate);
        if (isEndToday || isReminderToday || isExpectedToday) {
            myDayTaskDAO.save(MyDayTask.builder().taskId(taskDTO.getId()).build());
            log.info("just updated task has been added to my day");
        }
        return taskDTO;
    }

    @Override
    @Transactional
    public void deleteTaskById(Long taskId) throws NoDataInDataBaseException {
        Task task = taskDAO.findTaskById(taskId);
        if (task == null) {
            throw new NoDataInDataBaseException("找不到id为" + taskId + "的任务");
        }
        // 删除 MyDayTask
        MyDayTask myDayTask = myDayTaskDAO.findMyDayTaskByTaskId(taskId);
        if (myDayTask != null) {
            myDayTaskDAO.delete(myDayTask);
        }
        // 删除 TaskTagMatch
        taskTagMatchDAO.deleteAllByTaskId(taskId);
        taskDAO.delete(task);
    }

    @Override
    public void completeTask(Long taskId) throws NoDataInDataBaseException, UpdateEntityException {
        // 设置任务完成状态
        Task task = taskDAO.findTaskById(taskId);
        if (task == null) {
            throw new NoDataInDataBaseException("找不到id为" + taskId + "的任务");
        }
        task.complete();
        taskDAO.save(task);

        // 检查当前是否有与该任务关联的计时器，若有则结束计时器
        TimerDTO timerDTO = timerService.getCurrentTimer();
        if (timerDTO != null && timerDTO.getTaskId().equals(taskId)) {
            timerService.updateTimer(TimerUpdateReq.builder().timerId(timerDTO.getId()).build());
        }
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
    public List<TaskDTO> getUncompletedTasksEndBetweenDate(Date endDateStart, Date endDateEnd) {
        return taskDAO.findAllByEndDateBetweenAndTaskComplete(endDateStart, endDateEnd, false)
                .stream().map(this::toDTO).toList();
    }

    private List<TaskDTO> getUncompletedTasksEndBefore(Date date) {
        return taskDAO.findAllByEndDateBeforeAndTaskComplete(date, false)
                .stream().map(this::toDTO).toList();
    }

    /**
     * 已经过了提醒时间，但是没有完成的任务
     *
     * @param date
     */
    private List<TaskDTO> getUncompletedTasksReminderBefore(Date date) {
        return taskDAO.findAllByReminderDateBeforeAndTaskComplete(date, false)
                .stream().map(this::toDTO).toList();
    }

    /**
     * 已经过了规划执行时间，但是没有完成的任务
     *
     * @param date
     */
    private List<TaskDTO> getUncompletedTasksExpectedBefore(Date date) {
        return taskDAO.findAllByExpectedDateBeforeAndTaskComplete(date, false)
                .stream().map(this::toDTO).toList();
    }

    /**
     * 已经过期（截止日期、提醒时间、规划执行时间），但是没有完成的任务
     *
     * @param date
     */
    @Override
    public List<TaskDTO> getUncompletedTasksBefore(Date date) {
        List<TaskDTO> endBefore = this.getUncompletedTasksEndBefore(date);
        List<TaskDTO> remindBefore = this.getUncompletedTasksReminderBefore(date);
        List<TaskDTO> expectedBefore = this.getUncompletedTasksExpectedBefore(date);

        Set<TaskDTO> tasksBeforeSet = new HashSet<>(endBefore);
        tasksBeforeSet.addAll(remindBefore);
        tasksBeforeSet.addAll(expectedBefore);

        return new ArrayList<>(tasksBeforeSet);
    }

    @Override
    public List<TaskDTO> getTasksCreatedBetween(Timestamp start, Timestamp end) {
        List<Task> taskList = taskDAO.findAllByCreateTimeIsGreaterThanEqualAndCreateTimeIsLessThanEqual(start, end);
        return taskList.stream().map(this::toDTO).toList();
    }

    @Override
    public List<TaskDTO> getTaskByPeriod(String period) {
        if (Objects.equals(period, "all")) {
            return findAllTasks();
        } else if (Objects.equals(period, "today")) {
            List<MyDayTask> myDayTaskList = myDayTaskDAO.findAll();
            List<TaskDTO> taskDTOList = new ArrayList<>();
            for (MyDayTask myDayTask : myDayTaskList) {
                TaskDTO taskDTO = findTaskById(myDayTask.getTaskId());
                taskDTOList.add(taskDTO);
            }
            return taskDTOList;
        } else {
            throw new IllegalArgumentException("getTaskByPeriod: 不合法参数");
        }
    }

    private boolean isTaskInMyDay(Long taskId) {
        return myDayTaskDAO.findMyDayTaskByTaskId(taskId) != null;
    }
}
