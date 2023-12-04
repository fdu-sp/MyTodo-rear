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
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "completed")
    private Boolean completed;

    @Column(nullable = false, name = "completed_time")
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
}
