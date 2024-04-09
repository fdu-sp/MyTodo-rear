package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.reminder.TaskReminderInfo;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/19 14:38
 */
public interface IReminderService {
    /**
     * 获取所有需要提醒的任务：任务状态为未完成，且提醒时间有效（在现在之后）<br/>
     * <p>
     *
     * @return 所有需要提醒的任务
     */
    List<TaskReminderInfo> getTaskReminderInfoList();
}
