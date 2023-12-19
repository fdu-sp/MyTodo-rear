package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.bo.task.resp.inner.TaskTimeInfoResp;
import com.zmark.mytodo.dto.reminder.TaskReminderInfo;
import com.zmark.mytodo.utils.TimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

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
    @Builder.Default
    private Date endDate = null;

    @Column(name = "end_time")
    @Builder.Default
    private Time endTime = null;

    @Column(name = "reminder_timestamp")
    @Builder.Default
    private Timestamp reminderTimestamp = null;

    @Column(name = "activate_countdown")
    @Builder.Default
    private Boolean activateCountdown = false;

    @Column(name = "expected_execution_date")
    @Builder.Default
    private Date expectedExecutionDate = null;

    @Column(name = "expected_execution_start_period")
    @Builder.Default
    private Time expectedExecutionStartPeriod = null;

    @Column(name = "expected_execution_end_period")
    @Builder.Default
    private Time expectedExecutionEndPeriod = null;

    public static TaskTimeInfoResp from(TaskTimeInfo taskTimeInfo) {
        return TaskTimeInfoResp.builder()
                .endDate(TimeUtils.toString(taskTimeInfo.getEndDate()))
                .endTime(TimeUtils.toString(taskTimeInfo.getEndTime()))
                .reminderTimestamp(TimeUtils.toString(taskTimeInfo.getReminderTimestamp()))
                .activateCountdown(taskTimeInfo.getActivateCountdown())
                .expectedExecutionDate(TimeUtils.toString(taskTimeInfo.getExpectedExecutionDate()))
                .expectedExecutionStartPeriod(TimeUtils.toString(taskTimeInfo.getExpectedExecutionStartPeriod()))
                .expectedExecutionEndPeriod(TimeUtils.toString(taskTimeInfo.getExpectedExecutionEndPeriod()))
                .build();
    }

    public static TaskTimeInfo fromTaskTimeInfoResp(Task task, TaskTimeInfoResp taskTimeInfo) {
        return TaskTimeInfo.builder()
                .task(task)
                .endDate(TimeUtils.toDate(taskTimeInfo.getEndDate()))
                .endTime(TimeUtils.toTime(taskTimeInfo.getEndTime()))
                .reminderTimestamp(TimeUtils.toTimestamp(taskTimeInfo.getReminderTimestamp()))
                .activateCountdown(taskTimeInfo.getActivateCountdown())
                .expectedExecutionDate(TimeUtils.toDate(taskTimeInfo.getExpectedExecutionDate()))
                .expectedExecutionStartPeriod(TimeUtils.toTime(taskTimeInfo.getExpectedExecutionStartPeriod()))
                .expectedExecutionEndPeriod(TimeUtils.toTime(taskTimeInfo.getExpectedExecutionEndPeriod()))
                .build();
    }

    public TaskReminderInfo toTaskReminderInfo() {
        return TaskReminderInfo.builder()
                .taskId(task.getId())
                .reminderTimestamp(reminderTimestamp)
                .build();
    }

    public static List<TaskReminderInfo> toTaskReminderInfoList(List<TaskTimeInfo> taskTimeInfoList) {
        return taskTimeInfoList.stream().map(TaskTimeInfo::toTaskReminderInfo).toList();
    }

    /**
     * 返回任务的截止时间，如果没有设置，则返回null
     */
    public Timestamp getDueTime() {
        return TimeUtils.combineDateAndTime(endDate, endTime);
    }
}
