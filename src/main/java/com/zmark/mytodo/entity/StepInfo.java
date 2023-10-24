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
@Table(name = "step_info")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StepInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(nullable = false, name = "task_id")
    private Integer taskId;
    @Column(nullable = false, name = "step_seq_number")
    private Integer stepSeqNumber;
    @Column(nullable = false, name = "title")
    private String title;
    @Column(nullable = false, name = "completed")
    private Boolean completed;
    @Column(nullable = false, name = "create_time")
    private Timestamp createTime;
    @Column(nullable = false, name = "update_time")
    private Timestamp updateTime;
}
