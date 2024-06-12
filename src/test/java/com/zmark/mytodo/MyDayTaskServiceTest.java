package com.zmark.mytodo;

import com.zmark.mytodo.dao.MyDayTaskDAO;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dto.list.RecommendMyDayDTO;
import com.zmark.mytodo.dto.list.RecommendTaskListDTO;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.*;

/**
 * @author ZMark
 * @date 2024/3/29 18:45
 * @see MyDayTaskService 被测对象
 * @see IMyDayTaskService 被测接口
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class MyDayTaskServiceTest {
    public static final Date beforeFourDays = Date.valueOf("2024-06-07");
    public static final Timestamp beforeFourDaysTimestamp = new Timestamp(beforeFourDays.getTime());
    public static final Date beforeThreeDays = Date.valueOf("2024-06-08");
    public static final Timestamp beforeThreeDaysTimestamp = new Timestamp(beforeThreeDays.getTime());
    public static final Date beforeYesterday = Date.valueOf("2024-06-09");
    public static final Timestamp beforeYesterdayTimestamp = new Timestamp(beforeYesterday.getTime());
    public static final Date yesterday = Date.valueOf("2024-06-10");
    public static final Timestamp yesterdayTimestamp = new Timestamp(yesterday.getTime());
    public static final Date today = Date.valueOf("2024-06-11");
    public static final Timestamp todayTimestamp = new Timestamp(today.getTime());
    public static final Date tomorrow = Date.valueOf("2024-06-12");
    public static final Timestamp tomorrowTimestamp = new Timestamp(tomorrow.getTime());
    public static final Date afterThreeDays = Date.valueOf("2024-06-13");
    public static final Timestamp afterThreeDaysTimestamp = new Timestamp(afterThreeDays.getTime());
    public static final Date afterFourDays = Date.valueOf("2024-06-14");
    public static final Timestamp afterFourDaysTimestamp = new Timestamp(afterFourDays.getTime());
    public static final Date afterSevenDays = Date.valueOf("2024-06-15");
    public static final Timestamp afterSevenDaysTimestamp = new Timestamp(afterSevenDays.getTime());

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
        // mock 静态方法 TimeUtils
        mockStatic(TimeUtils.class);
        when(TimeUtils.afterDays(1)).thenReturn(tomorrow);
        when(TimeUtils.afterDays(3)).thenReturn(afterThreeDays);
        when(TimeUtils.afterDays(4)).thenReturn(afterFourDays);
        when(TimeUtils.afterDays(7)).thenReturn(afterSevenDays);
        when(TimeUtils.today()).thenReturn(today);
        when(TimeUtils.before(3)).thenReturn(beforeThreeDaysTimestamp);
        when(TimeUtils.now()).thenReturn(todayTimestamp);
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
                    log.info("模拟获取{}到{}之间未完成的任务", endDateStart, endDateEnd);
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
                                if (reminderTimestamp != null) {
                                    Date reminderDate = new Date(reminderTimestamp.getTime());
                                    if (reminderDate.before(beforeDate)) {
                                        return true;
                                    }
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
    }

    /**
     * 用户故事：我的一天任务列表<br/>
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
        TaskDTO taskOfFullTimeInfoOfToday = saveUncompletedTask(todayTimestamp, todayTimestamp, today, today);
        TaskDTO taskRemindToday = saveUncompletedTask(todayTimestamp, todayTimestamp, null, null);
        TaskDTO taskExpectedToday = saveUncompletedTask(todayTimestamp, null, today, null);
        TaskDTO taskEndToday = saveUncompletedTask(todayTimestamp, null, null, today);
        // 截止时间、提醒时间、规划时间在明天的任务
        TaskDTO taskOfFullTimeInfoOfTomorrow = saveUncompletedTask(tomorrowTimestamp, tomorrowTimestamp, tomorrow, tomorrow);
        TaskDTO taskRemindTomorrow = saveUncompletedTask(tomorrowTimestamp, tomorrowTimestamp, null, null);
        TaskDTO taskExpectedTomorrow = saveUncompletedTask(tomorrowTimestamp, null, tomorrow, null);
        TaskDTO taskEndTomorrow = saveUncompletedTask(tomorrowTimestamp, null, null, tomorrow);
        // 截止时间、提醒时间、规划时间在昨天的任务
        TaskDTO taskOfFullTimeInfoOfYesterday = saveUncompletedTask(yesterdayTimestamp, yesterdayTimestamp, yesterday, yesterday);
        TaskDTO taskRemindYesterday = saveUncompletedTask(yesterdayTimestamp, yesterdayTimestamp, null, null);
        TaskDTO taskExpectedYesterday = saveUncompletedTask(yesterdayTimestamp, null, yesterday, null);
        TaskDTO taskEndYesterday = saveUncompletedTask(yesterdayTimestamp, null, null, yesterday);
        // 截止时间、提醒时间、规划时间在3天后的任务
        TaskDTO taskOfFullTimeInfoOfAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, afterThreeDaysTimestamp, afterThreeDays, afterThreeDays);
        TaskDTO taskEndAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, null, null, afterThreeDays);
        TaskDTO taskRemindAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, beforeThreeDaysTimestamp, null, null);
        TaskDTO taskExpectedAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, null, afterThreeDays, null);

        // 2. when
        List<TaskDTO> myDayTaskList = myDayTaskService.getMyDayList();

        // 3. then 我的一天任务列表应该包含4个任务：
        assertEquals("我的一天任务数量不正确", 4, myDayTaskList.size());
        assertTrue("我的一天中应该有今日截止/规划/提醒的任务", myDayTaskList.contains(taskOfFullTimeInfoOfToday));
        assertTrue("我的一天中应该有今日截止的任务", myDayTaskList.contains(taskEndToday));
        assertTrue("我的一天中应该有今日规划的任务", myDayTaskList.contains(taskExpectedToday));
        assertTrue("我的一天中应该有今日提醒的任务", myDayTaskList.contains(taskRemindToday));
    }

    @Test
    public void test_getMyDayList_empty() {
        // 1. Given: 准备数据
        // 准备Task
        // 不存在截止时间、提醒时间、规划时间在今天的任务（我的一天为空）
        // 截止时间、提醒时间、规划时间在明天的任务
        TaskDTO taskOfFullTimeInfoOfTomorrow = saveUncompletedTask(tomorrowTimestamp, tomorrowTimestamp, tomorrow, tomorrow);
        TaskDTO taskRemindTomorrow = saveUncompletedTask(tomorrowTimestamp, tomorrowTimestamp, null, null);
        TaskDTO taskExpectedTomorrow = saveUncompletedTask(tomorrowTimestamp, null, tomorrow, null);
        TaskDTO taskEndTomorrow = saveUncompletedTask(tomorrowTimestamp, null, null, tomorrow);
        // 截止时间、提醒时间、规划时间在昨天的任务
        TaskDTO taskOfFullTimeInfoOfYesterday = saveUncompletedTask(yesterdayTimestamp, yesterdayTimestamp, yesterday, yesterday);
        TaskDTO taskRemindYesterday = saveUncompletedTask(yesterdayTimestamp, yesterdayTimestamp, null, null);
        TaskDTO taskExpectedYesterday = saveUncompletedTask(yesterdayTimestamp, null, yesterday, null);
        TaskDTO taskEndYesterday = saveUncompletedTask(yesterdayTimestamp, null, null, yesterday);
        // 截止时间、提醒时间、规划时间在3天后的任务
        TaskDTO taskOfFullTimeInfoOfAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, afterThreeDaysTimestamp, afterThreeDays, afterThreeDays);
        TaskDTO taskEndAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, null, null, afterThreeDays);
        TaskDTO taskRemindAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, beforeThreeDaysTimestamp, null, null);
        TaskDTO taskExpectedAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, null, afterThreeDays, null);

        // 2. when
        List<TaskDTO> myDayTaskList = myDayTaskService.getMyDayList();

        // 3. then 我的一天任务列表应该包含0个任务：
        assertEquals("我的一天任务数量不正确", 0, myDayTaskList.size());
    }

    private TaskDTO saveUncompletedTask(Timestamp createTimestamp,
                                        Timestamp reminderTimestamp,
                                        Date expectedExecutionDate,
                                        Date endDate) {
        Long taskId = (long) tasksInDB.size();
        TaskDTO taskDTO = TaskDTO.builder().id(taskId)
                .createTime(createTimestamp)
                .completed(false)
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

    private TaskDTO saveCompletedTask(Timestamp createTimestamp,
                                      Timestamp reminderTimestamp,
                                      Date expectedExecutionDate,
                                      Date endDate) {
        Long taskId = (long) tasksInDB.size();
        TaskDTO taskDTO = TaskDTO.builder().id(taskId)
                .createTime(createTimestamp)
                .completed(true)
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
     * 用户故事：今日建议<br/>
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
        // 未完成的任务：可能在推荐中
        // 截止时间、提醒时间、规划时间在今天的任务(默认全部自动加入我的一天，所以都不在推荐中)
        TaskDTO taskOfFullTimeInfoOfToday = saveUncompletedTask(todayTimestamp, todayTimestamp, today, today);
        TaskDTO taskRemindToday = saveUncompletedTask(todayTimestamp, todayTimestamp, null, null);
        TaskDTO taskExpectedToday = saveUncompletedTask(todayTimestamp, null, today, null);
        TaskDTO taskEndToday = saveUncompletedTask(todayTimestamp, null, null, today);
        // 截止时间、提醒时间、规划时间在明天的任务 (第一个和第四个在 三天内截止中) (第2、3个在 最近创建)
        TaskDTO taskOfFullTimeInfoOfTomorrow = saveUncompletedTask(todayTimestamp, tomorrowTimestamp, tomorrow, tomorrow);
        TaskDTO taskRemindTomorrow = saveUncompletedTask(todayTimestamp, tomorrowTimestamp, null, null);
        TaskDTO taskExpectedTomorrow = saveUncompletedTask(todayTimestamp, null, tomorrow, null);
        TaskDTO taskEndTomorrow = saveUncompletedTask(todayTimestamp, null, null, tomorrow);
        // 截止时间、提醒时间、规划时间在昨天的任务（4个都在 过期）
        TaskDTO taskOfFullTimeInfoOfYesterday = saveUncompletedTask(yesterdayTimestamp, yesterdayTimestamp, yesterday, yesterday);
        TaskDTO taskRemindYesterday = saveUncompletedTask(yesterdayTimestamp, yesterdayTimestamp, null, null);
        TaskDTO taskExpectedYesterday = saveUncompletedTask(yesterdayTimestamp, null, yesterday, null);
        TaskDTO taskEndYesterday = saveUncompletedTask(yesterdayTimestamp, null, null, yesterday);
        // 截止时间、提醒时间、规划时间在3天后的任务 （第1、2个在 三天内截止中）（3、4在最近创建）
        TaskDTO taskOfFullTimeInfoOfAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, afterThreeDaysTimestamp, afterThreeDays, afterThreeDays);
        TaskDTO taskEndAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, null, null, afterThreeDays);
        TaskDTO taskRemindAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, afterThreeDaysTimestamp, null, null);
        TaskDTO taskExpectedAfterThreeDays = saveUncompletedTask(beforeThreeDaysTimestamp, null, afterThreeDays, null);
        // 截止时间、提醒时间、规划时间在4天后的任务 （第1、2个在 4-7天截止中）（其余不在）
        TaskDTO taskOfFullTimeInfoOfAfterFourDays = saveUncompletedTask(beforeFourDaysTimestamp, afterFourDaysTimestamp, afterFourDays, afterFourDays);
        TaskDTO taskEndAfterFourDays = saveUncompletedTask(beforeFourDaysTimestamp, null, null, afterFourDays);
        TaskDTO taskRemindAfterFourDays = saveUncompletedTask(beforeFourDaysTimestamp, afterFourDaysTimestamp, null, null);
        TaskDTO taskExpectedAfterFourDays = saveUncompletedTask(beforeFourDaysTimestamp, null, afterFourDays, null);

        // 已经完成的任务，均不在推荐中
        saveCompletedTask(todayTimestamp, todayTimestamp, today, today);
        saveCompletedTask(todayTimestamp, todayTimestamp, null, null);
        saveCompletedTask(todayTimestamp, null, today, null);
        saveCompletedTask(todayTimestamp, null, null, today);
        saveCompletedTask(todayTimestamp, tomorrowTimestamp, tomorrow, tomorrow);
        saveCompletedTask(todayTimestamp, tomorrowTimestamp, null, null);
        saveCompletedTask(todayTimestamp, null, tomorrow, null);
        saveCompletedTask(todayTimestamp, null, null, tomorrow);
        saveCompletedTask(yesterdayTimestamp, yesterdayTimestamp, yesterday, yesterday);
        saveCompletedTask(yesterdayTimestamp, yesterdayTimestamp, null, null);
        saveCompletedTask(yesterdayTimestamp, null, yesterday, null);
        saveCompletedTask(yesterdayTimestamp, null, null, yesterday);
        saveCompletedTask(beforeThreeDaysTimestamp, afterThreeDaysTimestamp, afterThreeDays, afterThreeDays);
        saveCompletedTask(beforeThreeDaysTimestamp, null, null, afterThreeDays);
        saveCompletedTask(beforeThreeDaysTimestamp, afterThreeDaysTimestamp, null, null);
        saveCompletedTask(beforeThreeDaysTimestamp, null, afterThreeDays, null);
        saveCompletedTask(beforeFourDaysTimestamp, afterFourDaysTimestamp, afterFourDays, afterFourDays);
        saveCompletedTask(beforeFourDaysTimestamp, null, null, afterFourDays);
        saveCompletedTask(beforeFourDaysTimestamp, afterFourDaysTimestamp, null, null);
        saveCompletedTask(beforeFourDaysTimestamp, null, afterFourDays, null);

        // When
        RecommendMyDayDTO recommendMyDayDTO = myDayTaskService.getRecommendTasks();
        RecommendTaskListDTO tasksEndInThreeDays = recommendMyDayDTO.getTasksEndInThreeDays();
        RecommendTaskListDTO tasksEndInFourToSevenDays = recommendMyDayDTO.getTasksEndInFourToSevenDays();
        RecommendTaskListDTO uncompletedTasksBeforeToday = recommendMyDayDTO.getUncompletedTasksBeforeToday();
        RecommendTaskListDTO latestCreatedTasks = recommendMyDayDTO.getLatestCreatedTasks();

        // Then
        assertNotNull("建议中应该包含三天内截止的任务", tasksEndInThreeDays);
        assertEquals("三天内截止的任务数量不正确", 4, tasksEndInThreeDays.getTaskDTOList().size());
        assertTrue("三天内截止的任务应该包含taskEndTomorrow", tasksEndInThreeDays.getTaskDTOList().contains(taskEndTomorrow));
        assertTrue("三天内截止的任务应该包含taskOfFullTimeInfoOfTomorrow", tasksEndInThreeDays.getTaskDTOList().contains(taskOfFullTimeInfoOfTomorrow));
        assertTrue("三天内截止的任务应该包含taskOfFullTimeInfoOfAfterThreeDays", tasksEndInThreeDays.getTaskDTOList().contains(taskOfFullTimeInfoOfAfterThreeDays));
        assertTrue("三天内截止的任务应该包含taskEndAfterThreeDays", tasksEndInThreeDays.getTaskDTOList().contains(taskEndAfterThreeDays));

        assertNotNull("建议中应该包含四到七天内的任务", tasksEndInFourToSevenDays);
        assertEquals("四到七天内截止的任务数量不正确", 2, tasksEndInFourToSevenDays.getTaskDTOList().size());
        assertTrue("四到七天内截止的任务为 taskOfFullTimeInfoOfAfterFourDays", tasksEndInFourToSevenDays.getTaskDTOList().contains(taskOfFullTimeInfoOfAfterFourDays));
        assertTrue("四到七天内截止的任务为 taskEndAfterFourDays", tasksEndInFourToSevenDays.getTaskDTOList().contains(taskEndAfterFourDays));

        assertNotNull("建议中应该包含过去的任务", uncompletedTasksBeforeToday);
        assertEquals("过去的任务数量不正确", 4, uncompletedTasksBeforeToday.getTaskDTOList().size());
        assertTrue("过去的任务应该包含taskOfFullTimeInfoOfYesterday", uncompletedTasksBeforeToday.getTaskDTOList().contains(taskOfFullTimeInfoOfYesterday));
        assertTrue("过去的任务应该包含taskRemindYesterday", uncompletedTasksBeforeToday.getTaskDTOList().contains(taskRemindYesterday));
        assertTrue("过去的任务应该包含taskExpectedYesterday", uncompletedTasksBeforeToday.getTaskDTOList().contains(taskExpectedYesterday));
        assertTrue("过去的任务应该包含taskEndYesterday", uncompletedTasksBeforeToday.getTaskDTOList().contains(taskEndYesterday));

        assertNotNull("建议中应该包含最近(三天)创建的任务", latestCreatedTasks);
        assertEquals("最近(三天)创建的任务数量不正确，得到的任务id列表：" + latestCreatedTasks.getTaskDTOList().stream().map(TaskDTO::getId).toList(), 4, latestCreatedTasks.getTaskDTOList().size());
        assertTrue("最近(三天)创建的任务中应该包含taskRemindTomorrow", latestCreatedTasks.getTaskDTOList().contains(taskRemindTomorrow));
        assertTrue("最近(三天)创建的任务中应该包含taskExpectedTomorrow", latestCreatedTasks.getTaskDTOList().contains(taskExpectedTomorrow));
        assertTrue("最近(三天)创建的任务中应该包含taskRemindAfterThreeDays", latestCreatedTasks.getTaskDTOList().contains(taskRemindAfterThreeDays));
        assertTrue("最近(三天)创建的任务中应该包含taskExpectedAfterThreeDays", latestCreatedTasks.getTaskDTOList().contains(taskExpectedAfterThreeDays));
    }
}