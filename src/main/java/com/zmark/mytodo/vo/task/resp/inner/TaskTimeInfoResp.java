package com.zmark.mytodo.vo.task.resp.inner;

import com.zmark.mytodo.entity.TaskTimeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

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
    private Boolean activateCountdown;
    private Date expectedExecutionDate;
    private Time expectedExecutionStartPeriod;
    private Time expectedExecutionEndPeriod;

    public static TaskTimeInfoResp from(TaskTimeInfo taskTimeInfo) {
        return TaskTimeInfoResp.builder()
                .endDate(taskTimeInfo.getEndDate())
                .endTime(taskTimeInfo.getEndTime())
                .activateCountdown(taskTimeInfo.getActivateCountdown())
                .expectedExecutionDate(taskTimeInfo.getExpectedExecutionDate())
                .expectedExecutionStartPeriod(taskTimeInfo.getExpectedExecutionStartPeriod())
                .expectedExecutionEndPeriod(taskTimeInfo.getExpectedExecutionEndPeriod())
                .build();
    }
}
