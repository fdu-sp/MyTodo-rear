package com.zmark.mytodo.controller;

import com.zmark.mytodo.dto.reminder.TaskReminderInfo;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.IReminderService;
import com.zmark.mytodo.service.impl.ReminderService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/19 14:36
 */
@Slf4j
@RestController
public class ReminderController {

    private final IReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping("/api/reminder/get-all")
    public Result getAll() {
        try {
            List<TaskReminderInfo> taskReminderInfoList = reminderService.getTaskReminderInfoList();
            return ResultFactory.buildSuccessResult(TaskReminderInfo.toRespList(taskReminderInfoList));
        } catch (Exception e) {
            log.error("getAll error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/reminder/get-recent/{hour}")
    public Result getRecent(
            @PathVariable("hour")
            @Min(value = 1, message = "时间间隔必须为正整数（以小时计）")
            Integer hour) {
        try {
            List<TaskReminderInfo> taskReminderInfoList = reminderService.getTaskReminderInfoList(hour);
            return ResultFactory.buildSuccessResult(TaskReminderInfo.toRespList(taskReminderInfoList));
        } catch (Exception e) {
            log.error("getAll error", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }
}
