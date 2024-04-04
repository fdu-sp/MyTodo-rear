package com.zmark.mytodo;

import com.zmark.mytodo.bo.list.resp.TaskListSimpleResp;
import com.zmark.mytodo.bo.quadrant.resp.FourQuadrantDetailResp;
import com.zmark.mytodo.bo.quadrant.resp.OneQuadrantDetailResp;
import com.zmark.mytodo.dto.list.TaskListDTO;
import com.zmark.mytodo.dto.list.TaskListSimpleDTO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.TaskContentInfo;
import com.zmark.mytodo.entity.TaskPriorityInfo;
import com.zmark.mytodo.entity.TaskTimeInfo;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.impl.FourQuadrantService;
import com.zmark.mytodo.service.impl.MyDayTaskService;
import com.zmark.mytodo.service.impl.TaskListService;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * @author ZMark
 * @date 2024/4/4 14:52
 * @see com.zmark.mytodo.service.impl.FourQuadrantService 被测试的类
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class FourQuadrantServiceTest {

    /**
     * 模拟数据库中数据
     */
    private static List<TaskListDTO> taskListInDB;
    private static List<TaskDTO> myDayTaskList;
    private static TaskListSimpleDTO myDayTaskListSimple;
    private static long taskId = 0L;

    @MockBean
    TaskListService taskListService;

    @MockBean
    MyDayTaskService myDayTaskService;

    /**
     * 被测对象
     */
    private FourQuadrantService fourQuadrantService;

    @BeforeAll
    static void init() {
        // 初始化模拟数据
        taskListInDB = new ArrayList<>();
        myDayTaskList = new ArrayList<>();
        myDayTaskListSimple = TaskListSimpleDTO.builder()
                .id(1L)
                .name("我的一天")
                .description("我的一天")
                .build();
    }

    @BeforeEach
    void setUp() throws NoDataInDataBaseException {
        // 清空模拟数据
        taskListInDB.clear();
        myDayTaskList.clear();
        myDayTaskListSimple = TaskListSimpleDTO.builder()
                .id(1L)
                .name("我的一天")
                .description("我的一天")
                .build();
        taskId = 0L;
        // 重置模拟对象
        reset(taskListService, myDayTaskService);
        // 初始化被测对象
        fourQuadrantService =
                new FourQuadrantService(taskListService, myDayTaskService);
        // 设置模拟对象行为
        when(taskListService.findById(anyLong()))
                .thenAnswer(invocation -> {
                    // 模拟返回数据
                    int taskListId = ((Long) invocation.getArgument(0)).intValue();
                    if (taskListId >= taskListInDB.size()) {
                        throw new NoDataInDataBaseException("TaskList", taskListId);
                    }
                    return taskListInDB.get(taskListId);
                });
        when(myDayTaskService.getMyDayList())
                .thenReturn(myDayTaskList);
        when(myDayTaskService.getMyDayTaskListSimple())
                .thenReturn(myDayTaskListSimple);
    }

    private TaskListDTO createTaskList() {
        TaskListDTO taskListDTO = TaskListDTO.builder()
                .id((long) taskListInDB.size())
                .name("任务列表" + taskListInDB.size())
                .description("任务列表" + taskListInDB.size())
                .taskDTOList(new ArrayList<>())
                .build();
        taskListInDB.add(taskListDTO);
        return taskListDTO;
    }

    /**
     * 向指定的清单中添加指定优先级的任务
     *
     * @param taskListId  清单ID
     * @param isImportant 是否重要
     *                    true: 重要
     *                    false: 不重要
     * @param isUrgent    是否紧急
     *                    true: 紧急
     *                    false: 不紧急
     */
    private TaskDTO addTask(long taskListId, boolean isImportant, boolean isUrgent) {
        if (taskListId >= taskListInDB.size()) {
            throw new IllegalArgumentException("清单ID不存在");
        }
        TaskListDTO taskListDTO = taskListInDB.get((int) taskListId);
        TaskPriorityInfo taskPriorityInfo = TaskPriorityInfo.builder()
                .isImportant(isImportant)
                .isUrgent(isUrgent)
                .build();
        TaskDTO taskDTO = TaskDTO.builder()
                .id(taskId)
                .title("任务" + taskId)
                .taskListId(taskListId)
                .taskListName(taskListDTO.getName())
                .taskPriorityInfo(taskPriorityInfo)
                .taskContentInfo(TaskContentInfo.builder()
                        .description("任务" + taskId)
                        .build())
                .taskTimeInfo(TaskTimeInfo.builder()
                        .endDate(TimeUtils.today())
                        .build())
                .tags(new ArrayList<>())
                .build();
        taskId++;
        taskListDTO.getTaskDTOList().add(taskDTO);
        return taskDTO;
    }

    /**
     * 后端自动化测试-处理每个清单中的四象限数据<br/>
     * - Given 用户已经有一个清单，并且该清单中添加了多个带有不同优先级的任务<br/>
     * - When 系统处理该清单的四象限视图中的任务数据<br/>
     * - Then 系统应根据该清单中待办事项的优先级划分，正确分配这些待办事项到对应的四个象限的数据结构中<br/>
     */
    @Test
    public void test_getFourQuadrantDetailByList() throws NoDataInDataBaseException {
        TaskListDTO list1 = createTaskList();
        long listId = list1.getId();
        long anotherListId = createTaskList().getId();
        // Given
        TaskDTO urgentAndImportantTask1 = addTask(listId, true, true);
        TaskDTO urgentAndImportantTask2 = addTask(listId, true, true);
        TaskDTO notUrgentButImportantTask = addTask(listId, true, false);
        TaskDTO urgentButNotImportantTask = addTask(listId, false, true);
        TaskDTO notUrgentAndNotImportantTask = addTask(listId, false, false);

        addTask(anotherListId, true, true);
        addTask(anotherListId, true, true);
        addTask(anotherListId, true, false);
        addTask(anotherListId, false, true);
        addTask(anotherListId, false, false);


        // When
        FourQuadrantDetailResp resp = fourQuadrantService.getFourQuadrantDetailByList(listId);

        // Then
        // 断言四象限数据
        TaskListSimpleResp taskListInfo = resp.getTaskListInfo();
        OneQuadrantDetailResp urgentAndImportant = resp.getUrgentAndImportant();
        OneQuadrantDetailResp urgentAndNotImportant = resp.getUrgentAndNotImportant();
        OneQuadrantDetailResp notUrgentAndImportant = resp.getNotUrgentAndImportant();
        OneQuadrantDetailResp notUrgentAndNotImportant = resp.getNotUrgentAndNotImportant();
        assertEquals("目标清单id不正确", listId, taskListInfo.getId());
        assertEquals("目标清单名称不正确", list1.getName(), taskListInfo.getName());
        assertEquals("目标清单任务数量不正确", 5L, taskListInfo.getCount());
        assertEquals("紧急且重要任务数量不正确", 2, urgentAndImportant.getTasks().size());
        assertTrue("紧急且重要任务不包含目标任务1", urgentAndImportant.getTasks().stream().anyMatch(task -> task.getId().equals(urgentAndImportantTask1.getId())));
        assertTrue("紧急且重要任务不包含目标任务2", urgentAndImportant.getTasks().stream().anyMatch(task -> task.getId().equals(urgentAndImportantTask2.getId())));
        assertEquals("紧急不重要任务数量不正确", 1, urgentAndNotImportant.getTasks().size());
        assertTrue("紧急不重要任务不包含目标任务", urgentAndNotImportant.getTasks().stream().anyMatch(task -> task.getId().equals(urgentButNotImportantTask.getId())));
        assertEquals("不紧急但重要任务数量不正确", 1, notUrgentAndImportant.getTasks().size());
        assertTrue("不紧急但重要任务不包含目标任务", notUrgentAndImportant.getTasks().stream().anyMatch(task -> task.getId().equals(notUrgentButImportantTask.getId())));
        assertEquals("不紧急不重要任务数量不正确", 1, notUrgentAndNotImportant.getTasks().size());
        assertTrue("不紧急不重要任务不包含目标任务", notUrgentAndNotImportant.getTasks().stream().anyMatch(task -> task.getId().equals(notUrgentAndNotImportantTask.getId())));
    }

    /**
     * 场景2：后端自动化测试-处理“我的一天”中的四象限数据<br/>
     * - Given 用户已经添加了多个带有不同优先级的任务<br/>
     * - When 系统处理“我的一天”视图中的任务数据<br/>
     * - Then 系统应根据当日待办事项的优先级划分，正确分配这些待办事项到对应的四个象限的数据结构中<br/>
     */
    @Test
    public void test_getFourQuadrantDetailByMyDay() {

    }
}
