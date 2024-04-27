package com.zmark.mytodo;

import com.zmark.mytodo.bo.group.req.TaskGroupCreateReq;
import com.zmark.mytodo.dao.TaskGroupDAO;
import com.zmark.mytodo.entity.TaskGroup;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import com.zmark.mytodo.service.impl.TaskGroupService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Violette
 * @date 2024/4/15 17:24
 * @see TaskGroupService 被测对象
 */

@Slf4j
@ExtendWith(SpringExtension.class)
public class TaskGroupServiceTest {

    /**
     * 模拟数据库中的数据
     */
    private static Map<String, TaskGroup> taskGroupInDB;

    @MockBean
    private TaskGroupDAO taskGroupDAO;

    /**
     * 被测对象
     */
    private TaskGroupService taskGroupService;

    @BeforeAll
    static void init() {
        taskGroupInDB = new HashMap<>();
    }

    @BeforeEach
    void setUp() {
        // 清空模拟数据
        taskGroupInDB.clear();
        // 充值模拟对象
        reset(taskGroupDAO);
        // 初始化被测对象
        taskGroupService = new TaskGroupService(taskGroupDAO);
        // 设置模拟对象行为
        when(taskGroupDAO.findByName(anyString()))
                .thenAnswer(invocationOnMock -> {
                    String name = invocationOnMock.getArgument(0);
                    return taskGroupInDB.get(name);  // 不存在则返回null
                });
        when(taskGroupDAO.save(any(TaskGroup.class)))  // 目前只会有insert，不会有update
                .thenAnswer(invocationOnMock -> {
                    TaskGroup taskGroup = invocationOnMock.getArgument(0);
                    taskGroup.setId((long) taskGroupInDB.size());
                    taskGroupInDB.put(taskGroup.getName(), taskGroup);
                    log.info("task group {} saved", taskGroup.getName());
                    return taskGroup;
                });
    }

    /**
     * 创建新分组
     *
     * @see TaskGroupService#createNew(TaskGroupCreateReq)
     */

    @Test
    public void testCreateNewTaskGroupNormal() {
        TaskGroupCreateReq createReq = new TaskGroupCreateReq("分组1", "我是新分组");
        assertDoesNotThrow(() -> taskGroupService.createNew(createReq));

        verify(taskGroupDAO, times(1)).findByName(anyString());
        verify(taskGroupDAO, times(1)).save(any(TaskGroup.class));

        assertEquals(1, taskGroupInDB.size());
    }

    @Test
    public void testCreateNewTaskGroupException() throws RepeatedEntityInDatabase {
        TaskGroupCreateReq createReq;

        createReq = new TaskGroupCreateReq("分组1", "我是新分组");
        taskGroupService.createNew(createReq);
        log.info("成功创建分组：{}", createReq.getName());


        assertEquals(1, taskGroupInDB.size());

        // 创建失败：不能存在同名分组
        try {
            createReq = new TaskGroupCreateReq("分组1", "我是同名分组");
            taskGroupService.createNew(createReq);
            log.info("成功创建分组：{}", createReq.getName());
            fail();
        } catch (Exception e) {
            assertInstanceOf(RepeatedEntityInDatabase.class, e);
            log.warn("创建分组失败：", e);
        }
    }

}
