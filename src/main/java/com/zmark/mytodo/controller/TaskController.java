package com.zmark.mytodo.controller;

import com.zmark.mytodo.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.bo.task.req.TaskQueryByTagsReq;
import com.zmark.mytodo.bo.task.req.TaskUpdateReq;
import com.zmark.mytodo.bo.task.resp.TaskDetailResp;
import com.zmark.mytodo.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.dto.list.TaskListDTO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITaskListService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.service.impl.TaskListService;
import com.zmark.mytodo.service.impl.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/3 23:40
 */
@Slf4j
@Validated
@RestController
public class TaskController {

    private final ITaskService taskService;

    private final ITaskListService taskListService;

    @Autowired
    public TaskController(TaskService taskService,
                          TaskListService taskListService) {
        this.taskService = taskService;
        this.taskListService = taskListService;
    }

    @GetMapping("/api/task/simple/get-info/{task-id}")
    public Result getSimpleInfoById(@PathVariable("task-id") Long taskId) {
        try {
            TaskDTO taskDTO = taskService.findTaskById(taskId);
            if (taskDTO == null) {
                return ResultFactory.buildFailResult("不存在id为" + taskId + "的任务");
            }
            TaskSimpleResp respData = TaskDTO.toSimpleResp(taskDTO);
            return ResultFactory.buildSuccessResult(respData);
        } catch (Exception e) {
            log.error("getSimpleInfoById error, taskId: {}", taskId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/detail/get-info/{task-id}")
    public Result getDetailInfoById(@PathVariable("task-id") Long taskId) {
        try {
            TaskDTO taskDTO = taskService.findTaskById(taskId);
            if (taskDTO == null) {
                return ResultFactory.buildFailResult("不存在id为" + taskId + "的任务");
            }
            TaskDetailResp respData = TaskDTO.toDetailResp(taskDTO);
            return ResultFactory.buildSuccessResult(respData);
        } catch (Exception e) {
            log.error("getDetailInfoById error, taskId: {}", taskId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/simple/get-all-tasks")
    public Result getAllTasksWithSimpleInfo() {
        try {
            log.info("getAllTasksWithSimpleInfo api called");
            return ResultFactory.buildSuccessResult(TaskDTO.toSimpleResp(taskService.findAllTasks()));
        } catch (Exception e) {
            log.error("getAllTasksWithSimpleInfo error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/detail/get-all-tasks")
    public Result getAllTasksWithDetailInfo() {
        try {
            return ResultFactory.buildSuccessResult(TaskDTO.toDetailResp(taskService.findAllTasks()));
        } catch (Exception e) {
            log.error("getAllTasksWithDetailInfo error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/simple/get-all-tasks/{tag-id}")
    public Result getAllTasksWithSimpleInfoByTag(@PathVariable("tag-id") Long tagId) {
        List<TaskDTO> taskDTOList = taskService.findAllByTag(tagId);
        return ResultFactory.buildSuccessResult(TaskDTO.toSimpleResp(taskDTOList));
    }

    /**
     * 根据标签过滤（同时具有多个标签的）待办事项
     *
     * @param tagIdList 标签id列表（必填）
     */
    @PostMapping("/api/task/simple/get-all-tasks-by-tags")
    public Result getAllTasksWithSimpleInfoByTags(@Validated @RequestBody TaskQueryByTagsReq tagIdList) {
        List<TaskDTO> taskDTOList = taskService.findAllByTags(tagIdList);
        return ResultFactory.buildSuccessResult(TaskDTO.toSimpleResp(taskDTOList));
    }

    @GetMapping("/api/task/simple/get-all-tasks-by-list/{list-id}")
    public Result getAllTasksWithSimpleInfoByList(@PathVariable("list-id") Long listId) {
        try {
            TaskListDTO taskListDTO = taskListService.findById(listId);
            List<TaskDTO> taskDTOList = taskListDTO.getTaskDTOList();
            return ResultFactory.buildSuccessResult(TaskDTO.toSimpleResp(taskDTOList));
        } catch (NoDataInDataBaseException e) {
            log.warn("getAllTasksWithSimpleInfoByList error, listId: {}", listId, e);
            return ResultFactory.buildNotFoundResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("getAllTasksWithSimpleInfoByList error, listId: {}", listId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    /**
     * 获取任务的统计数据
     *
     * @param period 'all': 所有， 'today':今天
     */
    @GetMapping("/api/task/analysis/get-task-analysis-by-period/{period}")
    public Result getTaskAnalysisByPeriod(@PathVariable("period") String period) {
        try {
            List<TaskDTO> taskDTOList = taskService.getTaskByPeriod(period);
            return ResultFactory.buildSuccessResult(TaskDTO.toAnalysisResp(taskDTOList));
        } catch (RuntimeException e) {
            log.error("getTaskAnalysisByPeriod error, period: {}", period, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @PostMapping("/api/task/create-new-task")
    public Result createNewTask(@Validated @RequestBody TaskCreateReq taskCreateReq) {
        try {
            TaskDTO taskDTO = taskService.createNewTask(taskCreateReq);
            return ResultFactory.buildSuccessResult("创建成功", TaskDTO.toDetailResp(taskDTO));
        } catch (NewEntityException | NoDataInDataBaseException e) {
            log.error("createNewTask error, taskCreatReq: {}", taskCreateReq, e);
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("createNewTask error, taskCreatReq: {}", taskCreateReq, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @PostMapping("/api/task/delete/{task-id}")
    public Result deleteTaskById(@PathVariable("task-id") Long taskId) {
        try {
            taskService.deleteTaskById(taskId);
            return ResultFactory.buildSuccessResult("删除成功", null);
        } catch (NoDataInDataBaseException e) {
            log.warn("deleteTaskById error, taskId: {}", taskId, e);
            return ResultFactory.buildNotFoundResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("deleteTaskById error, taskId: {}", taskId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @PostMapping("/api/task/update")
    public Result updateTask(@RequestBody @Validated TaskUpdateReq taskUpdateReq) {
        try {
            TaskDTO taskDTO = taskService.updateTask(taskUpdateReq);
            return ResultFactory.buildSuccessResult("更新成功", TaskDTO.toDetailResp(taskDTO));
        } catch (RuntimeException e) {
            log.error("updateTask error, taskUpdateReq: {}", taskUpdateReq, e);
            return ResultFactory.buildInternalServerErrorResult();
        } catch (NoDataInDataBaseException e) {
            log.warn("updateTask error, taskUpdateReq: {}", taskUpdateReq, e);
            return ResultFactory.buildNotFoundResult(e.getMessage());
        } catch (NewEntityException e) {
            log.error("updateTask error, taskUpdateReq: {}", taskUpdateReq, e);
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    @PostMapping("/api/task/complete/{task-id}")
    public Result completeTask(@PathVariable("task-id") Long taskId) {
        try {
            taskService.completeTask(taskId);
            return ResultFactory.buildSuccessResult("任务已完成", null);
        } catch (NoDataInDataBaseException e) {
            log.warn("请求有误：前端请求了" + e.getMessage());
            return ResultFactory.buildNotFoundResult(e.getMessage());
        } catch (Exception e) {
            log.error("completeTask error, taskId: {}", taskId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @PostMapping("/api/task/un-complete/{task-id}")
    public Result unCompleteTask(@PathVariable("task-id") Long taskId) {
        try {
            taskService.unCompleteTask(taskId);
            return ResultFactory.buildSuccessResult("任务未完成", null);
        } catch (NoDataInDataBaseException e) {
            log.warn("请求有误：前端请求了" + e.getMessage());
            return ResultFactory.buildNotFoundResult(e.getMessage());
        } catch (Exception e) {
            log.error("unCompleteTask error, taskId: {}", taskId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }
}
