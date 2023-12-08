package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.utils.TimeUtils;
import com.zmark.mytodo.bo.task.resp.inner.TaskContentInfoResp;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task_content_info")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskContentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false, name = "task_id")
    private Task task;

    @Column(nullable = false, name = "description")
    @Builder.Default
    private String description = "";

    @Column(nullable = false, name = "create_time")
    @Builder.Default
    private Timestamp createTime = TimeUtils.now();

    @Column(nullable = false, name = "update_time")
    @Builder.Default
    private Timestamp updateTime = TimeUtils.now();

    public static TaskContentInfoResp from(TaskContentInfo taskContentInfo) {
        return TaskContentInfoResp.builder()
                .description(taskContentInfo.getDescription())
                .createTime(taskContentInfo.getCreateTime().toString())
                .updateTime(taskContentInfo.getCreateTime().toString())
                .build();
    }
}
