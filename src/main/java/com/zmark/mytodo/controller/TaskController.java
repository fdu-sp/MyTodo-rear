package com.zmark.mytodo.controller;

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
import org.springframework.web.bind.annotation.*;

/**
 * @author ZMark
 * @date 2023/12/3 23:40
 */
@Slf4j
@RestController
public class TaskController {

    private final ITaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/api/task/get-simple-info/{task-id}")
    public Result getSimpleInfoById(@PathVariable("task-id") Long taskId) {
        try {
            TaskSimpleResp respData = TaskSimpleResp.from(taskService.findTaskById(taskId));
            return ResultFactory.buildSuccessResult(respData);
        } catch (Exception e) {
            log.error("getSimpleInfoById error, taskId: {}", taskId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/get-detail-info/{task-id}")
    public Result getDetailInfoById(@PathVariable("task-id") Long taskId) {
        try {
            TaskDetailResp respData = TaskDetailResp.from(taskService.findTaskById(taskId));
            return ResultFactory.buildSuccessResult(respData);
        } catch (Exception e) {
            log.error("getDetailInfoById error, taskId: {}", taskId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/simple-info/get-all-tasks")
    public Result getAllTasksWithSimpleInfo() {
        try {
            return ResultFactory.buildSuccessResult(TaskSimpleResp.from(taskService.findAllTasks()));
        } catch (Exception e) {
            log.error("getAllTasksWithSimpleInfo error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/detail-info/get-all-tasks")
    public Result getAllTasksWithDetailInfo() {
        try {
            return ResultFactory.buildSuccessResult(TaskDetailResp.from(taskService.findAllTasks()));
        } catch (Exception e) {
            log.error("getAllTasksWithDetailInfo error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/simple-info/get-all-task/{tag-name}")
    public Result getAllTasksWithSimpleInfoByTag(@PathVariable("tag-name") String tagName) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @GetMapping("/api/task/simple-info/get-all-tasks/{tag-name}/{status}")
    public Result getAllTasksByTagAndStatus(@PathVariable("tag-name") String tagName, @PathVariable("status") String status) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @PostMapping("/api/task/create-new-task")
    public Result createNewTask(@RequestBody TaskCreatReq taskCreatReq) {
        try {
            taskService.createNewTask(taskCreatReq);
            return ResultFactory.buildSuccessResult("创建成功", null);
        } catch (NewEntityException e) {
            log.error("createNewTask error, taskCreatReq: {}", taskCreatReq, e);
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("createNewTask error, taskCreatReq: {}", taskCreatReq, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @PostMapping("/api/task/delete-task/{task-id}")
    public Result deleteTaskById(@PathVariable("task-id") int taskId) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @PostMapping("/api/task/update-task")
    public Result updateTask() {
        // // TODO: 2023/12/3 或许需要拆的更细致一些
        return ResultFactory.buildSuccessResult("todo...", null);
    }

}
