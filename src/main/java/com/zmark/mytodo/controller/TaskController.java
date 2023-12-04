package com.zmark.mytodo.controller;

import com.zmark.mytodo.dto.task.req.TaskCreatReq;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/task/simple-info/get-all-tasks")
    public Result getAllTasksWithSimpleInfo() {
        // todo
        return ResultFactory.buildSuccessResult("todo...", null);
    }

    @GetMapping("/api/task/detail-info/get-all-tasks")
    public Result getAllTasksWithDetailInfo() {
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
    public Result createNewTask(@RequestBody TaskCreatReq taskCreatReq) {
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
        // // TODO: 2023/12/3 或许需要拆的更细致一些
        return ResultFactory.buildSuccessResult("todo...", null);
    }

}
