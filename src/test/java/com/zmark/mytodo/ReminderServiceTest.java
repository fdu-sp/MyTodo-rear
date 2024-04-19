package com.zmark.mytodo;


import com.zmark.mytodo.dao.TaskTimeInfoDAO;
import com.zmark.mytodo.dto.reminder.TaskReminderInfo;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.TaskTimeInfo;
import com.zmark.mytodo.service.api.IReminderService;
import com.zmark.mytodo.service.impl.ReminderService;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * @author zouzouyi
 * @date 2024/4/10
 * @see ReminderService 被测对象
 * @see IReminderService 被测接口
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class ReminderServiceTest {

    //声明全局常量
    public static final Date createDate1 = Date.valueOf("2024-03-27");
    public static final Timestamp createTimeStamp1 = new Timestamp(createDate1.getTime());
    public static final Timestamp remindTimeStamp1 = new Timestamp(Date.valueOf("2024-04-05").getTime());
    public static final Date createDate2 = Date.valueOf("2024-03-29");
    public static final Timestamp createTimeStamp2 = new Timestamp(createDate2.getTime());
    public static final Timestamp remindTimeStamp2 = null;
    public static final Date createDate3 = Date.valueOf("2024-04-09");
    public static final Timestamp createTimeStamp3 = new Timestamp(createDate3.getTime());
    public static final Timestamp remindTimeStamp3 = new Timestamp(Date.valueOf("2024-04-22").getTime());
    public static final Date createDate4 = Date.valueOf("2024-03-28");
    public static final Timestamp createTimeStamp4 = new Timestamp(createDate4.getTime());
    public static final Timestamp remindTimeStamp4 = new Timestamp(Date.valueOf("2024-04-22").getTime());
    public static final Date createDate5 = Date.valueOf("2024-03-29");
    public static final Timestamp createTimeStamp5 = new Timestamp(createDate5.getTime());
    public static final Timestamp remindTimeStamp5 = new Timestamp(Date.valueOf("2024-04-22").getTime());

    public static final Date expectedExecutionDate = Date.valueOf("2024-04-23");
    public static final Date endDate = Date.valueOf("2024-04-23");

    public Timestamp currentTimeStamp = null;

    @MockBean
    private TaskTimeInfoDAO taskTimeInfoDAO;

    /**
     * 待测对象
     */
    private ReminderService reminderService;

    /**
     * 模拟数据库中的task对象
     */
    private static List<TaskDTO> tasksInDB;


    @BeforeAll
    public static void init() {
        //初始化数据
        tasksInDB = new ArrayList<>();
    }
    @BeforeEach
    public void setUp() {
        //清空数据
        tasksInDB.clear();
        // 重置mock对象
        reset(taskTimeInfoDAO);
        // 初始化被测对象
        reminderService = new ReminderService(taskTimeInfoDAO);

        //mock对象行为
        when(taskTimeInfoDAO.findAllByReminderTimestampAfterAndTask_Completed(
                any(Timestamp.class),
                anyBoolean()
        )).thenAnswer(invocationOnMock -> {
           currentTimeStamp = invocationOnMock.getArgument(0, Timestamp.class);
            Boolean completed = invocationOnMock.getArgument(1, Boolean.class);

            // 过滤 tasksInDB，基于条件提取并创建 TaskTimeInfo 对象列表
            return tasksInDB.stream()
                    .filter(taskDTO ->
                            taskDTO.getTaskTimeInfo() != null &&
                                    taskDTO.getTaskTimeInfo().getReminderTimestamp() != null &&
                                    taskDTO.getTaskTimeInfo().getReminderTimestamp().after(currentTimeStamp) &&
                                    taskDTO.getCompleted().equals(completed))
                    .map(taskDTO -> {
                        // 创建新的 TaskTimeInfo 对象，设置必要的信息
                      TaskTimeInfo  timeInfo = taskDTO.getTaskTimeInfo();
                      long taskId = taskDTO.getId();
                      Task task = new Task();
                      task.setId(taskId);
                      timeInfo.setTask(new Task());
                      log.info("Processing Task ID: {}, Reminder Timestamp: {}, Completed: {}",
                                taskDTO.getId(),
                                taskDTO.getTaskTimeInfo().getReminderTimestamp(),
                                taskDTO.getCompleted());
                        return timeInfo;
                    })
                    .collect(Collectors.toList());
        });

}


    @Test
    public void test_getTaskReminderInfoList() {


        //1. given 用户已经添加了带有提醒时间信息的任务
        // 准备task数据
        TaskDTO taskDTO1 = saveReminderTask(createTimeStamp1, remindTimeStamp1);
        TaskDTO taskDTO2 = saveReminderTask(createTimeStamp2,remindTimeStamp2);
        TaskDTO taskDTO3 = saveReminderTask(createTimeStamp3,remindTimeStamp3);
        TaskDTO taskDTO4 = saveReminderTask(createTimeStamp4,remindTimeStamp4);
        TaskDTO taskDTO5 = saveReminderTask(createTimeStamp5,remindTimeStamp5);


        //2. when 到达提醒时间出发提醒机制
        List<TaskReminderInfo> taskReminderInfoList = reminderService.getTaskReminderInfoList();

        //3. 用户收到提醒消息
        // 首先检查任务数量是否正确
        assertEquals("收到提醒的任务数量不正确", 3, taskReminderInfoList.size());
        // 然后，对于每一个任务，验证其提醒时间戳是否晚于currentTimeStamp
        for (TaskReminderInfo taskReminderInfo : taskReminderInfoList) {
            assertTrue("任务的提醒时间戳应晚于currentTimeStamp",
                    taskReminderInfo.getReminderTimestamp().after(currentTimeStamp));
        }

    }

    //保存任务
    private TaskDTO saveReminderTask(Timestamp createTimestamp,
                                        Timestamp reminderTimestamp) {
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
        return taskDTO;
    }

}
