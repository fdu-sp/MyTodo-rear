package com.zmark.mytodo;

import com.zmark.mytodo.bo.list.req.TaskListCreateReq;
import com.zmark.mytodo.dao.TaskGroupDAO;
import com.zmark.mytodo.dao.TaskListDAO;
import com.zmark.mytodo.entity.TaskGroup;
import com.zmark.mytodo.entity.TaskList;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.impl.TaskListService;
import com.zmark.mytodo.service.impl.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Violette
 * @date 2024/4/10 0:44
 * @see TaskListService 被测对象
 */

@Slf4j
@ExtendWith(SpringExtension.class)
public class TaskListServiceTest {

    /**
     * 模拟数据库中数据
     */
    private static List<TaskGroup> taskGroupInDB;
    private static List<TaskList> taskListInDB;
    private static long taskGroupId;  // 当前累计分组id
    public final static Long DEFAULT_GROUP_ID = 1L;

    @MockBean
    TaskService taskService;

    @MockBean
    TaskListDAO taskListDAO;

    @MockBean
    TaskGroupDAO taskGroupDAO;

    /**
     * 被测对象
     */
    private TaskListService taskListService;

    @BeforeAll
    static void init() {
        // 初始化模拟的数据库
        taskGroupInDB = new ArrayList<>();
        taskListInDB = new ArrayList<>();
    }

    @BeforeEach
    void setUp() {
        // 清空模拟数据
        taskGroupInDB.clear();
        taskListInDB.clear();
        taskGroupId = 1L;  // 默认分组id为1
        // 重置模拟对象
        reset(taskService, taskListDAO, taskGroupDAO);
        // 初始化被测对象
        taskListService =
                new TaskListService(taskListDAO, taskGroupDAO, taskService);
        // 设置模拟对象行为
        when(taskGroupDAO.findById(anyLong()))  // 这里的 anyLong是基元类型
                .thenAnswer(invocation -> {
                    // 模拟返回数据
                    int taskGroupId = ((Long) invocation.getArgument(0)).intValue();
//                    log.info("taskGroupId = {}", taskGroupId);
//                    log.info("taskGroupInDB.size() = {}", taskGroupInDB.size());
                    if (taskGroupId > taskGroupInDB.size()) {
                        throw new NoDataInDataBaseException("TaskGroup", taskGroupId);
                    }
//                    log.info("taskGroupInDB.get(taskGroupId) = {}", taskGroupInDB.get(taskGroupId).toString());
                    return taskGroupInDB.get(taskGroupId - 1);  // taskGroupId从1开始
                });
        when(taskListDAO.findByNameAndGroupId(anyString(), anyLong()))
                .thenAnswer(invocationOnMock -> {
                    String name = invocationOnMock.getArgument(0);
                    Long groupId = invocationOnMock.getArgument(1);
                    Optional<TaskList> foundTaskList = taskListInDB.stream()
                            .filter(taskList -> taskList.getName().equals(name))
                            .filter(taskList -> taskList.getGroupId().equals(groupId))
                            .findFirst();
                    return foundTaskList.orElse(null);
                });
        when(taskListDAO.save(any(TaskList.class)))
                .thenAnswer(invocationOnMock -> {
                    TaskList taskList = invocationOnMock.getArgument(0);
                    taskList.setId((long) taskListInDB.size());
                    taskListInDB.add(taskList);
                    log.info("task list {} saved", taskList.getName());
                    return taskList;
                });
    }

    private void createTaskGroup() {
        TaskGroup taskGroup = TaskGroup.builder()
                .id(taskGroupId++)
                .name("分组" + taskGroupInDB.size())
                .description("分组" + taskGroupInDB.size())
                .taskLists(new ArrayList<>())
                .build();
        taskGroupInDB.add(taskGroup);
//        return taskGroup;
    }

    @Test
    public void testCreateNewTaskListNormal() {
        // 添加默认分组
        createTaskGroup();

        // 添加清单，设置分组为默认分组
        TaskListCreateReq taskListCreateReqWithDefaultGroup = new TaskListCreateReq("清单1", "测试", DEFAULT_GROUP_ID);
        assertDoesNotThrow(() -> taskListService.createNewTaskList(taskListCreateReqWithDefaultGroup));

        verify(taskGroupDAO, times(1)).findById(anyLong());
        verify(taskListDAO, times(1)).findByNameAndGroupId(anyString(), anyLong());
        verify(taskListDAO, times(1)).save(any(TaskList.class));

        assertEquals(1, taskListInDB.size());

        // 添加清单，不指定分组，service函数会自动选择默认分组
        TaskListCreateReq taskListCreateReqWithNullGroup = new TaskListCreateReq("清单2", "测试", null);
        assertDoesNotThrow(() -> taskListService.createNewTaskList(taskListCreateReqWithNullGroup));

        verify(taskGroupDAO, times(2)).findById(anyLong());
        verify(taskListDAO, times(2)).findByNameAndGroupId(anyString(), anyLong());
        verify(taskListDAO, times(2)).save(any(TaskList.class));

        assertEquals(2, taskListInDB.size());
    }


}
