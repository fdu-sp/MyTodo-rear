package com.zmark.mytodo.controller;

import com.zmark.mytodo.dto.group.TaskGroupSimpleDTO;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITaskGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/8 14:57
 */
@Slf4j
@RestController
public class TaskGroupController {

    private final ITaskGroupService taskGroupService;

    @Autowired
    public TaskGroupController(ITaskGroupService taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    @GetMapping("/api/task-group/simple/get-all")
    public Result getAllTaskGroup() {
        List<TaskGroupSimpleDTO> taskGroupSimpleDTOList = taskGroupService.findAll();
        return ResultFactory.buildSuccessResult(TaskGroupSimpleDTO.toSimpleResp(taskGroupSimpleDTOList));
    }
}
