package com.zmark.mytodo.bo.task.resp.inner;

import com.zmark.mytodo.entity.TaskTimeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

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
    private Date endDate;
    private Time endTime;
    private Timestamp reminderTimestamp;
    private Boolean activateCountdown;
    private Date expectedExecutionDate;
    private Time expectedExecutionStartPeriod;
    private Time expectedExecutionEndPeriod;
}
