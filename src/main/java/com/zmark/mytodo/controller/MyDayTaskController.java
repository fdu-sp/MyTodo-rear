package com.zmark.mytodo.controller;

import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.IMyDayTaskService;
import com.zmark.mytodo.service.impl.MyDayTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/11 18:03
 */
@Slf4j
@RestController
public class MyDayTaskController {

    private final IMyDayTaskService myDayTaskService;

    @Autowired
    public MyDayTaskController(MyDayTaskService myDayTaskService) {
        this.myDayTaskService = myDayTaskService;
    }

    @PostMapping("/api/task/my-day/add/{task-id}")
    public Result addToMyDayList(@PathVariable("task-id") Long taskId) {
        try {
            myDayTaskService.addToMyDayList(taskId);
            return ResultFactory.buildSuccessResult();
        } catch (RuntimeException e) {
            log.error("addToMyDayList error" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        } catch (NoDataInDataBaseException | NewEntityException e) {
            log.error("addToMyDayList error" + e.getMessage(), e);
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    @PostMapping("/api/task/my-day/remove/{task-id}")
    public Result removeFromMyDayList(@PathVariable("task-id") Long taskId) {
        try {
            myDayTaskService.removeFromMyDayList(taskId);
            return ResultFactory.buildSuccessResult();
        } catch (RuntimeException e) {
            log.error("removeFromMyDayList error" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        } catch (NoDataInDataBaseException e) {
            log.error("removeFromMyDayList error" + e.getMessage(), e);
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    @PostMapping("/api/task/my-day/clear")
    public Result clearMyDayList() {
        try {
            myDayTaskService.clearMyDayList();
            return ResultFactory.buildSuccessResult();
        } catch (RuntimeException e) {
            log.error("clearMyDayList error" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/my-day/simple/list")
    public Result getMyDayListWithSimpleInfo() {
        try {
            List<TaskDTO> taskDTOList = myDayTaskService.getMyDayList();
            return ResultFactory.buildSuccessResult(TaskDTO.toSimpleResp(taskDTOList));
        } catch (RuntimeException e) {
            log.error("getMyDayListWithSimpleInfo error" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/task/my-day/detail/list")
    public Result getMyDayListWithDetailInfo() {
        try {
            List<TaskDTO> taskDTOList = myDayTaskService.getMyDayList();
            return ResultFactory.buildSuccessResult(TaskDTO.toDetailResp(taskDTOList));
        } catch (RuntimeException e) {
            log.error("getMyDayListWithDetailInfo error" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }
}
