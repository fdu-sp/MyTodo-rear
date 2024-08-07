package com.zmark.mytodo.controller;

import com.zmark.mytodo.bo.task.req.TaskExtractFromTextReq;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITaskExtractService;
import com.zmark.mytodo.service.impl.TaskExtractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZMark
 * @date 2024/7/23 下午1:34
 */
@Slf4j
@Validated
@RestController
public class TaskExtractController {
    private ITaskExtractService taskExtractService;

    @Autowired
    public void setTaskExtractService(TaskExtractService taskExtractService) {
        this.taskExtractService = taskExtractService;
    }

    /**
     * AI语义分析任务
     */
    @PostMapping("/api/task/extract-from-text")
    public Result extractTaskFromText(@Validated @RequestBody TaskExtractFromTextReq req) {
        try {
            return ResultFactory.buildSuccessResult(taskExtractService.extractFromText(req));
        } catch (Exception e) {
            log.error("extractTaskFromText error, req: {}", req, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    /**
     * AI语义分析任务并自动添加任务
     */
    @PostMapping("/api/task/extract-and-add-from-text")
    public Result extractAndAddTaskFromText(@Validated @RequestBody TaskExtractFromTextReq req) {
        try {
            TaskDTO taskDTO = taskExtractService.extractAndAddFromText(req);
            return ResultFactory.buildSuccessResult("任务提取并创建成功", TaskDTO.toDetailResp(taskDTO));
        } catch (Exception e) {
            log.error("extractAndAddTaskFromText error, req: {}", req, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }
}
