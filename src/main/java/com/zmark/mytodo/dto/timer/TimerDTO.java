package com.zmark.mytodo.dto.timer;

import com.zmark.mytodo.bo.timer.resp.TimerSimpleResp;
import com.zmark.mytodo.bo.timer.resp.TimerWeekAnalysisResp;
import com.zmark.mytodo.entity.Timer;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Violette
 * @date 2024/5/4 22:23
 * @see Timer 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerDTO {
    private Long id;
    private Long taskId;
    private Timestamp startTime;
    private Timestamp endTime;
    private Boolean completed;

    public static TimerDTO from(Timer timer) {
        return TimerDTO.builder()
                .id(timer.getId())
                .taskId(timer.getTaskId())
                .startTime(timer.getStartTime())
                .endTime(timer.getEndTime())
                .completed(timer.getCompleted())
                .build();
    }

    public static TimerSimpleResp toSimpleResp(TimerDTO timerDTO) {
        return TimerSimpleResp.builder()
                .id(timerDTO.getId())
                .taskId(timerDTO.getTaskId())
                .startTime(TimeUtils.toString(timerDTO.getStartTime()))
                .build();
    }
}
