package com.zmark.mytodo.controller;

import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZMark
 * @date 2023/12/3 23:40
 */
@RestController
public class TaskController {

    @GetMapping("/api/task/get-simple-info/{task-id}")
    public Result getSimpleInfoById(@PathVariable("task-id") int taskId) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @GetMapping("/api/task/get-detail-info/{task-id}")
    public Result getDetailInfoById(@PathVariable("task-id") int taskId) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @GetMapping("/api/task/get-all-tasks")
    public Result getAllTasks() {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
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
    public Result createNewTask() {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @PostMapping("/api/task/delete-task/{task-id}")
    public Result deleteTaskById(@PathVariable("task-id") int taskId) {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @PostMapping("/api/task/update-task")
    public Result updateTask() {
        // // TODO: 2023/12/3
        return ResultFactory.buildSuccessResult("todo...", null);
    }

}
