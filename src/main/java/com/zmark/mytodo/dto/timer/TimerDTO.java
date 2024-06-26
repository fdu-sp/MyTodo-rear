package com.zmark.mytodo.dto.timer;

import com.zmark.mytodo.bo.timer.resp.TimerSimpleResp;
import com.zmark.mytodo.entity.Timer;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;
    private Boolean completed;

    public static TimerDTO from(Timer timer) {
        return TimerDTO.builder()
                .id(timer.getId())
                .taskId(timer.getTaskId())
                .startTimestamp(timer.getStartTimestamp())
                .endTimestamp(timer.getEndTimestamp())
                .completed(timer.getCompleted())
                .build();
    }

    public static TimerSimpleResp toSimpleResp(TimerDTO timerDTO) {
        return TimerSimpleResp.builder()
                .id(timerDTO.getId())
                .taskId(timerDTO.getTaskId())
                .startTimestamp(TimeUtils.toString(timerDTO.getStartTimestamp()))
                .build();
    }
}
