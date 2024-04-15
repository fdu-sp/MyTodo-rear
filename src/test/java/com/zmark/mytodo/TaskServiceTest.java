package com.zmark.mytodo;


import com.zmark.mytodo.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.bo.task.req.TaskUpdateReq;
import com.zmark.mytodo.bo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.bo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.bo.task.resp.inner.TaskTimeInfoResp;
import com.zmark.mytodo.dao.*;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.*;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.api.ITagService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.service.impl.TagService;
import com.zmark.mytodo.service.impl.TaskService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;


/**
 * @author zouzouyi
 * @date 2024/4/10
 * @see TaskService 被测对象
 * @see ITaskService 被测接口
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

    @MockBean
    private TaskDAO taskDAO;
    @MockBean
    private TaskTagMatchDAO taskTagMatchDAO;
    @MockBean
    private TagDAO tagDAO;
    @MockBean
    private TaskListDAO taskListDAO;
    @MockBean
    private TaskTimeInfoDAO  taskTimeInfoDAO;
    @MockBean
    private MyDayTaskDAO myDayTaskDAO;
    @MockBean
    private TagService tagService;

    private TaskService taskService;

    //假设数据库中的数据
    private static List<TaskList> taskListsInDB; //已有的清单ids
    private static List<Tag> tagsInDB;
    private static List<TaskTagMatch> taskTagMatchesInDB;
    private static List<MyDayTask> myDayTasksInDB;
    private static List<Task>  tasksInDB;

    private static TaskCreateReq taskCreateReqSuccess ; //模拟的数据 方便插入、修改和删除
    private static TaskCreateReq taskCreateReqFail;

    private static TaskUpdateReq taskUpdateReqSuccess ; //模拟的数据 方便插入、修改和删除
    private static TaskUpdateReq taskUpdateReqFail;


    @BeforeAll
    static void init() {
        taskListsInDB = new ArrayList<>();
        tagsInDB = new ArrayList<>();
        taskTagMatchesInDB = new ArrayList<>();
        myDayTasksInDB = new ArrayList<>();
        tasksInDB = new ArrayList<>();
        //初始化添加1个默认清单
        TaskList taskList = new TaskList();
        taskList.setId(1L);
        taskList.setName("高级web");
        taskListsInDB.add(taskList);

        String title  = "今天搞懂软件实践的测试";
        List<String> tagNames = new ArrayList<>();
        tagNames.add("啊啊啊啊");
        String description = "aaaaa不许拖延";
        Long taskListId = 1L;
        Boolean inMyDay = true;
        // 创建成功的任务创建请求
        taskCreateReqSuccess = TaskCreateReq.builder()
                .title("今天搞懂软件实践的测试")
                .tagNames(List.of("啊啊啊啊"))
                .description("aaaaa不许拖延")
                .taskListId(1L)
                .inMyDay(true)
                .build();

        // 创建失败的任务创建请求
        taskCreateReqFail = TaskCreateReq.builder()
                .title("今天搞懂软件实践的测试")
                .tagNames(List.of("啊啊啊啊"))
                .description("aaaaa不许拖延")
                .taskListId(2L) // 无效的taskListId
                .inMyDay(true)
                .build();

// 创建成功的任务更新请求
        taskUpdateReqSuccess = TaskUpdateReq.builder()
                .title("好耶 快写完测试了")
                .id(0L) // 假设的有效的任务ID
                .taskListId(1L)
                .inMyDay(true)
                .tags(List.of(new TagSimpleResp(0L, "啊啊啊啊", "啊啊啊啊"))) // 假设的有效的标签对象
                .taskContentInfo(new TaskContentInfoResp())
                .taskTimeInfo(new TaskTimeInfoResp())
                .taskPriorityInfo(new TaskPriorityInfoResp())
                .build();


        // 创建失败的任务更新请求
        taskUpdateReqFail = TaskUpdateReq.builder()
                .title("今天搞懂软件实践的测试")
                .id(0L) // 假设的无效的任务ID
                .taskListId(2L) // 无效的taskListId
                .tags(List.of(new TagSimpleResp(0L, "啊啊啊啊", "啊啊啊啊"))) // 假设的有效的标签对象
                .taskContentInfo(new TaskContentInfoResp())
                .taskTimeInfo(new TaskTimeInfoResp())
                .taskPriorityInfo(new TaskPriorityInfoResp())
                .build();
    }

    @BeforeEach
    void setUp() throws NewEntityException {
        //清空数据
        tagsInDB.clear();
        taskTagMatchesInDB.clear();
        myDayTasksInDB.clear();
        tasksInDB.clear();
        //重置
        reset(taskDAO, taskTagMatchDAO, tagDAO, taskListDAO, taskTimeInfoDAO, myDayTaskDAO, tagService);
        //初始化被测对象
        taskService = new TaskService(taskDAO, taskTagMatchDAO, tagDAO, taskListDAO, taskTimeInfoDAO, myDayTaskDAO, tagService);


        //指定mock对象的行为

        /**
         * 模拟taskListDAO
         */
        when(taskListDAO.findTaskListById(anyLong())).thenAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            for (TaskList taskList : taskListsInDB) {
                if (taskList.getId().equals(id)) {
                    return taskList;
                }
            }
            // 如果没有找到，返回一个空的Optional对象
            return null;
        });


        /**
         * 模拟tagDAO
         */


        // 通过遍历tagsInDB来查找具有特定名称的Tag
        when(tagDAO.findByTagName(anyString())).thenAnswer(invocation -> {
            String tagName = invocation.getArgument(0);
            for (Tag tag : tagsInDB) {
                if (tag.getTagName().equals(tagName)) {
                    return tag;
                }
            }
            return null; // 如果没有找到具有该名称的Tag，则返回null
        });

        /**
         * 模拟taskDAO
         */
        doAnswer(invocation -> {
            Task task = invocation.getArgument(0); // 获取 save 方法的第一个参数
            if (task.getId() == null) {
                task.setId((long) (tasksInDB.size())); // 简单的 ID 分配逻辑
            }
            // 现在添加或更新任务到模拟的数据库中
            tasksInDB.removeIf(t -> t.getId().equals(task.getId())); // 移除旧的同ID任务（如果存在）
            tasksInDB.add(task); // 添加更新后的任务
            return null; // 由于是 void 方法，返回 null
        }).when(taskDAO).save(any(Task.class));

        // Mock findTaskById 方法的实现
        when(taskDAO.findTaskById(anyLong())).thenAnswer(invocation -> {
            Long taskId = invocation.getArgument(0); // 获取传入的taskId
            // 在tasksInDB中查找具有指定taskId的Task对象
            for (Task task : tasksInDB) {
                if (task.getId().equals(taskId)) {
                    return task; // 如果找到匹配的Task，返回该对象
                }
            }
            return null; // 如果没有找到匹配的Task，返回null
        });

        // Mock taskDAO.delete 方法的实现
        doAnswer(invocation -> {
            Task task = invocation.getArgument(0); // 获取传入的Task对象
            // 从tasksInDB中删除指定的Task对象
            tasksInDB.remove(task); // 执行删除操作，这里不需要返回值
            return null;
        }).when(taskDAO).delete(any(Task.class));

        /**
         * 模拟tagService
         */
        // 自定义方法，创建新的Tag实体
        when(tagService.createNewTag(anyString())).thenAnswer(invocation -> {

            String tagPath = invocation.getArgument(0);
            String[] tags = tagPath.split("/");
            List<Tag> tagListInDBCopy = new ArrayList<>(tagsInDB); // 创建tagsInDB的副本以避免修改原始数据

            // 从第一个tag开始，如果不存在，则依次创建tag
            // 如果重名tag存在于其他层级中，则抛出异常
            Tag parentTag = null;
            for (String tagName : tags) {
                Tag tagEntity = findTagByTagName(tagListInDBCopy, tagName); // 使用自定义方法查找tag
                if (tagEntity == null) {

                    tagEntity = createNewTagEntity(tagName, parentTag); // 使用自定义方法创建新的tag实体

                } else {
                    if (parentTag != null && !parentTag.getId().equals(tagEntity.getParentTagId())) {
                        throw new NewEntityException("创建tag失败，tag#" + tagEntity.getTagName() + "已存在于其他层级中");
                    }
                }
                parentTag = tagEntity;
            }
            // 返回最后一个tag
            return parentTag;
        });
        /**
         * 模拟taskTagMatchDAO
         */
        // Mock findAllByTagIdAndTaskId 方法的实现
        when(taskTagMatchDAO.findAllByTagIdAndTaskId(anyLong(), anyLong())).thenAnswer(invocation -> {
            Long tagId = (Long) invocation.getArgument(0);
            Long taskId = (Long) invocation.getArgument(1);
            List<TaskTagMatch> matchingTaskTagMatches = new ArrayList<>(); // 创建一个新的列表来存储匹配的TaskTagMatch对象

            // 在taskTagMatchesInDB中查找匹配的TaskTagMatch对象
            for (TaskTagMatch match : taskTagMatchesInDB) {
                if (match.getTagId().equals(tagId) && match.getTaskId().equals(taskId)) {
                    matchingTaskTagMatches.add(match); // 如果匹配，将TaskTagMatch对象添加到新的列表中
                }
            }
//            System.out.println(matchingTaskTagMatches);
            return matchingTaskTagMatches; // 返回匹配的TaskTagMatch对象列表
        });

        // Mock save 方法的实现
        when(taskTagMatchDAO.save(any(TaskTagMatch.class))).thenAnswer(invocation -> {
            TaskTagMatch match = invocation.getArgument(0); // 获取传入的TaskTagMatch对象
            // 根据taskTagMatchesInDB列表的当前大小设置id
            match.setId((long)(taskTagMatchesInDB.size())); // 假设id从1开始
            // 将传入的TaskTagMatch对象添加到taskTagMatchesInDB列表中
            taskTagMatchesInDB.add(match);
//            System.out.println(match);
            // 通常，save方法会返回被保存的对象，这里我们返回传入的TaskTagMatch对象
            return match;
        });

        // Mock taskTagMatchDAO.deleteAllByTaskId 方法的实现
        doAnswer(invocation -> {
            Long taskId = invocation.getArgument(0); // 获取传入的taskId
            // 从taskTagMatchesInDB中删除所有具有指定taskId的TaskTagMatch对象
            taskTagMatchesInDB.removeIf(match -> match.getTaskId().equals(taskId)); // 执行删除操作，这里不需要返回值
            return null;
        }).when(taskTagMatchDAO).deleteAllByTaskId(anyLong());

        /**
         * 模拟 myDayTaskDAO
         */
        // Mock findMyDayTaskByTaskId 方法的实现
        when(myDayTaskDAO.findMyDayTaskByTaskId(anyLong())).thenAnswer(invocation -> {
            Long taskId = invocation.getArgument(0); // 获取传入的taskId
            // 在myDayTasksInDB中查找具有指定taskId的MyDayTask对象
            for (MyDayTask myDayTask : myDayTasksInDB) {
                if (myDayTask.getTaskId().equals(taskId)) {
                    return myDayTask; // 如果找到匹配的MyDayTask，返回该对象
                }
            }
            return null; // 如果没有找到匹配的MyDayTask，返回null
        });

// Mock save 方法的实现
        when(myDayTaskDAO.save(any(MyDayTask.class))).thenAnswer(invocation -> {
            MyDayTask myDayTask = invocation.getArgument(0); // 获取传入的MyDayTask对象
            // 根据myDayTasksInDB列表的当前大小设置id
            if (myDayTask.getId() == null) {
                myDayTask.setId((long)myDayTasksInDB.size()); // 假设id从1开始
            }
            // 将MyDayTask对象添加到myDayTasksInDB列表中
            myDayTasksInDB.add(myDayTask);
            // 通常，save方法会返回被保存的对象，这里我们返回传入的MyDayTask对象
            return myDayTask;
        });


        // Mock myDayTaskDAO.delete 方法的实现
        doAnswer(invocation -> {
            MyDayTask myDayTask = invocation.getArgument(0); // 获取传入的MyDayTask对象
            // 从myDayTasksInDB中删除指定的MyDayTask对象
            myDayTasksInDB.remove(myDayTask); // 执行删除操作，这里不需要返回值，因为delete方法返回void
            return null;
        }).when(myDayTaskDAO).delete(any(MyDayTask.class));

    }
    private Tag createNewTagEntity(String tagName, Tag parentTag) {
        long newId = tagsInDB.size() ; // 假设id从0开始
        Tag newTag = Tag.builder()
                .tagName(tagName)
                .parentTag(parentTag)
                .parentTagId(parentTag == null ? null : parentTag.getId())
                .id(newId) // 设置新Tag的id
                .build();
        tagsInDB.add(newTag); // 将新创建的Tag添加到tagsInDB列表中
        return newTag;
    }
    // 自定义方法，根据tagName在tagsInDB副本中查找Tag
    private Tag findTagByTagName(List<Tag> tagList, String tagName) {
        for (Tag tag : tagList) {
            if (tag.getTagName().equals(tagName)) {
                return tag;
            }
        }
        return null;
    }

    @Test
    public void test_createTaskSuccess(){
        //1.Given 用户需要创建待办事项
        //beforeAll已经准备好数据


        try {
            //2. When 调用taskService的createNewTask
            TaskDTO taskDTO = taskService.createNewTask(taskCreateReqSuccess );

            //3. Then task创建成功
            // 验证 tagsInDB 的大小为 1
            Assertions.assertEquals(1, tagsInDB.size());
            // 验证 taskTagMatchesInDB 的大小为 1
            Assertions.assertEquals(1, taskTagMatchesInDB.size());
            // 验证 myDayTasksInDB 的大小为 1
            Assertions.assertEquals(1, myDayTasksInDB.size());
            // 验证 tasksInDB 的大小也为 1
            Assertions.assertEquals(1, tasksInDB.size());

            // 验证 taskTagMatchesInDB 里面的 taskId 和 tagId 能和 tagsInDB、tasksInDB 对应上
            for (TaskTagMatch match : taskTagMatchesInDB) {
                Long matchedTaskId = match.getTaskId();
                Long matchedTagId = match.getTagId();

                // 检查 taskId 是否存在于 tasksInDB 中
                boolean taskIdExists = false;
                for (Task task : tasksInDB) {
                    if (task.getId().equals(matchedTaskId)) {
                        taskIdExists = true;
                        break;
                    }
                }
                assertTrue("The task with id " + matchedTaskId + " was not found in tasksInDB.", taskIdExists);

                // 检查 tagId 是否存在于 tagsInDB 中
                boolean tagIdExists = false;
                for (Tag tag : tagsInDB) {
                    if (tag.getId().equals(matchedTagId)) {
                        tagIdExists = true;
                        break;
                    }
                }
                assertTrue("The tag with id " + matchedTagId + " was not found in tagsInDB.", tagIdExists);
            }
        } catch (NewEntityException | NoDataInDataBaseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test_createTaskFailed() {
        // 尝试调用 createNewTask 方法
        try {
            taskService.createNewTask(taskCreateReqFail);
            fail("Expected NewEntityException to be thrown");
        }catch (NewEntityException | NoDataInDataBaseException e) {
            // 验证抛出的异常类型
            Assertions.assertEquals( "找不到id为" + taskCreateReqFail.getTaskListId() + "的任务清单", e.getMessage());
        }
    }

    @Test
    public void test_updateTaskSuccess(){
        try {
            //1. Given用户要修改待办事项
            //准备好数据，创建待办事项
            taskService.createNewTask(taskCreateReqSuccess);

            //2.When 调用函数进行修改
            taskService.updateTask(taskUpdateReqSuccess);

            //3. 断言
            // 断言tasksInDB中对应id的任务的title已经被更新
            Task updatedTask = tasksInDB.stream()
                    .filter(task -> task.getId().equals(taskUpdateReqSuccess.getId()))
                    .findFirst()
                    .orElse(null); // 获取更新后的任务对象，如果不存在则返回null

            assertNotNull("The task should exist after update.", updatedTask);
            assertEquals("The title of the task should be updated to '好耶 快写完测试了'.", "好耶 快写完测试了", updatedTask.getTitle());

        }catch(NewEntityException | NoDataInDataBaseException e){
            e.printStackTrace();
        }
    }
    @Test
    public void test_updateTaskFailed(){
        try {
            //1. Given用户要修改待办事项
            //准备好数据，创建待办事项
            taskService.createNewTask(taskCreateReqSuccess);

            //2.When 调用函数进行修改
            taskService.updateTask(taskUpdateReqFail);


        }catch (NewEntityException | NoDataInDataBaseException e) {
            // 验证抛出的异常类型
            //3. 断言
            Assertions.assertEquals( "找不到id为" + taskUpdateReqFail.getTaskListId() + "的任务清单", e.getMessage());
        }
    }


    @Test
    public void test_deleteTaskSuccess(){
        //先创建好任务
        try {
            //1. Given 作为用户，我希望能删除待办事项，以便管理我的工作和生活

            //2. When 调用删除函数
            taskService.deleteTaskById(2L);

        }catch (NoDataInDataBaseException e) {
            // 验证抛出的异常类型
            //3. Then 断言检查
            Assertions.assertEquals( "找不到id为2的任务", e.getMessage());
        }



    }

    @Test
    public void test_deleteTaskFailed(){
        //先创建好任务
        try {
            //1. Given 作为用户，我希望能删除待办事项，以便管理我的工作和生活
            //已有创建好的待办事项
            TaskDTO taskDTO = taskService.createNewTask(taskCreateReqSuccess );

            //2. 调用删除函数
            taskService.deleteTaskById(taskDTO.getId());




        }catch (NewEntityException | NoDataInDataBaseException e) {
            e.printStackTrace();
        }

    }


}
