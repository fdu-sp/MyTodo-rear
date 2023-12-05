package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.vo.task.req.TaskCreatReq;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "completed")
    private Boolean completed;

    @Column(nullable = true, name = "completed_time")
    private Timestamp completedTime;

    @Column(nullable = false, name = "archived")
    private Boolean archived;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private TaskContentInfo taskContentInfo;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private TaskPriorityInfo taskPriorityInfo;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private TaskTimeInfo taskTimeInfo;

    @Column(nullable = false, name = "create_time")
    private Timestamp createTime;

    @Column(nullable = false, name = "update_time")
    private Timestamp updateTime;

    public void complete() {
        this.completed = true;
        this.completedTime = new Timestamp(System.currentTimeMillis());
    }

    public static Task fromTaskCreatReq(TaskCreatReq req) {
        TaskPriorityInfo taskPriorityInfo = TaskPriorityInfo.builder()
                .isImportant(req.getIsImportant() != null && req.getIsImportant())
                .isUrgent(req.getIsUrgent() != null && req.getIsUrgent())
                .build();
        TaskContentInfo taskContentInfo = TaskContentInfo.builder()
                .description(req.getDescription())
                .createTime(new Timestamp(System.currentTimeMillis()))
                .updateTime(new Timestamp(System.currentTimeMillis()))
                .build();
        TaskTimeInfo taskTimeInfo = TaskTimeInfo.builder()
                .endDate(req.getEndDate())
                .endTime(req.getEndTime())
                .activateCountdown(req.getActivateCountdown() != null && req.getActivateCountdown())
                .expectedExecutionDate(req.getExpectedExecutionDate())
                .expectedExecutionStartPeriod(req.getExpectedExecutionStartPeriod())
                .expectedExecutionEndPeriod(req.getExpectedExecutionEndPeriod())
                .build();
        Task task = Task.builder()
                .title(req.getTitle())
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
