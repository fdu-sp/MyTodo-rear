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
@Table(name = "task_content_info")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskContentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(nullable = false, name = "task_id")
    private Integer taskId;
    @Column(nullable = false, name = "description")
    private String description;
    @Column(nullable = false, name = "create_time")
    private Timestamp createTime;
    @Column(nullable = false, name = "update_time")
    private Timestamp updateTime;
}
