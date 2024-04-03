package com.zmark.mytodo;

import com.zmark.mytodo.dao.MyDayTaskDAO;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dto.list.RecommendMyDayDTO;
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
    public static final Date beforeFourDays = Date.valueOf("2024-03-25");
    public static final Timestamp beforeFourDaysTimestamp = new Timestamp(beforeFourDays.getTime());
    public static final Date beforeThreeDays = Date.valueOf("2024-03-26");
    public static final Timestamp beforeThreeDaysTimestamp = new Timestamp(beforeThreeDays.getTime());
    public static final Date beforeYesterday = Date.valueOf("2024-03-27");
    public static final Timestamp beforeYesterdayTimestamp = new Timestamp(beforeYesterday.getTime());
    public static final Date yesterday = Date.valueOf("2024-03-28");
    public static final Timestamp yesterdayTimestamp = new Timestamp(yesterday.getTime());
    public static final Date today = Date.valueOf("2024-03-29");
    public static final Timestamp todayTimestamp = new Timestamp(today.getTime());
    public static final Date tomorrow = Date.valueOf("2024-03-30");
    public static final Timestamp tomorrowTimestamp = new Timestamp(tomorrow.getTime());
    public static final Date afterThreeDays = Date.valueOf("2024-04-01");
    public static final Timestamp afterThreeDaysTimestamp = new Timestamp(afterThreeDays.getTime());

    /**
     * 模拟数据库中的task
     */
    private static List<TaskDTO> tasksInDB;
    /**
     * 模拟数据库中的MyDayTaskList<br/>
     * key: taskId, value: MyDayTask
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
        // mock TaskService
        when(taskService.findTaskById(anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long id = invocationOnMock.getArgument(0);
                    log.info("模拟查询id为{}的任务", id);
                    return tasksInDB.stream()
                            .filter(task -> task.getId().equals(id))
                            .findFirst()
                            .orElse(null);
                });
        when(taskService.getUncompletedTasksEndBetweenDate(any(Date.class), any(Date.class)))
                .thenAnswer(invocationOnMock -> {
                    Date endDateStart = invocationOnMock.getArgument(0);
                    Date endDateEnd = invocationOnMock.getArgument(1);
                    log.info("模拟获取{}-{}之间未完成的任务", endDateStart, endDateEnd);
                    return tasksInDB.stream()
                            .filter(task -> {
                                if (task.getCompleted()) {
                                    return false;
                                }
                                if (task.getTaskTimeInfo() == null) {
                                    return false;
                                }
                                Date endDate = task.getTaskTimeInfo().getEndDate();
                                return endDate != null && endDate.compareTo(endDateStart) >= 0 && endDate.compareTo(endDateEnd) <= 0;
                            })
                            .toList();
                });
        when(taskService.getUncompletedTasksBefore(any(Date.class)))
                .thenAnswer(invocationOnMock -> {
                    // 已经过期（截止日期、提醒时间、规划执行时间），但是没有完成的任务
                    Date beforeDate = invocationOnMock.getArgument(0);
                    log.info("模拟获取在{}之前未完成的任务", beforeDate);
                    return tasksInDB.stream()
                            .filter(task -> {
                                if (task.getCompleted()) {
                                    return false;
                                }
                                if (task.getTaskTimeInfo() == null) {
                                    return false;
                                }
                                Date endDate = task.getTaskTimeInfo().getEndDate();
                                if (endDate != null && endDate.before(beforeDate)) {
                                    return true;
                                }
                                Timestamp reminderTimestamp = task.getTaskTimeInfo().getReminderTimestamp();
                                if (reminderTimestamp == null) {
                                    return false;
                                }
                                Date reminderDate = new Date(reminderTimestamp.getTime());
                                if (reminderDate.before(beforeDate)) {
                                    return true;
                                }
                                Date expectedExecuteDate = task.getTaskTimeInfo().getExpectedExecutionDate();
                                return expectedExecuteDate != null && expectedExecuteDate.before(beforeDate);
                            })
                            .toList();
                });
        when((taskService.getTasksCreatedBetween(any(Timestamp.class), any(Timestamp.class))))
                .thenAnswer(invocationOnMock -> {
                    Timestamp startTime = invocationOnMock.getArgument(0);
                    Timestamp endTime = invocationOnMock.getArgument(1);
                    log.info("模拟查询{}-{}之间创建的任务", startTime, endTime);
                    return tasksInDB.stream()
                            .filter(task -> {
                                Timestamp createTime = task.getCreateTime();
                                return createTime != null && createTime.compareTo(startTime) >= 0 && createTime.compareTo(endTime) <= 0;
                            })
                            .toList();
                });
        // mock MyDayTaskDAO
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
        when(myDayTaskDAO.findAll())
                .thenAnswer(invocation -> {
                    log.info("模拟查询我的一天列表");
                    return new ArrayList<>(myDayTaskListInDB.values());
                });
        // todo mock taskDAO?
        when(taskDAO.findAllByTaskTimeInfo_EndDate(any(Date.class)))
                .thenAnswer(invocationOnMock -> {
                    log.info("模拟查询某日截止的任务");
                    // todo
                    Date today = TimeUtils.today();
                    return null;
                });
    }

    /**
     * 测试场景：后端自动化测试-我的一天自动添加任务<br>
     * - Given 用户已经添加了多个带有不同时间信息的任务<br>
     * - When 到达新的一天（设定为具体的某个时间点T）<br>
     * - Then “我的一天”清单中会显示今日截止的任务、设定今日提醒的任务、规划今日执行的任务<br>
     * 实际上，由于系统有计时器，会在每天凌晨0点执行，来更新我的一天列表，而我们这里只是模拟了计时器的发生
     *
     * @see MyDayTaskService#getMyDayList()
     */
    @Test
    public void test_getMyDayList() {
        // 1. Given: 准备数据
        // 准备Task
        // 截止时间、提醒时间、规划时间在今天的任务
        TaskDTO taskOfFullTimeInfoOfToday = saveTask(todayTimestamp, todayTimestamp, today, today);
        TaskDTO taskRemindToday = saveTask(todayTimestamp, todayTimestamp, null, null);
        TaskDTO taskExpectedToday = saveTask(todayTimestamp, null, today, null);
        TaskDTO taskEndToday = saveTask(todayTimestamp, null, null, today);
        // 截止时间、提醒时间、规划时间在明天的任务
        TaskDTO taskOfFullTimeInfoOfTomorrow = saveTask(tomorrowTimestamp, tomorrowTimestamp, tomorrow, tomorrow);
        TaskDTO taskRemindTomorrow = saveTask(tomorrowTimestamp, tomorrowTimestamp, null, null);
        TaskDTO taskExpectedTomorrow = saveTask(tomorrowTimestamp, null, tomorrow, null);
        TaskDTO taskEndTomorrow = saveTask(tomorrowTimestamp, null, null, tomorrow);
        // 截止时间、提醒时间、规划时间在昨天的任务
        TaskDTO taskOfFullTimeInfoOfYesterday = saveTask(yesterdayTimestamp, yesterdayTimestamp, yesterday, yesterday);
        TaskDTO taskRemindYesterday = saveTask(yesterdayTimestamp, yesterdayTimestamp, null, null);
        TaskDTO taskExpectedYesterday = saveTask(yesterdayTimestamp, null, yesterday, null);
        TaskDTO taskEndYesterday = saveTask(yesterdayTimestamp, null, null, yesterday);
        // 截止时间、提醒时间、规划时间在3天后的任务
        TaskDTO taskOfFullTimeInfoOfAfterThreeDays = saveTask(beforeThreeDaysTimestamp, afterThreeDaysTimestamp, afterThreeDays, afterThreeDays);
        TaskDTO taskEndAfterThreeDays = saveTask(beforeThreeDaysTimestamp, null, null, afterThreeDays);
        TaskDTO taskRemindAfterThreeDays = saveTask(beforeThreeDaysTimestamp, beforeThreeDaysTimestamp, null, null);
        TaskDTO taskExpectedAfterThreeDays = saveTask(beforeThreeDaysTimestamp, null, afterThreeDays, null);

        // 2. when
        List<TaskDTO> myDayTaskList = myDayTaskService.getMyDayList();

        // 3. then 我的一天任务列表应该包含4个任务：
        assertEquals("我的一天任务数量不正确", 4, myDayTaskList.size());
        assertTrue("我的一天中应该有今日截止/规划/提醒的任务", myDayTaskList.contains(taskOfFullTimeInfoOfToday));
        assertTrue("我的一天中应该有今日截止的任务", myDayTaskList.contains(taskEndToday));
        assertTrue("我的一天中应该有今日规划的任务", myDayTaskList.contains(taskExpectedToday));
        assertTrue("我的一天中应该有今日提醒的任务", myDayTaskList.contains(taskRemindToday));
    }

    private TaskDTO saveTask(Timestamp createTimestamp,
                             Timestamp reminderTimestamp,
                             Date expectedExecutionDate,
                             Date endDate) {
        Long taskId = (long) tasksInDB.size();
        TaskDTO taskDTO = TaskDTO.builder().id(taskId)
                .createTime(createTimestamp)
                .taskTimeInfo(TaskTimeInfo.builder()
                        .expectedExecutionDate(expectedExecutionDate)
                        .reminderTimestamp(reminderTimestamp)
                        .endDate(endDate)
                        .build()).build();
        tasksInDB.add(taskDTO);
        if ((endDate != null && endDate.equals(today))
                || (expectedExecutionDate != null && expectedExecutionDate.equals(today))
                || (reminderTimestamp != null && new Date(reminderTimestamp.getTime()).equals(today))) {
            // 模拟MyDayTaskScheduler：自动添加到我的一天中
            myDayTaskListInDB.put(taskId, MyDayTask.builder()
                    .taskId(taskId).build());
        }
        return taskDTO;
    }

    /**
     * 后端自动化测试-显示当天任务的建议<br/>
     * - Given 用户已经添加了多个带有不同时间信息的任务<br/>
     * - When 到达新的一天（设定为具体的某个时间点T）<br/>
     * - Then 系统推荐的今日任务中会包括：（都是没有完成的任务）截止日期为之后三天的任务、截止日期为之后四到七天的任务、过去（截止、提醒、规划）的任务、最近三天创建的任务<br/>
     *
     * @see MyDayTaskService#getRecommendTasks()
     */
    @Test
    public void test_getRecommendTasks() {
        // Given 用户已经添加了多个带有不同时间信息的任务
        // 准备Task
        // 截止时间、提醒时间、规划时间在今天的任务
        TaskDTO taskOfFullTimeInfoOfToday = saveTask(todayTimestamp, todayTimestamp, today, today);
        TaskDTO taskRemindToday = saveTask(todayTimestamp, todayTimestamp, null, null);
        TaskDTO taskExpectedToday = saveTask(todayTimestamp, null, today, null);
        TaskDTO taskEndToday = saveTask(todayTimestamp, null, null, today);
        // 截止时间、提醒时间、规划时间在明天的任务
        TaskDTO taskOfFullTimeInfoOfTomorrow = saveTask(tomorrowTimestamp, tomorrowTimestamp, tomorrow, tomorrow);
        TaskDTO taskRemindTomorrow = saveTask(tomorrowTimestamp, tomorrowTimestamp, null, null);
        TaskDTO taskExpectedTomorrow = saveTask(tomorrowTimestamp, null, tomorrow, null);
        TaskDTO taskEndTomorrow = saveTask(tomorrowTimestamp, null, null, tomorrow);
        // 截止时间、提醒时间、规划时间在昨天的任务
        TaskDTO taskOfFullTimeInfoOfYesterday = saveTask(yesterdayTimestamp, yesterdayTimestamp, yesterday, yesterday);
        TaskDTO taskRemindYesterday = saveTask(yesterdayTimestamp, yesterdayTimestamp, null, null);
        TaskDTO taskExpectedYesterday = saveTask(yesterdayTimestamp, null, yesterday, null);
        TaskDTO taskEndYesterday = saveTask(yesterdayTimestamp, null, null, yesterday);
        // 截止时间、提醒时间、规划时间在3天后的任务
        TaskDTO taskOfFullTimeInfoOfAfterThreeDays = saveTask(beforeThreeDaysTimestamp, afterThreeDaysTimestamp, afterThreeDays, afterThreeDays);
        TaskDTO taskEndAfterThreeDays = saveTask(beforeThreeDaysTimestamp, null, null, afterThreeDays);
        TaskDTO taskRemindAfterThreeDays = saveTask(beforeThreeDaysTimestamp, beforeThreeDaysTimestamp, null, null);
        TaskDTO taskExpectedAfterThreeDays = saveTask(beforeThreeDaysTimestamp, null, afterThreeDays, null);

        // When
        RecommendMyDayDTO recommendMyDayDTO = myDayTaskService.getRecommendTasks();

        // Then

    }
}