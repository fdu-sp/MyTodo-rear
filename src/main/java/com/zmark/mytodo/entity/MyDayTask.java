package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/*
DROP TABLE IF EXISTS `my_day_tasks`;
CREATE TABLE `my_day_tasks`
(
    `id`      INT NOT NULL AUTO_INCREMENT,
    `task_id` INT NOT NULL COMMENT '任务id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "my_day_task")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MyDayTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(nullable = false, name = "task_id")
    private Integer taskId;
}
