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
 * @see com.zmark.mytodo.service.impl.TaskListService 被测试的类
 */

@Slf4j
@ExtendWith(SpringExtension.class)
public class TaskListServiceTest {

    /**
     * 模拟数据库中数据
     */
    private static List<TaskGroup> taskGroupInDB;
    private static List<TaskList> taskListInDB;

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
        // 初始化模拟数据
        taskGroupInDB = new ArrayList<>();
        taskListInDB = new ArrayList<>();
    }

    @BeforeEach
    void setUp() {
        // 清空模拟数据
        taskGroupInDB.clear();
        taskListInDB.clear();
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
                    log.info("taskGroupId = {}", taskGroupId);
                    log.info("taskGroupInDB.size() = {}", taskGroupInDB.size());
                    if (taskGroupId >= taskGroupInDB.size()) {
                        throw new NoDataInDataBaseException("TaskGroup", taskGroupId);
                    }
                    log.info("taskGroupInDB.get(taskGroupId) = {}", taskGroupInDB.get(taskGroupId).toString());
                    return taskGroupInDB.get(taskGroupId);
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

    private TaskGroup createTaskGroup() {
        TaskGroup taskGroup = TaskGroup.builder()
                .id((long) taskGroupInDB.size())
                .name("分组" + taskGroupInDB.size())
                .description("分组" + taskGroupInDB.size())
                .taskLists(new ArrayList<>())
                .build();
        taskGroupInDB.add(taskGroup);
        return taskGroup;
    }

    @Test
    public void testCreateNewTaskListNormal() {
        createTaskGroup();

        log.info("taskGroupInDB.size()={}", taskGroupInDB.size());
        log.info(taskGroupInDB.toString());

        TaskListCreateReq taskListCreateReq = new TaskListCreateReq("清单1", "测试", 0L);
        assertDoesNotThrow(() -> taskListService.createNewTaskList(taskListCreateReq));

        verify(taskGroupDAO, times(1)).findById(anyLong());
        verify(taskListDAO, times(1)).findByNameAndGroupId(anyString(), anyLong());
        verify(taskListDAO, times(1)).save(any(TaskList.class));

        assertEquals(1, taskListInDB.size());
    }


}
