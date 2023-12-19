package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.reminder.TaskReminderInfo;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/19 14:38
 */
public interface IReminderService {
    List<TaskReminderInfo> getTaskReminderInfoList();
}
