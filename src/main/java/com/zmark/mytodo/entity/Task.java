package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Integer id;
    @Column(nullable = false, name = "title")
    private String title;
    @Column(nullable = false, name = "completed")
    private Boolean completed;
    @Column(nullable = false, name = "completed_time")
    private Timestamp completedTime;
    @Column(nullable = false, name = "archived")
    private Boolean archived;
    @OneToOne
    @JoinColumn(name = "task_content_info_id", insertable = true, updatable = true)
    private TaskContentInfo taskContentInfo;
    @OneToOne
    @JoinColumn(name = "task_priority_info_id", insertable = true, updatable = true)
    private TaskPriorityInfo taskPriorityInfo;
    @OneToOne
    @JoinColumn(name = "task_time_info_id", insertable = true, updatable = true)
    private TaskTimeInfo taskTimeInfo;
    @Column(nullable = false, name = "create_time")
    private Timestamp createTime;
    @Column(nullable = false, name = "update_time")
    private Timestamp updateTime;
}
