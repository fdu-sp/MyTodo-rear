INSERT INTO task (title, completed, archived)
VALUES ('test1', 0, 0),
       ('test2', 0, 0);

INSERT INTO task_content_info (task_id, description)
VALUES (1, 'test1'),
       (2, 'test2');

INSERT INTO task_time_info (task_id)
VALUES (1),
       (2);

INSERT INTO task_priority_info (task_id)
VALUES (1),
       (2);