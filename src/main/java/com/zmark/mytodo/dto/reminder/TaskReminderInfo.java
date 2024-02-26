package com.zmark.mytodo.dto.reminder;

import com.zmark.mytodo.bo.reminder.TaskReminderInfoResp;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/19 14:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReminderInfo {
    private Long taskId;
    private Timestamp reminderTimestamp;

    public TaskReminderInfoResp toResp() {
        return TaskReminderInfoResp.builder()
                .taskId(taskId)
                .reminderTimestamp(TimeUtils.toString(reminderTimestamp))
                .build();
    }

    public static List<TaskReminderInfoResp> toRespList(List<TaskReminderInfo> taskReminderInfoList) {
        return taskReminderInfoList.stream().map(TaskReminderInfo::toResp).toList();
    }
}
