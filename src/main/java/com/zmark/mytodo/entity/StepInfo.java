package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.utils.TimeUtils;
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
    private Long id;

    @Column(nullable = false, name = "task_id")
    private Long taskId;

    @Column(nullable = false, name = "step_seq_number")
    private Integer stepSeqNumber;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "completed")
    @Builder.Default
    private Boolean completed = false;

    @Column(nullable = false, name = "create_time")
    @Builder.Default
    private Timestamp createTime = TimeUtils.now();

    @Column(nullable = false, name = "update_time")
    @Builder.Default
    private Timestamp updateTime = TimeUtils.now();
}
