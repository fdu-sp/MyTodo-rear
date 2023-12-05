package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.utils.TimeUtils;
import com.zmark.mytodo.vo.task.resp.inner.TaskTimeInfoResp;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task_time_info")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskTimeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne
    @JoinColumn(nullable = false, name = "task_id")
    private Task task;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "end_time")
    private Time endTime;
    @Column(name = "activate_countdown")
    private Boolean activateCountdown;
    @Column(name = "expected_execution_date")
    private Date expectedExecutionDate;
    @Column(name = "expected_execution_start_period")
    private Time expectedExecutionStartPeriod;
    @Column(name = "expected_execution_end_period")
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

    /**
     * 返回任务的截止时间，如果没有设置，则返回null
     */
    public Timestamp getDueTime() {
        return TimeUtils.combineDateAndTime(endDate, endTime);
    }
}
