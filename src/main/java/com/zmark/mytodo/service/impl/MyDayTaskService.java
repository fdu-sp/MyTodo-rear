package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.MyDayTaskDAO;
import com.zmark.mytodo.dto.list.RecommendMyDayDTO;
import com.zmark.mytodo.dto.list.RecommendTaskListDTO;
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

import java.sql.Timestamp;
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

    private Timestamp timestampBefore;

    @Autowired
    public MyDayTaskService(ITaskService taskService, MyDayTaskDAO myDayTaskDAO) {
        this.taskService = taskService;
        this.myDayTaskDAO = myDayTaskDAO;
        this.timestampBefore = TimeUtils.now();
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
        this.checkAndUpdate();
        List<MyDayTask> myDayTaskList = myDayTaskDAO.findAll();
        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (MyDayTask myDayTask : myDayTaskList) {
            TaskDTO taskDTO = taskService.findTaskById(myDayTask.getTaskId());
            taskDTOList.add(taskDTO);
        }
        return taskDTOList;
    }

    /*
     * 更新我的一天列表
     * */
    private void checkAndUpdate() {
        Timestamp timestampNow = TimeUtils.now();
        if (!TimeUtils.isAfter(timestampBefore, timestampNow)) {
            return;
        }
        // 如果是第二天了，就更新我的一天列表
        timestampBefore = timestampNow;
        List<MyDayTask> myDayTaskList = myDayTaskDAO.findAll();
        for (MyDayTask myDayTask : myDayTaskList) {
            TaskDTO taskDTO = taskService.findTaskById(myDayTask.getTaskId());
            if (taskDTO == null) {
                myDayTaskDAO.delete(myDayTask);
            }
        }
        // 将截止日期为今天的任务添加到我的一天列表中
        List<TaskDTO> tasksEndToday = taskService.getTasksEndToday();
        for (TaskDTO taskDTO : tasksEndToday) {
            if (!myDayTaskDAO.existsByTaskId(taskDTO.getId())) {
                MyDayTask myDayTask = MyDayTask.builder()
                        .taskId(taskDTO.getId())
                        .build();
                myDayTaskDAO.save(myDayTask);
            }
        }
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
     * 1. 截止日期为今天的任务 <br/>
     * 2. 截止日期为之后三天的任务 <br/>
     * 3. 截止日期为之后四到七天的任务 <br/>
     * 4. 已经过期，但是没有完成的任务 <br/>
     * 5. 最新一天创建的任务 <br/>
     */
    @Override
    public RecommendMyDayDTO getRecommendTasks() {
        // 截止日期为今天的任务
        RecommendTaskListDTO tasksEndToday = RecommendTaskListDTO.builder()
                .title("今天")
                .taskDTOList(taskService.getTasksEndToday())
                .build();
        tasksEndToday.removeCompletedTasks();
        this.removeTasksInMyDayList(tasksEndToday);
        // 截止日期为之后三天的任务
        RecommendTaskListDTO tasksEndInThreeDays = RecommendTaskListDTO.builder()
                .title("三天内")
                .taskDTOList(taskService.getTasksEndBetweenDate(TimeUtils.afterDays(1), TimeUtils.afterDays(3)))
                .build();
        tasksEndInThreeDays.removeCompletedTasks();
        this.removeTasksInMyDayList(tasksEndInThreeDays);
        tasksEndInThreeDays.removeTasksInList(tasksEndToday);
        // 截止日期为之后四到七天的任务
        RecommendTaskListDTO tasksEndInFourToSevenDays = RecommendTaskListDTO.builder()
                .title("四到七天内到期")
                .taskDTOList(taskService.getTasksEndBetweenDate(TimeUtils.afterDays(4), TimeUtils.afterDays(7)))
                .build();
        tasksEndInFourToSevenDays.removeCompletedTasks();
        this.removeTasksInMyDayList(tasksEndInFourToSevenDays);
        tasksEndInFourToSevenDays.removeTasksInList(tasksEndToday);
        tasksEndInFourToSevenDays.removeTasksInList(tasksEndInThreeDays);
        // 已经过期，但是没有完成的任务
        RecommendTaskListDTO uncompletedTasksEndBeforeToday = RecommendTaskListDTO.builder()
                .title("先前")
                .taskDTOList(taskService.getUncompletedTasksEndBeforeToday())
                .build();
        uncompletedTasksEndBeforeToday.removeCompletedTasks();
        this.removeTasksInMyDayList(uncompletedTasksEndBeforeToday);
        uncompletedTasksEndBeforeToday.removeTasksInList(tasksEndToday);
        uncompletedTasksEndBeforeToday.removeTasksInList(tasksEndInThreeDays);
        uncompletedTasksEndBeforeToday.removeTasksInList(tasksEndInFourToSevenDays);
        // 最新一天创建的任务
        RecommendTaskListDTO latestCreatedTasks = RecommendTaskListDTO.builder()
                .title("最近添加")
                .taskDTOList(taskService.getTasksCreatedBetween(TimeUtils.before(1), TimeUtils.now()))
                .build();
        latestCreatedTasks.removeCompletedTasks();
        this.removeTasksInMyDayList(latestCreatedTasks);
        latestCreatedTasks.removeTasksInList(tasksEndToday);
        latestCreatedTasks.removeTasksInList(tasksEndInThreeDays);
        latestCreatedTasks.removeTasksInList(tasksEndInFourToSevenDays);
        latestCreatedTasks.removeTasksInList(uncompletedTasksEndBeforeToday);
        return RecommendMyDayDTO.builder()
                .tasksEndToday(tasksEndToday)
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
    public int addTodayDeadlineTaskToMyDayList() {
        List<TaskDTO> tasksEndToday = taskService.getTasksEndToday();
        int count = 0;
        for (TaskDTO taskDTO : tasksEndToday) {
            if (!myDayTaskDAO.existsByTaskId(taskDTO.getId())) {
                MyDayTask myDayTask = MyDayTask.builder()
                        .taskId(taskDTO.getId())
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
