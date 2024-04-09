package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.TaskTimeInfoDAO;
import com.zmark.mytodo.dto.reminder.TaskReminderInfo;
import com.zmark.mytodo.entity.TaskTimeInfo;
import com.zmark.mytodo.service.api.IReminderService;
import com.zmark.mytodo.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/19 14:38
 */
@Service
public class ReminderService implements IReminderService {
    private final TaskTimeInfoDAO taskTimeInfoDAO;

    @Autowired
    public ReminderService(TaskTimeInfoDAO taskTimeInfoDAO) {
        this.taskTimeInfoDAO = taskTimeInfoDAO;
    }

    @Override
    public List<TaskReminderInfo> getTaskReminderInfoList() {
        // TODO 在后端获取时间，会不会刚好错过提醒时间？
        List<TaskTimeInfo> taskTimeInfoRespList = taskTimeInfoDAO.findAllByReminderTimestampAfterAndTask_Completed(TimeUtils.now(), false);
        return TaskTimeInfo.toTaskReminderInfoList(taskTimeInfoRespList);
    }
}
