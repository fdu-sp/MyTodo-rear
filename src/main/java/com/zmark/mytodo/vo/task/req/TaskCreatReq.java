package com.zmark.mytodo.vo.task.req;

import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.TaskContentInfo;
import com.zmark.mytodo.entity.TaskPriorityInfo;
import com.zmark.mytodo.entity.TaskTimeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 10:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreatReq {
    @Valid
    @NotNull(message = "任务标题不能为空")
    private String title;

    private List<String> tagNames;

    /**
     * content description
     */
    private String description;

    /**
     * priority info
     */
    private Boolean isImportant;
    private Boolean isUrgent;

    /**
     * time info
     */
    private Date endDate;
    private Time endTime;
    private Boolean activateCountdown;
    private Date expectedExecutionDate;
    private Time expectedExecutionStartPeriod;
    private Time expectedExecutionEndPeriod;

    public Task toTaskDTO() {
        TaskPriorityInfo taskPriorityInfo = TaskPriorityInfo.builder()
                .isImportant(this.isImportant != null && this.isImportant)
                .isUrgent(this.isUrgent != null && this.isUrgent)
                .build();
        TaskContentInfo taskContentInfo = TaskContentInfo.builder()
                .description(this.description)
                .build();
        TaskTimeInfo taskTimeInfo = TaskTimeInfo.builder()
                .endDate(endDate)
                .endTime(endTime)
                .activateCountdown(this.activateCountdown != null && this.activateCountdown)
                .expectedExecutionDate(this.expectedExecutionDate)
                .expectedExecutionStartPeriod(this.expectedExecutionStartPeriod)
                .expectedExecutionEndPeriod(this.expectedExecutionEndPeriod)
                .build();
        Task task = Task.builder()
                .title(this.title)
                .completed(false)
                .completedTime(null)
                .archived(false)
                .taskContentInfo(taskContentInfo)
                .taskPriorityInfo(taskPriorityInfo)
                .taskTimeInfo(taskTimeInfo)
                .createTime(new Timestamp(System.currentTimeMillis()))
                .updateTime(new Timestamp(System.currentTimeMillis()))
                .build();
        taskContentInfo.setTask(task);
        taskPriorityInfo.setTask(task);
        taskTimeInfo.setTask(task);
        return task;
    }
}
