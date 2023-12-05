INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (1, 'test1', 0, null, 0, '2023-12-05 16:15:21', '2023-12-05 16:15:21');
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (2, 'test2', 0, null, 0, '2023-12-05 16:15:21', '2023-12-05 16:15:21');
INSERT INTO task (id, title, completed, completed_time, archived, create_time, update_time)
VALUES (3, '《安卓开发》Lab3', 0, null, 0, '2023-12-05 16:16:05', '2023-12-05 16:16:05');

INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (1, 1, 'test1', '2023-12-05 16:15:21', '2023-12-05 16:15:21');
INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (2, 2, 'test2', '2023-12-05 16:15:21', '2023-12-05 16:15:21');
INSERT INTO task_content_info (id, task_id, description, create_time, update_time)
VALUES (3, 3, '', '2023-12-05 16:16:05', '2023-12-05 16:16:05');


INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (1, 1, 0, 0);
INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (2, 2, 0, 0);
INSERT INTO task_priority_info (id, task_id, is_important, is_urgent)
VALUES (3, 3, 1, 1);


INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (1, 1, null, null, 0, null, null, null);
INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (2, 2, null, null, 0, null, null, null);
INSERT INTO task_time_info (id, task_id, end_date, end_time, activate_countdown, expected_execution_date,
                            expected_execution_start_period, expected_execution_end_period)
VALUES (3, 3, '2023-12-20', '23:59:59', 0, '2023-12-09', '08:00:00', '12:00:00');


INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (1, '大学', null, '2023-12-05 16:16:05', '2023-12-05 16:16:05');
INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (2, '课程', 1, '2023-12-05 16:16:05', '2023-12-05 16:16:05');
INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (3, '智能移动平台开发', 2, '2023-12-05 16:16:05', '2023-12-05 16:16:05');
INSERT INTO tag (id, tag_name, parent_tag_id, create_time, update_time)
VALUES (4, '操作系统（H)', 2, '2023-12-05 16:16:05', '2023-12-05 16:16:05');

INSERT INTO task_tag_match (id, task_id, tag_id)
VALUES (1, 3, 3);
