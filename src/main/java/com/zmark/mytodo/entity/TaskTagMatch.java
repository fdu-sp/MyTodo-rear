package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/*
-- --------------------------
-- Table structure for 任务与标签对应表
-- --------------------------
DROP TABLE IF EXISTS `task_tag`;
CREATE TABLE `task_tag`
(
    `id`      INT NOT NULL AUTO_INCREMENT,
    `task_id` INT NOT NULL COMMENT '任务id',
    `tag_id`  INT NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task_tag_match")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskTagMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long taskId;

    private Long tagId;
}
