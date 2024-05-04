package com.zmark.mytodo.controller;

import com.zmark.mytodo.bo.timer.req.TimerCreateReq;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.dto.timer.TimerDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITimerService;
import com.zmark.mytodo.service.impl.TimerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Violette
 * @date 2024/5/4 15:20
 */
@Slf4j
@Validated
@RestController
public class TimerController {
    private final ITimerService timerService;

    @Autowired
    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostMapping("/api/timer/create-new-timer")
    public Result createNewTimer(@Validated @RequestBody TimerCreateReq timerCreateReq) {
        try {
            TimerDTO timerDTO = timerService.createNewTimer(timerCreateReq);
            return ResultFactory.buildSuccessResult("创建成功", TimerDTO.toSimpleResp(timerDTO));
        } catch (NewEntityException | NoDataInDataBaseException e) {
            log.error("createNewTimer error, timerCreateReq: {}", timerCreateReq, e);
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("createNewTimer error, timerCreateReq: {}", timerCreateReq, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }


}
