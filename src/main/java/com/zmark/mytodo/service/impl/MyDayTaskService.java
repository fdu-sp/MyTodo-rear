package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.MyDayTaskDAO;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dto.list.RecommendMyDayDTO;
import com.zmark.mytodo.dto.list.RecommendTaskListDTO;
import com.zmark.mytodo.dto.list.TaskListSimpleDTO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.MyDayTask;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.TaskGroup;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.api.IMyDayTaskService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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

    private final TaskDAO taskDAO;


    @Autowired
    public MyDayTaskService(ITaskService taskService,
                            MyDayTaskDAO myDayTaskDAO,
                            TaskDAO taskDAO) {
        this.taskService = taskService;
        this.myDayTaskDAO = myDayTaskDAO;
        this.taskDAO = taskDAO;
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

    /**
     * `我的一天` 列表中，自动加入：<br/>
     * 1. 过去没有完成的任务(截止时间)<br/>
     * 2. 今日截止的任务、设定今日提醒的任务、规划今日执行的任务<br/>
     */
    @Override
    public List<TaskDTO> getMyDayList() {
        // 在MyDayTaskScheduler中，会定时更新我的一天
        List<MyDayTask> myDayTaskList = myDayTaskDAO.findAll();
        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (MyDayTask myDayTask : myDayTaskList) {
            TaskDTO taskDTO = taskService.findTaskById(myDayTask.getTaskId());
            taskDTOList.add(taskDTO);
        }
        return taskDTOList;
    }

    @Override
    public TaskListSimpleDTO getMyDayTaskListSimple() {
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

    /**
     * 推荐任务列表：(都是没有完成的任务，并且不在我的一天列表中，并且不重复)
     * <p>
     * 三天内：截止日期为之后三天的任务 <br/>
     * 未来：截止日期为之后四到七天的任务 <br/>
     * 先前：过去（截止、提醒、规划）的任务 <br/>
     * 最近添加：最近三天创建的任务 <br/>
     */
    @Override
    public RecommendMyDayDTO getRecommendTasks() {
        // 截止日期为之后三天的任务
        RecommendTaskListDTO tasksEndInThreeDays = RecommendTaskListDTO.builder()
                .title("三天内")
                .taskDTOList(taskService.getTasksEndBetweenDate(TimeUtils.afterDays(1), TimeUtils.afterDays(3)))
                .build();
        tasksEndInThreeDays.removeCompletedTasks();
        this.removeTasksInMyDayList(tasksEndInThreeDays);
        // 截止日期为之后四到七天的任务
        RecommendTaskListDTO tasksEndInFourToSevenDays = RecommendTaskListDTO.builder()
                .title("未来")
                .taskDTOList(taskService.getTasksEndBetweenDate(TimeUtils.afterDays(4), TimeUtils.afterDays(7)))
                .build();
        tasksEndInFourToSevenDays.removeCompletedTasks();
        this.removeTasksInMyDayList(tasksEndInFourToSevenDays);
        tasksEndInFourToSevenDays.removeTasksInList(tasksEndInThreeDays);
        // 已经过期(截止日期、提醒日期、规划日期)，但是没有完成的任务
        RecommendTaskListDTO uncompletedTasksEndBeforeToday = RecommendTaskListDTO.builder()
                .title("先前")
                .taskDTOList(taskService.getUncompletedTasksBefore(TimeUtils.today()))
                .build();
        uncompletedTasksEndBeforeToday.removeCompletedTasks();
        this.removeTasksInMyDayList(uncompletedTasksEndBeforeToday);
        uncompletedTasksEndBeforeToday.removeTasksInList(tasksEndInThreeDays);
        uncompletedTasksEndBeforeToday.removeTasksInList(tasksEndInFourToSevenDays);
        // 最近三天创建的任务
        RecommendTaskListDTO latestCreatedTasks = RecommendTaskListDTO.builder()
                .title("最近添加")
                .taskDTOList(taskService.getTasksCreatedBetween(TimeUtils.before(3), TimeUtils.now()))
                .build();
        latestCreatedTasks.removeCompletedTasks();
        this.removeTasksInMyDayList(latestCreatedTasks);
        latestCreatedTasks.removeTasksInList(tasksEndInThreeDays);
        latestCreatedTasks.removeTasksInList(tasksEndInFourToSevenDays);
        latestCreatedTasks.removeTasksInList(uncompletedTasksEndBeforeToday);
        return RecommendMyDayDTO.builder()
                .tasksEndInThreeDays(tasksEndInThreeDays)
                .tasksEndInFourToSevenDays(tasksEndInFourToSevenDays)
                .uncompletedTasksEndBeforeToday(uncompletedTasksEndBeforeToday)
                .latestCreatedTasks(latestCreatedTasks)
                .build();
    }

    /**
     * @return 返回添加到我的一天列表的任务数量
     */
    @Override
    public int updateMyDayTaskList() {
        this.myDayTaskDAO.deleteAll();
        Date today = TimeUtils.today();
        // 今日截止的任务
        List<Task> tasksEndToday = taskDAO.findAllByTaskTimeInfo_EndDate(today);
        // 设定今日提醒的任务
        List<Task> tasksReminderToday = taskDAO.findAllTasksOfReminderByDate(today);
        // 规划今日执行的任务
        List<Task> tasksExpectedToday = taskDAO.findAllByTaskTimeInfo_ExpectedExecutionDate(today);
        return addToMyDayList(tasksEndToday) +
                addToMyDayList(tasksReminderToday) +
                addToMyDayList(tasksExpectedToday);
    }

    private int addToMyDayList(List<Task> taskList) {
        int count = 0;
        for (Task task : taskList) {
            if (!myDayTaskDAO.existsByTaskId(task.getId())) {
                MyDayTask myDayTask = MyDayTask.builder()
                        .taskId(task.getId())
                        .build();
                myDayTaskDAO.save(myDayTask);
                count++;
            }
        }
        return count;
    }


    private void removeTasksInMyDayList(RecommendTaskListDTO recommendTaskListDTO) {
        List<TaskDTO> taskDTOList = recommendTaskListDTO.getTaskDTOList();
        List<TaskDTO> taskDTOListInMyDayList = getMyDayList();
        taskDTOList.removeAll(taskDTOListInMyDayList);
    }
}
