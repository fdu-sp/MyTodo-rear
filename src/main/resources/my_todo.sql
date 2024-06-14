/*
 Navicat Premium Data Transfer

 Source Server         : yys
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : my_todo

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 14/06/2024 17:39:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for my_day_task
-- ----------------------------
DROP TABLE IF EXISTS `my_day_task`;
CREATE TABLE `my_day_task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL COMMENT '任务id',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `my_day_task_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of my_day_task
-- ----------------------------
BEGIN;
INSERT INTO `my_day_task` VALUES (2, 2);
INSERT INTO `my_day_task` VALUES (1, 3);
INSERT INTO `my_day_task` VALUES (13, 13);
INSERT INTO `my_day_task` VALUES (4, 14);
INSERT INTO `my_day_task` VALUES (12, 21);
COMMIT;

-- ----------------------------
-- Table structure for step_info
-- ----------------------------
DROP TABLE IF EXISTS `step_info`;
CREATE TABLE `step_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL COMMENT '任务id',
  `step_seq_number` int NOT NULL COMMENT '步骤在任务中的序号',
  `title` varchar(255) NOT NULL COMMENT '步骤标题',
  `completed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '步骤是否完成',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `step_info_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(255) NOT NULL COMMENT '标签名称',
  `parent_tag_id` bigint DEFAULT NULL COMMENT '父标签id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tag
-- ----------------------------
BEGIN;
INSERT INTO `tag` VALUES (1, '大学', NULL, '2024-05-05 16:16:05', '2024-05-05 16:16:05');
INSERT INTO `tag` VALUES (2, '课程', 1, '2024-05-05 16:16:05', '2024-05-05 16:16:05');
INSERT INTO `tag` VALUES (3, '智能移动平台开发', 2, '2024-05-05 16:16:05', '2024-05-05 16:16:05');
INSERT INTO `tag` VALUES (4, '操作系统（H)', 2, '2024-05-05 16:16:05', '2024-05-05 16:16:05');
INSERT INTO `tag` VALUES (5, '概率论与数理统计', 2, '2024-05-05 16:16:05', '2024-05-05 16:16:05');
INSERT INTO `tag` VALUES (6, '计算机网络', 2, '2024-05-05 17:30:00', '2024-05-05 17:30:00');
INSERT INTO `tag` VALUES (7, '人工智能', 2, '2024-05-05 18:00:00', '2024-05-05 18:00:00');
INSERT INTO `tag` VALUES (8, '软件测试', 2, '2024-05-05 18:30:00', '2024-05-05 18:30:00');
INSERT INTO `tag` VALUES (9, '算法', 2, '2024-05-06 09:45:00', '2024-05-06 09:45:00');
INSERT INTO `tag` VALUES (10, '数据库', 2, '2024-05-06 10:30:00', '2024-05-06 10:30:00');
INSERT INTO `tag` VALUES (11, '计算机图形学', 2, '2024-05-06 11:15:00', '2024-05-06 11:15:00');
INSERT INTO `tag` VALUES (12, '软件工程', 2, '2024-05-06 12:00:00', '2024-05-06 12:00:00');
INSERT INTO `tag` VALUES (13, '计算机组成原理', 2, '2024-05-06 13:00:00', '2024-05-06 13:00:00');
INSERT INTO `tag` VALUES (14, '人机交互设计', 2, '2024-05-06 14:00:00', '2024-05-06 14:00:00');
COMMIT;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL COMMENT '任务标题',
  `completed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否完成',
  `completed_time` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  `list_id` bigint NOT NULL DEFAULT '1' COMMENT '任务清单id',
  `archived` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否归档',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `list_id` (`list_id`),
  CONSTRAINT `task_ibfk_1` FOREIGN KEY (`list_id`) REFERENCES `task_list` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of task
-- ----------------------------
BEGIN;
INSERT INTO `task` VALUES (1, '概率论与数理统计HW11', 1, NULL, 2, 1, '2024-05-05 16:15:21', '2024-05-05 16:15:21');
INSERT INTO `task` VALUES (2, '《安卓开发》Lab4', 1, '2024-06-14 16:49:10', 2, 0, '2024-05-05 16:15:21', '2024-05-05 16:15:21');
INSERT INTO `task` VALUES (3, '《安卓开发》Lab3', 1, '2024-06-14 16:09:26', 2, 0, '2024-05-05 16:16:05', '2024-05-05 16:16:05');
INSERT INTO `task` VALUES (4, '计算机网络作业', 1, NULL, 1, 0, '2024-05-05 17:30:00', '2024-05-05 17:30:00');
INSERT INTO `task` VALUES (5, '人工智能实验', 0, NULL, 1, 0, '2024-05-05 18:00:00', '2024-05-05 18:00:00');
INSERT INTO `task` VALUES (6, '软件测试项目', 0, NULL, 1, 0, '2024-05-05 18:30:00', '2024-05-05 18:30:00');
INSERT INTO `task` VALUES (7, '算法设计作业', 0, NULL, 1, 0, '2024-05-06 09:45:00', '2024-05-06 09:45:00');
INSERT INTO `task` VALUES (8, '数据库系统实验', 0, NULL, 1, 0, '2024-05-06 10:30:00', '2024-05-06 10:30:00');
INSERT INTO `task` VALUES (9, '计算机图形学项目', 0, NULL, 1, 0, '2024-05-06 11:15:00', '2024-05-06 11:15:00');
INSERT INTO `task` VALUES (10, '软件工程作业', 0, NULL, 1, 0, '2024-05-06 12:00:00', '2024-05-06 12:00:00');
INSERT INTO `task` VALUES (11, '计算机组成原理实验', 0, NULL, 1, 0, '2024-05-06 13:00:00', '2024-05-06 13:00:00');
INSERT INTO `task` VALUES (12, '人机交互设计项目', 0, NULL, 1, 0, '2024-05-06 14:00:00', '2024-05-06 14:00:00');
INSERT INTO `task` VALUES (13, '计算机网络课程报告', 0, NULL, 5, 0, '2024-06-14 12:35:44', '2024-06-14 12:35:44');
INSERT INTO `task` VALUES (14, '机器学习课程论文', 0, NULL, 6, 0, '2024-06-14 12:36:34', '2024-06-14 12:36:34');
INSERT INTO `task` VALUES (15, '操作系统考试', 0, NULL, 4, 0, '2024-06-14 12:37:47', '2024-06-14 12:37:47');
INSERT INTO `task` VALUES (16, '概率论与数理统计考试', 0, NULL, 7, 0, '2024-06-14 12:38:35', '2024-06-14 12:38:35');
INSERT INTO `task` VALUES (17, '操作系统lab4', 0, NULL, 4, 0, '2024-06-14 12:40:09', '2024-06-14 12:40:09');
INSERT INTO `task` VALUES (18, '软件设计PJ', 0, NULL, 3, 0, '2024-06-14 17:20:26', '2024-06-14 17:20:26');
INSERT INTO `task` VALUES (19, '分布式系统考试', 0, NULL, 9, 0, '2024-06-14 17:21:36', '2024-06-14 17:21:36');
INSERT INTO `task` VALUES (21, '计算机网络考试', 0, NULL, 5, 0, '2024-06-14 17:18:50', '2024-06-14 17:18:50');
INSERT INTO `task` VALUES (22, '软件设计Lab4', 0, NULL, 3, 0, '2024-06-14 17:24:49', '2024-06-14 17:24:49');
COMMIT;

-- ----------------------------
-- Table structure for task_content_info
-- ----------------------------
DROP TABLE IF EXISTS `task_content_info`;
CREATE TABLE `task_content_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL COMMENT '任务id',
  `description` text NOT NULL COMMENT '任务描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `task_content_info_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of task_content_info
-- ----------------------------
BEGIN;
INSERT INTO `task_content_info` VALUES (1, 1, 'Lab2', '2024-05-05 16:15:21', '2024-05-05 16:15:21');
INSERT INTO `task_content_info` VALUES (2, 2, 'Lab4', '2024-05-05 16:15:21', '2024-05-05 16:15:21');
INSERT INTO `task_content_info` VALUES (3, 3, '', '2024-05-05 16:16:05', '2024-05-05 16:16:05');
INSERT INTO `task_content_info` VALUES (4, 4, 'Chapter 5 Exercises', '2024-05-05 17:30:00', '2024-05-05 17:30:00');
INSERT INTO `task_content_info` VALUES (5, 5, 'Decision Trees Implementation', '2024-05-05 18:00:00', '2024-05-05 18:00:00');
INSERT INTO `task_content_info` VALUES (6, 6, 'Test Case Design', '2024-05-05 18:30:00', '2024-05-05 18:30:00');
INSERT INTO `task_content_info` VALUES (7, 7, 'Dynamic Programming Problems', '2024-05-06 09:45:00', '2024-05-06 09:45:00');
INSERT INTO `task_content_info` VALUES (8, 8, 'Normalization and Indexing', '2024-05-06 10:30:00', '2024-05-06 10:30:00');
INSERT INTO `task_content_info` VALUES (9, 9, '3D Rendering Implementation', '2024-05-06 11:15:00', '2024-05-06 11:15:00');
INSERT INTO `task_content_info` VALUES (10, 10, 'Requirements Specification', '2024-05-06 12:00:00', '2024-05-06 12:00:00');
INSERT INTO `task_content_info` VALUES (11, 11, 'CPU Design Simulation', '2024-05-06 13:00:00', '2024-05-06 13:00:00');
INSERT INTO `task_content_info` VALUES (12, 12, 'UI Prototyping', '2024-05-06 14:00:00', '2024-05-06 14:00:00');
INSERT INTO `task_content_info` VALUES (14, 13, '', '2024-06-14 12:35:44', '2024-06-14 12:35:44');
INSERT INTO `task_content_info` VALUES (16, 14, '', '2024-06-14 12:36:34', '2024-06-14 12:36:34');
INSERT INTO `task_content_info` VALUES (18, 15, '', '2024-06-14 12:37:47', '2024-06-14 12:37:47');
INSERT INTO `task_content_info` VALUES (20, 16, '', '2024-06-14 12:38:35', '2024-06-14 12:38:35');
INSERT INTO `task_content_info` VALUES (22, 17, '操作系统', '2024-06-14 12:40:09', '2024-06-14 12:40:09');
INSERT INTO `task_content_info` VALUES (31, 21, '', '2024-06-14 17:18:50', '2024-06-14 17:18:50');
INSERT INTO `task_content_info` VALUES (32, 18, 'software design project', '2024-06-14 17:20:26', '2024-06-14 17:20:26');
INSERT INTO `task_content_info` VALUES (34, 19, '', '2024-06-14 17:21:36', '2024-06-14 17:21:36');
INSERT INTO `task_content_info` VALUES (38, 22, 'software design Lab4', '2024-06-14 17:24:49', '2024-06-14 17:24:49');
COMMIT;

-- ----------------------------
-- Table structure for task_group
-- ----------------------------
DROP TABLE IF EXISTS `task_group`;
CREATE TABLE `task_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '分组名称',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '分组描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of task_group
-- ----------------------------
BEGIN;
INSERT INTO `task_group` VALUES (1, '默认分组', '', '2024-05-05 16:15:21', '2024-05-05 16:15:21');
INSERT INTO `task_group` VALUES (2, '专业课程', '', '2024-05-05 16:15:21', '2024-05-05 16:15:21');
COMMIT;

-- ----------------------------
-- Table structure for task_list
-- ----------------------------
DROP TABLE IF EXISTS `task_list`;
CREATE TABLE `task_list` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '清单名称',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '清单描述',
  `group_id` bigint NOT NULL DEFAULT '1' COMMENT '分组id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `task_list_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `task_group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of task_list
-- ----------------------------
BEGIN;
INSERT INTO `task_list` VALUES (1, '默认清单', '', 1, '2024-05-05 16:15:21', '2024-05-05 16:15:21');
INSERT INTO `task_list` VALUES (2, '智能移动平台应用开发', '', 2, '2024-06-14 12:31:07', '2024-06-14 12:31:07');
INSERT INTO `task_list` VALUES (3, '软件设计', '', 2, '2024-06-14 12:31:07', '2024-06-14 12:31:07');
INSERT INTO `task_list` VALUES (4, '操作系统（H）', '', 2, '2024-06-14 12:31:07', '2024-06-14 12:31:07');
INSERT INTO `task_list` VALUES (5, '计算机网络', '', 2, '2024-06-14 12:31:07', '2024-06-14 12:31:07');
INSERT INTO `task_list` VALUES (6, '机器学习', '', 2, '2024-06-14 12:31:07', '2024-06-14 12:31:07');
INSERT INTO `task_list` VALUES (7, '概率论与数理统计', '', 2, '2024-06-14 12:31:07', '2024-06-14 12:31:07');
INSERT INTO `task_list` VALUES (8, '数据库设计（H）', '', 2, '2024-06-14 12:31:07', '2024-06-14 12:31:07');
INSERT INTO `task_list` VALUES (9, '分布式系统', '', 2, '2024-06-14 12:31:07', '2024-06-14 12:31:07');
COMMIT;

-- ----------------------------
-- Table structure for task_priority_info
-- ----------------------------
DROP TABLE IF EXISTS `task_priority_info`;
CREATE TABLE `task_priority_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL COMMENT '任务id',
  `is_important` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否重要',
  `is_urgent` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否紧急',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `task_priority_info_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of task_priority_info
-- ----------------------------
BEGIN;
INSERT INTO `task_priority_info` VALUES (1, 1, 1, 0);
INSERT INTO `task_priority_info` VALUES (2, 2, 1, 0);
INSERT INTO `task_priority_info` VALUES (3, 3, 1, 1);
INSERT INTO `task_priority_info` VALUES (4, 4, 1, 0);
INSERT INTO `task_priority_info` VALUES (5, 5, 1, 1);
INSERT INTO `task_priority_info` VALUES (6, 6, 0, 1);
INSERT INTO `task_priority_info` VALUES (7, 7, 1, 1);
INSERT INTO `task_priority_info` VALUES (8, 8, 0, 1);
INSERT INTO `task_priority_info` VALUES (9, 9, 1, 0);
INSERT INTO `task_priority_info` VALUES (10, 10, 1, 0);
INSERT INTO `task_priority_info` VALUES (11, 11, 0, 1);
INSERT INTO `task_priority_info` VALUES (12, 12, 1, 0);
INSERT INTO `task_priority_info` VALUES (14, 13, 0, 0);
INSERT INTO `task_priority_info` VALUES (16, 14, 0, 0);
INSERT INTO `task_priority_info` VALUES (18, 15, 0, 0);
INSERT INTO `task_priority_info` VALUES (20, 16, 0, 0);
INSERT INTO `task_priority_info` VALUES (22, 17, 0, 0);
INSERT INTO `task_priority_info` VALUES (31, 21, 0, 0);
INSERT INTO `task_priority_info` VALUES (32, 18, 0, 0);
INSERT INTO `task_priority_info` VALUES (34, 19, 0, 0);
INSERT INTO `task_priority_info` VALUES (38, 22, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for task_tag_match
-- ----------------------------
DROP TABLE IF EXISTS `task_tag_match`;
CREATE TABLE `task_tag_match` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL COMMENT '任务id',
  `tag_id` bigint NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `task_tag_match_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `task_tag_match_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of task_tag_match
-- ----------------------------
BEGIN;
INSERT INTO `task_tag_match` VALUES (1, 1, 5);
INSERT INTO `task_tag_match` VALUES (2, 2, 3);
INSERT INTO `task_tag_match` VALUES (3, 3, 3);
INSERT INTO `task_tag_match` VALUES (4, 4, 6);
INSERT INTO `task_tag_match` VALUES (5, 5, 7);
INSERT INTO `task_tag_match` VALUES (6, 6, 8);
INSERT INTO `task_tag_match` VALUES (7, 7, 9);
INSERT INTO `task_tag_match` VALUES (8, 8, 10);
INSERT INTO `task_tag_match` VALUES (9, 9, 11);
INSERT INTO `task_tag_match` VALUES (10, 10, 12);
INSERT INTO `task_tag_match` VALUES (11, 11, 13);
INSERT INTO `task_tag_match` VALUES (12, 12, 14);
COMMIT;

-- ----------------------------
-- Table structure for task_time_info
-- ----------------------------
DROP TABLE IF EXISTS `task_time_info`;
CREATE TABLE `task_time_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL COMMENT '任务id',
  `end_date` date DEFAULT NULL COMMENT '截止日期',
  `end_time` time DEFAULT NULL COMMENT '截止时间',
  `reminder_timestamp` timestamp NULL DEFAULT NULL COMMENT '提醒时间戳',
  `activate_countdown` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否激活倒计时',
  `expected_execution_date` date DEFAULT NULL COMMENT '预计执行日期',
  `expected_execution_start_period` time DEFAULT NULL COMMENT '预计执行开始时间段',
  `expected_execution_end_period` time DEFAULT NULL COMMENT '预计执行结束时间段',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `task_time_info_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of task_time_info
-- ----------------------------
BEGIN;
INSERT INTO `task_time_info` VALUES (1, 1, '2024-06-14', '23:59:59', '2024-06-14 23:23:23', 0, '2024-06-14', '08:00:00', '12:00:00');
INSERT INTO `task_time_info` VALUES (2, 2, '2024-06-14', '23:59:59', '2024-06-14 21:23:23', 0, '2024-06-14', '08:00:00', '12:00:00');
INSERT INTO `task_time_info` VALUES (3, 3, '2024-06-14', '23:59:59', '2024-06-14 12:23:23', 0, '2024-06-14', '08:00:00', '12:00:00');
INSERT INTO `task_time_info` VALUES (4, 4, '2024-05-15', '23:59:59', NULL, 0, '2024-05-09', '10:00:00', '14:00:00');
INSERT INTO `task_time_info` VALUES (5, 5, '2024-05-18', '23:59:59', NULL, 0, '2024-05-10', '09:00:00', '13:00:00');
INSERT INTO `task_time_info` VALUES (6, 6, '2024-05-22', '23:59:59', NULL, 0, '2024-05-12', '14:00:00', '18:00:00');
INSERT INTO `task_time_info` VALUES (7, 7, '2024-05-15', '23:59:59', NULL, 0, '2024-05-09', '11:00:00', '15:00:00');
INSERT INTO `task_time_info` VALUES (8, 8, '2024-05-20', '23:59:59', NULL, 0, '2024-05-10', '10:00:00', '14:00:00');
INSERT INTO `task_time_info` VALUES (9, 9, '2024-05-25', '23:59:59', NULL, 0, '2024-05-15', '13:00:00', '17:00:00');
INSERT INTO `task_time_info` VALUES (10, 10, NULL, NULL, NULL, 0, '2024-05-15', '10:00:00', '14:00:00');
INSERT INTO `task_time_info` VALUES (11, 11, NULL, NULL, NULL, 0, '2024-05-18', '09:00:00', '13:00:00');
INSERT INTO `task_time_info` VALUES (12, 12, NULL, NULL, NULL, 0, '2024-05-22', '14:00:00', '18:00:00');
INSERT INTO `task_time_info` VALUES (14, 13, '2024-06-16', NULL, '2024-06-15 12:19:00', 0, NULL, NULL, NULL);
INSERT INTO `task_time_info` VALUES (16, 14, '2024-06-17', NULL, '2024-06-14 22:40:00', 0, NULL, NULL, NULL);
INSERT INTO `task_time_info` VALUES (18, 15, '2024-06-22', NULL, '2024-06-21 19:44:00', 0, NULL, NULL, NULL);
INSERT INTO `task_time_info` VALUES (20, 16, '2024-06-19', NULL, '2024-06-18 21:40:00', 0, NULL, NULL, NULL);
INSERT INTO `task_time_info` VALUES (22, 17, '2024-06-19', NULL, '2024-06-18 00:00:00', 0, NULL, NULL, NULL);
INSERT INTO `task_time_info` VALUES (31, 21, '2024-06-15', NULL, '2024-06-14 17:19:00', 0, NULL, NULL, NULL);
INSERT INTO `task_time_info` VALUES (32, 18, '2024-06-17', NULL, '2024-06-16 22:36:00', 0, NULL, NULL, NULL);
INSERT INTO `task_time_info` VALUES (34, 19, '2024-06-17', NULL, '2024-06-16 09:39:00', 0, NULL, NULL, NULL);
INSERT INTO `task_time_info` VALUES (38, 22, '2024-06-22', NULL, '2024-06-21 00:00:00', 0, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for timer
-- ----------------------------
DROP TABLE IF EXISTS `timer`;
CREATE TABLE `timer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL COMMENT '关联任务id',
  `start_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_timestamp` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `completed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '任务在本次专注过程中是否完成',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `timer_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of timer
-- ----------------------------
BEGIN;
INSERT INTO `timer` VALUES (1, 1, '2024-06-09 15:02:03', '2024-06-09 16:02:03', 0);
INSERT INTO `timer` VALUES (2, 2, '2024-06-10 15:02:03', '2024-06-10 18:50:03', 0);
INSERT INTO `timer` VALUES (3, 3, '2024-06-11 15:02:03', '2024-06-11 19:10:03', 0);
INSERT INTO `timer` VALUES (4, 1, '2024-06-12 15:02:03', '2024-06-12 16:20:03', 0);
INSERT INTO `timer` VALUES (5, 2, '2024-06-13 15:02:03', '2024-06-13 18:36:03', 0);
INSERT INTO `timer` VALUES (6, 3, '2024-06-14 15:02:03', '2024-06-14 17:10:03', 0);
INSERT INTO `timer` VALUES (7, 4, '2024-06-09 15:02:03', '2024-06-09 16:30:03', 0);
INSERT INTO `timer` VALUES (8, 5, '2024-06-10 15:02:03', '2024-06-10 16:02:03', 0);
INSERT INTO `timer` VALUES (9, 6, '2024-06-11 15:02:03', '2024-06-11 16:02:03', 0);
INSERT INTO `timer` VALUES (10, 4, '2024-06-12 15:02:03', '2024-06-12 16:24:03', 0);
INSERT INTO `timer` VALUES (11, 5, '2024-06-13 15:02:03', '2024-06-13 16:52:03', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
