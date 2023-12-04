SET
    FOREIGN_KEY_CHECKS = 0;

-- --------------------------
-- Table structure for task 任务表
-- --------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `title`          varchar(255) NOT NULL COMMENT '任务标题',
    `completed`      boolean      NOT NULL DEFAULT FALSE COMMENT '是否完成',
    `completed_time` timestamp             DEFAULT NULL COMMENT '完成时间',
    `archived`       boolean      NOT NULL DEFAULT FALSE COMMENT '是否归档',
    `create_time`    timestamp             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------
-- Table structure for task_content_info 任务内容信息表
-- --------------------------
DROP TABLE IF EXISTS `task_content_info`;
CREATE TABLE `task_content_info`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT,
    `task_id`     BIGINT NOT NULL COMMENT '任务id',
    `description` TEXT COMMENT '任务描述',
    `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------
-- Table structure for task_time_info 任务时间信息表
-- --------------------------
DROP TABLE IF EXISTS `task_time_info`;
CREATE TABLE `task_time_info`
(
    `id`                              BIGINT  NOT NULL AUTO_INCREMENT,
    `task_id`                         BIGINT  NOT NULL COMMENT '任务id',
    `end_date`                        date             DEFAULT NULL COMMENT '截止日期',
    `end_time`                        time             DEFAULT NULL COMMENT '截止时间',
    `activate_countdown`              boolean NOT NULL DEFAULT FALSE COMMENT '是否激活倒计时',
    `expected_execution_date`         date             DEFAULT NULL COMMENT '预计执行日期',
    `expected_execution_start_period` time             DEFAULT NULL COMMENT '预计执行开始时间段',
    `expected_execution_end_period`   time             DEFAULT NULL COMMENT '预计执行结束时间段',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------
-- Table structure for task_priority_info 任务优先级信息表
-- -------------------------
DROP TABLE IF EXISTS `task_priority_info`;
CREATE TABLE `task_priority_info`
(
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `task_id`      BIGINT NOT NULL COMMENT '任务id',
    `is_important` boolean DEFAULT FALSE COMMENT '是否重要',
    `is_urgent`    boolean DEFAULT FALSE COMMENT '是否紧急',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------
-- Table structure for step_info 任务步骤信息表
-- --------------------------
DROP TABLE IF EXISTS `step_info`;
CREATE TABLE `step_info`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT,
    `task_id`         BIGINT       NOT NULL COMMENT '任务id',
    `step_seq_number` INT          NOT NULL COMMENT '步骤在任务中的序号',
    `title`           varchar(255) NOT NULL COMMENT '步骤标题',
    `completed`       boolean      NOT NULL DEFAULT FALSE COMMENT '步骤是否完成',
    `create_time`     timestamp             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     timestamp             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------
-- Table structure for my_day_task 我的一天任务表
-- --------------------------
DROP TABLE IF EXISTS `my_day_task`;
CREATE TABLE `my_day_task`
(
    `id`      BIGINT NOT NULL AUTO_INCREMENT,
    `task_id` BIGINT NOT NULL COMMENT '任务id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------
-- Table structure for tag 标签表
-- --------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `tag_name`      varchar(255) NOT NULL unique COMMENT '标签名称',
    `parent_tag_id` BIGINT    DEFAULT NULL COMMENT '父标签id',
    `create_time`   timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------
-- Table structure for task_tag_match 任务与标签对应表
-- --------------------------
DROP TABLE IF EXISTS `task_tag_match`;
CREATE TABLE `task_tag_match`
(
    `id`      BIGINT NOT NULL AUTO_INCREMENT,
    `task_id` BIGINT NOT NULL COMMENT '任务id',
    `tag_id`  BIGINT NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
SET
    FOREIGN_KEY_CHECKS = 1;
