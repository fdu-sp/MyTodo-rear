package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Boolean isImportant;
    @Column(nullable = false, name = "is_urgent")
    private Boolean isUrgent;
}
