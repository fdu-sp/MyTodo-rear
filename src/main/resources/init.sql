-- prepare mysql
-- create database
DROP DATABASE IF EXISTS my_todo;
CREATE DATABASE IF NOT EXISTS my_todo;
-- create user and grant privileges
CREATE USER IF NOT EXISTS 'my_todo_admin'@'%' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON my_todo.* TO 'my_todo_admin'@'%';
FLUSH PRIVILEGES;