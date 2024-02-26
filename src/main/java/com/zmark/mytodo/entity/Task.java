package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.bo.task.req.TaskUpdateReq;
import com.zmark.mytodo.utils.TimeUtils;
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
    @Builder.Default
    private Boolean completed = false;

    @Column(nullable = true, name = "completed_time")
    @Builder.Default
    private Timestamp completedTime = null;

    @Column(nullable = false, name = "list_id")
    @Builder.Default
    private Long taskListId = 1L;

    @Column(nullable = false, name = "archived")
    private Boolean archived;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private TaskContentInfo taskContentInfo;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private TaskPriorityInfo taskPriorityInfo;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private TaskTimeInfo taskTimeInfo;

    @Column(nullable = false, name = "create_time")
    @Builder.Default
    private Timestamp createTime = TimeUtils.now();

    @Column(nullable = false, name = "update_time")
    @Builder.Default
    private Timestamp updateTime = TimeUtils.now();

    public static Task fromTaskUpdateReq(TaskUpdateReq taskUpdateReq) {
        Task task = Task.builder()
                .id(taskUpdateReq.getId())
                .title(taskUpdateReq.getTitle())
                .completed(taskUpdateReq.getCompleted())
                .completedTime(TimeUtils.toTimestamp(taskUpdateReq.getCompletedTime()))
                .taskListId(taskUpdateReq.getTaskListId())
                .archived(taskUpdateReq.getArchived())
                .updateTime(TimeUtils.now())
                .build();
        TaskContentInfo taskContentInfo = TaskContentInfo.fromTaskContentInfoResp(task, taskUpdateReq.getTaskContentInfo());
        TaskPriorityInfo taskPriorityInfo = TaskPriorityInfo.fromTaskPriorityInfoResp(task, taskUpdateReq.getTaskPriorityInfo());
        TaskTimeInfo taskTimeInfo = TaskTimeInfo.fromTaskTimeInfoResp(task, taskUpdateReq.getTaskTimeInfo());
        task.setTaskContentInfo(taskContentInfo);
        task.setTaskPriorityInfo(taskPriorityInfo);
        task.setTaskTimeInfo(taskTimeInfo);
        return task;
    }

    public void complete() {
        this.completed = true;
        this.completedTime = new Timestamp(System.currentTimeMillis());
    }

    public void unComplete() {
        this.completed = false;
        this.completedTime = null;
    }

    public static Task fromTaskCreatReq(TaskCreateReq req) {
        TaskPriorityInfo taskPriorityInfo = TaskPriorityInfo.builder()
                .isImportant(req.getIsImportant() != null && req.getIsImportant())
                .isUrgent(req.getIsUrgent() != null && req.getIsUrgent())
                .build();
        TaskContentInfo taskContentInfo = TaskContentInfo.builder()
                .description(req.getDescription())
                .build();
        TaskTimeInfo taskTimeInfo = TaskTimeInfo.builder()
                .endDate(TimeUtils.toDate(req.getEndDate()))
                .endTime(TimeUtils.toTime(req.getEndTime()))
                .reminderTimestamp(TimeUtils.toTimestamp(req.getReminderTimestamp()))
                .activateCountdown(req.getActivateCountdown() != null && req.getActivateCountdown())
                .expectedExecutionDate(req.getExpectedExecutionDate())
                .expectedExecutionStartPeriod(TimeUtils.toTime(req.getExpectedExecutionStartPeriod()))
                .expectedExecutionEndPeriod(TimeUtils.toTime(req.getExpectedExecutionEndPeriod()))
                .build();
        Task task = Task.builder()
                .title(req.getTitle())
                .completed(req.getCompleted())
                .completedTime(null)
                .archived(false)
                .taskListId(req.getTaskListId() == null ? TaskList.DEFAULT_LIST_ID : req.getTaskListId())
                .taskContentInfo(taskContentInfo)
                .taskPriorityInfo(taskPriorityInfo)
                .taskTimeInfo(taskTimeInfo)
                .build();
        taskContentInfo.setTask(task);
        taskPriorityInfo.setTask(task);
        taskTimeInfo.setTask(task);
        return task;
    }
}
