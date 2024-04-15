package com.zmark.mytodo;

import com.zmark.mytodo.bo.list.req.TaskListCreateReq;
import com.zmark.mytodo.bo.list.req.TaskListUpdateReq;
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
    public final static long DEFAULT_GROUP_ID = 1L;

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
        when(taskGroupDAO.findTaskGroupById(anyLong()))  // 这里的 anyLong是基元类型
                .thenAnswer(invocationOnMock -> {
                    // 模拟返回数据
                    int taskGroupId = ((Long) invocationOnMock.getArgument(0)).intValue();
                    if (taskGroupId > taskGroupInDB.size()) {
                        throw new NoDataInDataBaseException("TaskGroup", taskGroupId);
                    }
                    return taskGroupInDB.get(taskGroupId - 1);  // taskGroupId从1开始
                });
        when(taskListDAO.findTaskListById(anyLong()))
                .thenAnswer(invocationOnMock -> {
                    int taskListId = ((Long) invocationOnMock.getArgument(0)).intValue();
                    if (taskListId > taskListInDB.size()) {
                        throw new NoDataInDataBaseException("TaskList", taskListId);
                    }
                    return taskListInDB.get(taskListId - 1);  // taskListId也从1开始
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
                    if (taskList.getId() != null) {  // update
                        taskListInDB.set((int) ((long) taskList.getId() - 1), taskList);
                        log.info("task list group{}#{} updated", taskList.getGroupId(), taskList.getName());
                    } else {  // insert
                        taskList.setId((long) taskListInDB.size() + 1); // FIX
                        taskListInDB.add(taskList);
                        log.info("task list group{}#{} saved", taskList.getGroupId(), taskList.getName());
                    }
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
        log.info("task group {} saved", taskGroupInDB.size());
    }


    /**
     * 创建清单
     *
     * @see TaskListService#updateTaskList(TaskListUpdateReq)  被测方法
     */

    @Test
    public void testCreateNewTaskListNormal() {
        // 添加分组
        createTaskGroup();  // 默认分组，id为1
        createTaskGroup();

        // 添加清单，不指定分组，service函数会自动选择默认分组
        TaskListCreateReq taskListCreateReqWithDefaultGroup = new TaskListCreateReq("清单1", "测试", null);
        assertDoesNotThrow(() -> taskListService.createNewTaskList(taskListCreateReqWithDefaultGroup));

        verify(taskGroupDAO, times(1)).findTaskGroupById(anyLong());
        verify(taskListDAO, times(1)).findByNameAndGroupId(anyString(), anyLong());
        verify(taskListDAO, times(1)).save(any(TaskList.class));

        assertEquals(1, taskListInDB.size());

        // 添加清单，指定分组为第二个
        TaskListCreateReq taskListCreateReqWithAnotherGroup = new TaskListCreateReq("清单2", "测试", 2L);
        assertDoesNotThrow(() -> taskListService.createNewTaskList(taskListCreateReqWithAnotherGroup));

        verify(taskGroupDAO, times(2)).findTaskGroupById(anyLong());
        verify(taskListDAO, times(2)).findByNameAndGroupId(anyString(), anyLong());
        verify(taskListDAO, times(2)).save(any(TaskList.class));

        assertEquals(2, taskListInDB.size());
    }

    @Test
    public void testCreateNewTaskListException() {
        TaskListCreateReq taskListCreateReq;

        // 创建失败：当前不存在默认分组
        try {
            log.info("尝试创建分组1#清单1");
            taskListCreateReq = new TaskListCreateReq("清单1", "测试", null);
            taskListService.createNewTaskList(taskListCreateReq);
            log.info("分组1#清单1创建成功");
        } catch (Exception e) {
            log.warn("分组1#清单1创建失败", e);
        }

        // 添加分组
        createTaskGroup();  // 默认分组，id为1
        createTaskGroup();

        try {
            log.info("尝试创建分组1#清单1");
            taskListCreateReq = new TaskListCreateReq("清单1", "测试", 1L);
            taskListService.createNewTaskList(taskListCreateReq);
            log.info("分组1#清单1创建成功");
        } catch (Exception e) {
            log.error("分组1#清单1创建失败", e);
        }

        try {
            log.info("尝试创建分组2#清单1");
            taskListCreateReq = new TaskListCreateReq("清单1", "测试", 2L);
            taskListService.createNewTaskList(taskListCreateReq);
            log.info("分组2#清单1创建成功");
        } catch (Exception e) {
            log.error("分组2#清单1创建失败", e);
        }

        // 创建失败：当前只有2个分组
        try {
            log.info("尝试创建分组3#清单1");
            taskListCreateReq = new TaskListCreateReq("清单1", "测试", 3L);
            taskListService.createNewTaskList(taskListCreateReq);
            log.info("分组3#清单1创建成功");
        } catch (Exception e) {
            log.warn("创建清单失败：", e);
        }

        // 创建失败：同一分组下不能有同名清单
        try {
            log.info("尝试创建分组2#清单1");
            taskListCreateReq = new TaskListCreateReq("清单1", "测试", 1L);
            taskListService.createNewTaskList(taskListCreateReq);
            log.info("分组2#清单1创建成功");
        } catch (Exception e) {
            log.warn("分组2#清单1创建失败", e);
        }
    }


    private void addTaskList(long taskGroupId) {
        if (taskGroupId > taskGroupInDB.size()) {
            throw new IllegalArgumentException("分组ID不存在");
        }
        long taskListId = taskListInDB.size() + 1;  // taskListId也从1开始（不同分组的清单共享id变量）
        TaskList taskList = TaskList.builder()
                .id(taskListId)
                .name("清单" + taskListId)
                .groupId(taskGroupId)
                .description("清单的自我修养")
                .build();
        taskListInDB.add(taskList);
        log.info("task list group{}#list{} saved", taskGroupId, taskListId);
    }


    /**
     * 修改清单信息
     *
     * @see TaskListService#updateTaskList(TaskListUpdateReq)
     */
    @Test
    public void testUpdateTaskListNormal() {
        // 创建分组
        createTaskGroup();
        createTaskGroup();
        assertEquals(2, taskGroupInDB.size());
        // 新建一个在默认分组中的清单
        addTaskList(1L);  // name: 清单1  description: 清单的自我修养  groupId: 1L
        assertEquals(1, taskListInDB.size());

        // 更新清单名字、描述、所属分组
        String newName = "我是新的清单";
        String newDescription = "抵达next level";
        Long newGroupId = 2L;
        TaskListUpdateReq updateReq = new TaskListUpdateReq(1L, newName, newDescription, newGroupId);
        assertDoesNotThrow(() -> taskListService.updateTaskList(updateReq));

        verify(taskListDAO, times(1)).findTaskListById(anyLong());
        verify(taskGroupDAO, times(1)).findTaskGroupById(anyLong());
        verify(taskListDAO, times(1)).findByNameAndGroupId(anyString(), anyLong());
        verify(taskListDAO, times(1)).save(any(TaskList.class));

        assertEquals(1, taskListInDB.size());

        // 验证信息是否被成功更新
        TaskList taskListAfterUpdate = taskListInDB.get(0);
        assertEquals(newName, taskListAfterUpdate.getName());
        assertEquals(newDescription, taskListAfterUpdate.getDescription());
        assertEquals(newGroupId, taskListAfterUpdate.getGroupId());
    }

    @Test
    public void testUpdateTaskListException() {
        TaskListUpdateReq updateReq;
        String newName = "我是新的清单";
        String newDescription = "抵达next level";

        // 更新失败：清单不存在
        try {
            updateReq = new TaskListUpdateReq(1L, newName, newDescription, DEFAULT_GROUP_ID);
            taskListService.updateTaskList(updateReq);
            log.info("清单更新成功");
        } catch (Exception e) {
            log.warn("清单更新失败", e);
        }

        // 创建默认分组
        createTaskGroup();
        assertEquals(1, taskGroupInDB.size());
        // 新建两个默认分组中的清单
        addTaskList(DEFAULT_GROUP_ID);  // name: 清单1  description: 清单的自我修养  groupId: 1L
        addTaskList(DEFAULT_GROUP_ID);  // name: 清单2  description: 清单的自我修养  groupId: 1L
        assertEquals(2, taskListInDB.size());

        // 更新失败：同一分组下的清单名字不能相同
        try {
            updateReq = new TaskListUpdateReq(1L, "清单2", newDescription, DEFAULT_GROUP_ID);
            taskListService.updateTaskList(updateReq);
            log.info("清单更新成功");
        } catch (Exception e) {
            log.warn("清单更新失败", e);
        }

        // 更新失败：不存在分组2
        try {
            updateReq = new TaskListUpdateReq(1L, newName, newDescription, 2L);
            taskListService.updateTaskList(updateReq);
            log.info("清单更新成功");
        } catch (Exception e) {
            log.warn("清单更新失败", e);
        }

    }

}
