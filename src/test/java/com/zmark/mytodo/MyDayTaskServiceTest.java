package com.zmark.mytodo;

import com.zmark.mytodo.dao.MyDayTaskDAO;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.MyDayTask;
import com.zmark.mytodo.entity.TaskTimeInfo;
import com.zmark.mytodo.service.api.IMyDayTaskService;
import com.zmark.mytodo.service.impl.MyDayTaskService;
import com.zmark.mytodo.service.impl.TaskService;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * @author ZMark
 * @date 2024/3/29 18:45
 * @see MyDayTaskService 被测对象
 * @see IMyDayTaskService 被测接口
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class MyDayTaskServiceTest {
    public static final Date today = Date.valueOf("2024-03-29");
    public static final Timestamp todayTimestamp = new Timestamp(today.getTime());
    public static final Date yesterday = Date.valueOf("2024-03-28");
    public static final Timestamp yesterdayTimestamp = new Timestamp(yesterday.getTime());
    public static final Date tomorrow = Date.valueOf("2024-03-30");
    public static final Timestamp tomorrowTimestamp = new Timestamp(tomorrow.getTime());
    public static final Date afterThreeDays = Date.valueOf("2024-04-01");
    /**
     * 模拟数据库中的task
     */
    private static List<TaskDTO> tasksInDB;
    /**
     * 模拟数据库中的MyDayTaskList
     */
    private static Map<Long, MyDayTask> myDayTaskListInDB;
    @MockBean
    private TaskService taskService;
    @MockBean
    private MyDayTaskDAO myDayTaskDAO;
    @MockBean
    private TaskDAO taskDAO;
    /**
     * 被测对象
     */
    private MyDayTaskService myDayTaskService;

    @BeforeAll
    static void init() {
        // 初始化模拟数据
        tasksInDB = new ArrayList<>();
        myDayTaskListInDB = new HashMap<>();
    }

    @BeforeEach
    void setUp() {
        // 清空模拟数据
        tasksInDB.clear();
        myDayTaskListInDB.clear();
        // 重置mock对象
        reset(taskService, myDayTaskDAO, taskDAO);
        // 初始化被测对象
        myDayTaskService = new
                MyDayTaskService(taskService, myDayTaskDAO, taskDAO);
        // 设置mock对象的行为
        when(taskService.getTasksEndToday())
                .thenAnswer(invocation -> {
                    log.info("模拟获取今日截止的任务");
                    return tasksInDB.stream().filter(taskDTO -> {
                                Date endDate = taskDTO.getTaskTimeInfo().getEndDate();
                                return endDate != null && endDate.equals(today);
                            }
                    ).toList();
                });
        when(myDayTaskDAO.existsByTaskId(anyLong()))
                .thenAnswer(invocation -> {
                    log.info("模拟查询我的一天列表中是否存在指定任务");
                    Long taskId = invocation.getArgument(0);
                    return myDayTaskListInDB.containsKey(taskId);
                });
        when(myDayTaskDAO.save(any(MyDayTask.class)))
                .thenAnswer(invocation -> {
                    log.info("模拟保存任务到我的一天列表");
                    MyDayTask myDayTask = invocation.getArgument(0);
                    myDayTask.setId((long) myDayTaskListInDB.size());
                    myDayTaskListInDB.put(myDayTask.getTaskId(), myDayTask);
                    log.info("保存成功，myDayTask: {}", myDayTask);
                    return myDayTask;
                });
        // todo mock taskDAO?
        when(taskDAO.findAllByTaskTimeInfo_EndDate(any(Date.class)))
                .thenAnswer(invocationOnMock -> {
                    log.info("模拟查询某日截止的任务");
                    Date today = TimeUtils.today();
                    return null;
                });
    }


    /**
     * 测试场景：后端自动化测试-我的一天自动添加任务<br>
     * - Given 用户已经添加了多个带有不同时间信息的任务<br>
     * - When 到达新的一天（设定为具体的某个时间点T）<br>
     * - Then “我的一天”清单中会显示今日截止的任务、设定今日提醒的任务、规划今日执行的任务
     *
     * @see MyDayTaskService#getMyDayList()
     */
    @Test
    @Disabled("无法自动化测试")
    public void test_getMyDayList() {
        // 1. Given: 准备数据
        // 准备Task
        // 截止时间、提醒时间、规划时间在今天的任务
        TaskDTO taskEndToday = TaskDTO.builder().id(1L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .endDate(today)
                        .build()).build();
        TaskDTO taskRemindToday = TaskDTO.builder().id(2L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .reminderTimestamp(todayTimestamp)
                        .build()).build();
        TaskDTO taskExpectedToday = TaskDTO.builder().id(3L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .expectedExecutionDate(today)
                        .build()).build();
        tasksInDB.add(taskEndToday);
        tasksInDB.add(taskExpectedToday);
        tasksInDB.add(taskRemindToday);
        // 截止时间、提醒时间、规划时间在昨天的任务
        TaskDTO taskEndYesterday = TaskDTO.builder().id(4L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .endDate(yesterday)
                        .build()).build();
        TaskDTO taskRemindYesterday = TaskDTO.builder().id(5L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .reminderTimestamp(yesterdayTimestamp)
                        .build()).build();
        TaskDTO taskExpectedYesterday = TaskDTO.builder().id(6L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .expectedExecutionDate(yesterday)
                        .build()).build();
        tasksInDB.add(taskEndYesterday);
        tasksInDB.add(taskExpectedYesterday);
        tasksInDB.add(taskRemindYesterday);
        // 截止时间、提醒时间、规划时间在明天的任务
        TaskDTO taskEndTomorrow = TaskDTO.builder().id(7L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .endDate(tomorrow)
                        .build()).build();
        TaskDTO taskRemindTomorrow = TaskDTO.builder().id(8L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .reminderTimestamp(tomorrowTimestamp)
                        .build()).build();
        TaskDTO taskExpectedTomorrow = TaskDTO.builder().id(9L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .expectedExecutionDate(tomorrow)
                        .build()).build();
        tasksInDB.add(taskEndTomorrow);
        tasksInDB.add(taskExpectedTomorrow);
        tasksInDB.add(taskRemindTomorrow);
        // 几天后的任务
        TaskDTO taskEndAfterThreeDays = TaskDTO.builder().id(10L)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .endDate(afterThreeDays)
                        .build()).build();
        tasksInDB.add(taskEndAfterThreeDays);
        // 昨天已经在我的一天中的任务
        myDayTaskListInDB.put(taskEndYesterday.getId(), MyDayTask.builder()
                .taskId(taskEndYesterday.getId()).build());
        myDayTaskListInDB.put(taskExpectedYesterday.getId(), MyDayTask.builder()
                .taskId(taskExpectedYesterday.getId()).build());
        myDayTaskListInDB.put(taskRemindYesterday.getId(), MyDayTask.builder
                ().taskId(taskRemindYesterday.getId()).build());
        // 模拟系统的定时器

        // 2. when
        List<TaskDTO> myDayTaskList = myDayTaskService.getMyDayList();

        // 3. then 我的一天任务列表应该包含3个任务：今日的三个任务
        assertEquals("我的一天任务数量不正确", 3, myDayTaskList.size());
        assertTrue("我的一天中应该有今日截止的任务", myDayTaskList.contains(taskEndToday));
        assertTrue("我的一天中应该有今日规划的任务", myDayTaskList.contains(taskExpectedToday));
        assertTrue("我的一天中应该有今日提醒的任务", myDayTaskList.contains(taskRemindToday));
    }
}
