package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task_time_info")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskTimeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(nullable = false, name = "task_id")
    private Integer taskId;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "end_time")
    private Time endTime;
    @Column(name = "activate_countdown")
    private Boolean activateCountdown;
    @Column(name = "expected_execution_date")
    private Date expectedExecutionDate;
    @Column(name = "expected_execution_start_period")
    private Time expectedExecutionStartPeriod;
    @Column(name = "expected_execution_end_period")
    private Time expectedExecutionEndPeriod;
}
