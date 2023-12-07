package com.zmark.mytodo.controller;

import com.zmark.mytodo.dto.TaskListDTO;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITaskListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZMark
 * @date 2023/12/7 20:25
 */
@Slf4j
@RestController
public class TaskListController {

    private final ITaskListService taskListService;

    @Autowired
    public TaskListController(com.zmark.mytodo.service.impl.TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @GetMapping("/api/task-list/detail/find-by-id/{id}")
    public Result findById(@PathVariable("id") int id) {
        try {
            TaskListDTO taskListDTO = taskListService.findById(id);
            return ResultFactory.buildSuccessResult(taskListDTO.toDetailResp());
        } catch (NoDataInDataBaseException e) {
            log.warn("根据id查询任务列表失败！" + e.getMessage());
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (Exception e) {
            log.error("根据id查询任务列表失败！" + e.getMessage(), e);
            return ResultFactory.buildFailResult(String.format("不存在id为 %d 的任务列表！", id));
        }
    }
}
