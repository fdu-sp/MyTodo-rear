package com.zmark.mytodo.controller;

import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.service.impl.TaskService;
import com.zmark.mytodo.vo.task.req.TaskCreatReq;
import com.zmark.mytodo.vo.task.resp.TaskDetailResp;
import com.zmark.mytodo.vo.task.resp.TaskSimpleResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZMark
 * @date 2023/12/3 23:40
 */
@Slf4j
@Validated
@RestController
public class TaskController {

    private final ITaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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

    @GetMapping("/api/task/simple/get-all-task/{tag-name}")
    public Result getAllTasksWithSimpleInfoByTag(@PathVariable("tag-name") String tagName) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @GetMapping("/api/task/simple/get-all-tasks/{tag-name}/{status}")
    public Result getAllTasksByTagAndStatus(@PathVariable("tag-name") String tagName, @PathVariable("status") String status) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @PostMapping("/api/task/create-new-task")
    public Result createNewTask(@Validated @RequestBody TaskCreatReq taskCreatReq) {
        try {
            TaskDTO taskDTO = taskService.createNewTask(taskCreatReq);
            return ResultFactory.buildSuccessResult("创建成功", TaskDTO.toDetailResp(taskDTO));
        } catch (NewEntityException e) {
            log.error("createNewTask error, taskCreatReq: {}", taskCreatReq, e);
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("createNewTask error, taskCreatReq: {}", taskCreatReq, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @PostMapping("/api/task/delete/{task-id}")
    public Result deleteTaskById(@PathVariable("task-id") int taskId) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @PostMapping("/api/task/update")
    public Result updateTask() {
        // // TODO: 2023/12/3 或许需要拆的更细致一些
        return ResultFactory.buildSuccessResult("todo...", null);
    }

}
