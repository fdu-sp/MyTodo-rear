package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.bo.task.resp.inner.TaskPriorityInfoResp;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task_priority_info")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskPriorityInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false, name = "task_id")
    private Task task;

    @Column(nullable = false, name = "is_important")
    @Builder.Default
    private Boolean isImportant = false;

    @Column(nullable = false, name = "is_urgent")
    @Builder.Default
    private Boolean isUrgent = false;

    public static TaskPriorityInfoResp from(TaskPriorityInfo taskPriorityInfo) {
        return TaskPriorityInfoResp.builder()
                .isImportant(taskPriorityInfo.getIsImportant())
                .isUrgent(taskPriorityInfo.getIsUrgent())
                .build();
    }
}
