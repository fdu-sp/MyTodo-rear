package com.zmark.mytodo.bo.task.resp.inner;

import com.zmark.mytodo.entity.TaskTimeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/4 10:18
 * @see TaskTimeInfo 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskTimeInfoResp {
    private String endDate;
    private String endTime;
    private String reminderTimestamp;
    private Boolean activateCountdown;
    private String expectedExecutionDate;
    private String expectedExecutionStartPeriod;
    private String expectedExecutionEndPeriod;

    public TaskTimeInfoResp(String reminderTimestamp){
        this.reminderTimestamp = reminderTimestamp;
    }
}
