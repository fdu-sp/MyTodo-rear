package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.bo.timer.req.TimerCreateReq;
import com.zmark.mytodo.utils.TimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

/**
 * @author Violette
 * @date 2024/5/4 15:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "timer")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_id")
    @Builder.Default
    private Long taskId = null;

    @Column(name = "start_time")
    @Builder.Default
    private Timestamp startTime = null;

    @Column(name = "end_time")
    @Builder.Default
    private Timestamp endTime = null;

    @Column(nullable = false, name = "completed")
    @Builder.Default
    private Boolean completed = false;

    public static Timer fromTimerCreateReq(TimerCreateReq timerCreateReq) {
        return Timer.builder()
                .taskId(timerCreateReq.getTaskId())
                .startTime(TimeUtils.toTimestamp(timerCreateReq.getStartTime()))
                .build();
    }

}
