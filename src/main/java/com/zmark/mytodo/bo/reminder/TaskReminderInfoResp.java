package com.zmark.mytodo.bo.reminder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/19 14:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReminderInfoResp {
    private Long taskId;
    private String reminderTimestamp;
}
