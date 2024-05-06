INSERT INTO task_group (id, name, create_time, update_time)
VALUES (1, '默认分组', '2023-12-05 16:15:21', '2023-12-05 16:15:21');

INSERT INTO task_list (id, name, create_time, update_time)
VALUES (1, '默认清单', '2023-12-05 16:15:21', '2023-12-05 16:15:21');

INSERT INTO task_group (id, name, create_time, update_time)
VALUES (2, '专业课程', '2023-12-05 16:15:21', '2023-12-05 16:15:21');

INSERT INTO task_list (id, name, group_id)
VALUES (2, '智能移动平台应用开发', 2),
       (3, '软件设计', 2),
       (4, '操作系统（H）', 2),
       (5, '计算机网络', 2),
       (6, '机器学习', 2),
       (7, '概率论与数理统计', 2),
       (8, '数据库设计（H）', 2),
       (9, '分布式系统', 2);

INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (1, '概率论与数理统计HW11', 0, null, 0, '2023-12-05 16:15:21', '2023-12-05 16:15:21');
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (2, '《安卓开发》Lab4', 0, null, 0, '2023-12-05 16:15:21', '2023-12-05 16:15:21');
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (3, '《安卓开发》Lab3', 0, null, 0, '2023-12-05 16:16:05', '2023-12-05 16:16:05');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (1, 1, 'Lab2', '2023-12-05 16:15:21', '2023-12-05 16:15:21');
INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (2, 2, 'Lab4', '2023-12-05 16:15:21', '2023-12-05 16:15:21');
INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (3, 3, '', '2023-12-05 16:16:05', '2023-12-05 16:16:05');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (1, 1, 1, 0);
INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (2, 2, 1, 0);
INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (3, 3, 1, 1);


INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period, reminder_timestamp)
VALUES (1, 1, '2023-10-20', '23:59:59', 0, '2023-12-09', '08:00:00', '12:00:00', '2024-05-06 23:23:23');
INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period, reminder_timestamp)
VALUES (2, 2, '2023-12-20', '23:59:59', 0, '2023-12-09', '08:00:00', '12:00:00', '2024-05-06 21:23:23');
INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period, reminder_timestamp)
VALUES (3, 3, '2023-12-20', '23:59:59', 0, '2023-12-09', '08:00:00', '12:00:00', '2024-04-02 12:23:23');


INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (1, '大学', null, '2023-12-05 16:16:05', '2023-12-05 16:16:05');
INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (2, '课程', 1, '2023-12-05 16:16:05', '2023-12-05 16:16:05');
INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (3, '智能移动平台开发', 2, '2023-12-05 16:16:05', '2023-12-05 16:16:05');
INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (4, '操作系统（H)', 2, '2023-12-05 16:16:05', '2023-12-05 16:16:05');
INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (5, '概率论与数理统计', 2, '2023-12-05 16:16:05', '2023-12-05 16:16:05');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (1, 1, 5),
       (2, 2, 3),
       (3, 3, 3);

INSERT INTO timer (id, task_id, start_time, end_time, completed)
VALUES (1, 1, '2024-05-04 15:02:03', null, 0);

-- Task 4
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (4, '计算机网络作业', 0, null, 0, '2023-12-05 17:30:00', '2023-12-05 17:30:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (4, 4, 'Chapter 5 Exercises', '2023-12-05 17:30:00', '2023-12-05 17:30:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (4, 4, 1, 0);

INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (4, 4, '2023-12-15', '23:59:59', 0, '2023-12-09', '10:00:00', '14:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (6, '计算机网络', 2, '2023-12-05 17:30:00', '2023-12-05 17:30:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (4, 4, 6);


-- Task 5
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (5, '人工智能实验', 0, null, 0, '2023-12-05 18:00:00', '2023-12-05 18:00:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (5, 5, 'Decision Trees Implementation', '2023-12-05 18:00:00', '2023-12-05 18:00:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (5, 5, 1, 1);

INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (5, 5, '2023-12-18', '23:59:59', 0, '2023-12-10', '09:00:00', '13:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (7, '人工智能', 2, '2023-12-05 18:00:00', '2023-12-05 18:00:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (5, 5, 7);


-- Task 6
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (6, '软件测试项目', 0, null, 0, '2023-12-05 18:30:00', '2023-12-05 18:30:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (6, 6, 'Test Case Design', '2023-12-05 18:30:00', '2023-12-05 18:30:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (6, 6, 0, 1);

INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (6, 6, '2023-12-22', '23:59:59', 0, '2023-12-12', '14:00:00', '18:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (8, '软件测试', 2, '2023-12-05 18:30:00', '2023-12-05 18:30:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (6, 6, 8);

-- Task 7
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (7, '算法设计作业', 0, null, 0, '2023-12-06 09:45:00', '2023-12-06 09:45:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (7, 7, 'Dynamic Programming Problems', '2023-12-06 09:45:00', '2023-12-06 09:45:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (7, 7, 1, 1);

INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (7, 7, '2023-12-15', '23:59:59', 0, '2023-12-09', '11:00:00', '15:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (9, '算法', 2, '2023-12-06 09:45:00', '2023-12-06 09:45:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (7, 7, 9);


-- Task 8
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (8, '数据库系统实验', 0, null, 0, '2023-12-06 10:30:00', '2023-12-06 10:30:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (8, 8, 'Normalization and Indexing', '2023-12-06 10:30:00', '2023-12-06 10:30:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (8, 8, 0, 1);

INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (8, 8, '2023-12-20', '23:59:59', 0, '2023-12-10', '10:00:00', '14:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (10, '数据库', 2, '2023-12-06 10:30:00', '2023-12-06 10:30:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (8, 8, 10);


-- Task 9
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (9, '计算机图形学项目', 0, null, 0, '2023-12-06 11:15:00', '2023-12-06 11:15:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (9, 9, '3D Rendering Implementation', '2023-12-06 11:15:00', '2023-12-06 11:15:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (9, 9, 1, 0);

INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (9, 9, '2023-12-25', '23:59:59', 0, '2023-12-15', '13:00:00', '17:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (11, '计算机图形学', 2, '2023-12-06 11:15:00', '2023-12-06 11:15:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (9, 9, 11);

-- Task 10
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (10, '软件工程作业', 0, null, 0, '2023-12-06 12:00:00', '2023-12-06 12:00:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (10, 10, 'Requirements Specification', '2023-12-06 12:00:00', '2023-12-06 12:00:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (10, 10, 1, 0);

INSERT INTO task_time_info (id, task_id, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (10, 10, 0, '2023-12-15', '10:00:00', '14:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (12, '软件工程', 2, '2023-12-06 12:00:00', '2023-12-06 12:00:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (10, 10, 12);


-- Task 11
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (11, '计算机组成原理实验', 0, null, 0, '2023-12-06 13:00:00', '2023-12-06 13:00:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (11, 11, 'CPU Design Simulation', '2023-12-06 13:00:00', '2023-12-06 13:00:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (11, 11, 0, 1);

INSERT INTO task_time_info (id, task_id, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (11, 11, 0, '2023-12-18', '09:00:00', '13:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (13, '计算机组成原理', 2, '2023-12-06 13:00:00', '2023-12-06 13:00:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (11, 11, 13);


-- Task 12
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (12, '人机交互设计项目', 0, null, 0, '2023-12-06 14:00:00', '2023-12-06 14:00:00');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (12, 12, 'UI Prototyping', '2023-12-06 14:00:00', '2023-12-06 14:00:00');

INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (12, 12, 1, 0);

INSERT INTO task_time_info (id, task_id, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (12, 12, 0, '2023-12-22', '14:00:00', '18:00:00');

INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (14, '人机交互设计', 2, '2023-12-06 14:00:00', '2023-12-06 14:00:00');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (12, 12, 14);
