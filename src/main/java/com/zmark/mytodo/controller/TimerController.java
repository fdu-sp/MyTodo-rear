package com.zmark.mytodo.controller;

import com.zmark.mytodo.bo.timer.req.TimerCreateReq;
import com.zmark.mytodo.bo.timer.req.TimerUpdateReq;
import com.zmark.mytodo.bo.timer.resp.TimerMonthAnalysisResp;
import com.zmark.mytodo.bo.timer.resp.TimerSimpleResp;
import com.zmark.mytodo.bo.timer.resp.TimerWeekAnalysisResp;
import com.zmark.mytodo.dto.timer.TimerDTO;
import com.zmark.mytodo.dto.timer.TimerDayDTO;
import com.zmark.mytodo.dto.timer.TimerMonthDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.UpdateEntityException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITimerService;
import com.zmark.mytodo.service.impl.TimerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            log.warn("createNewTimer error, timerCreateReq: {}", timerCreateReq, e);
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("createNewTimer error, timerCreateReq: {}", timerCreateReq, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @PostMapping("/api/timer/update-timer")
    public Result updateTimer(@Validated @RequestBody TimerUpdateReq timerUpdateReq) {
        try {
            timerService.updateTimer(timerUpdateReq);
            return ResultFactory.buildSuccessResult("计时器已结束", null);
        } catch (UpdateEntityException | NoDataInDataBaseException e) {
            log.warn("updateTimer error, timerUpdateReq: {}", timerUpdateReq, e);
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("updateTimer error, timerUpdateReq: {}", timerUpdateReq, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/timer/get-current-timer")
    public Result getCurrentTimer() {
        try {
            TimerDTO timerDTO = timerService.getCurrentTimer();
            if (timerDTO == null) {
                return ResultFactory.buildSuccessResult(null);
            } else {
                TimerSimpleResp respData = TimerDTO.toSimpleResp(timerDTO);
                return ResultFactory.buildSuccessResult(respData);
            }
        } catch (Exception e) {
            log.error("getCurrentTimer error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/timer/get-timer-week-analysis")
    public Result getTimerWeekAnalysis() {
        try {
            List<TimerDayDTO> timerDayDTOList = timerService.getTimerWeekAnalysis();
            TimerWeekAnalysisResp respData = TimerDayDTO.toWeekAnalysisResp(timerDayDTOList);
            return ResultFactory.buildSuccessResult(respData);
        } catch (Exception e) {
            log.error("getTimerWeekAnalysis error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/timer/get-timer-month-analysis")
    public Result getTimerMonthAnalysis() {
        try {
            List<TimerMonthDTO> timerMonthDTOList = timerService.getTimerMonthAnalysis();
            TimerMonthAnalysisResp respData = TimerMonthDTO.toMonthAnalysisResp(timerMonthDTOList);
            return ResultFactory.buildSuccessResult(respData);
        } catch (Exception e) {
            log.error("getTimerMonthAnalysis error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

}