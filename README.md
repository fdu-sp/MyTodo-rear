# MyTodo-rear

2023年秋季学期《智能移动平台应用开发》课程PJ的后端

## 技术栈

- Java 17
- Spring Boot 3
- MySQL 8.0
- 数据库操作：Jpa
- 日志：Slf4j

# 开发指南

## 数据库配置

方式一：运行 `src/main/resources/init.sql` 文件

方式二：手动运行下面的 SQL 语句

```shell
mysql> CREATE DATABASE my_todo;
mysql> CREATE USER 'my_todo_admin'@'%' IDENTIFIED BY 'password123';
mysql> GRANT ALL ON my_todo.* TO 'my_todo_admin'@'%';
mysql> FLUSH PRIVILEGES;
mysql> quit
```

# 使用docker与docker-compose进行部署

## 使用docker-compose部署mysql

启动MySQL-Docker

```Shell
cd mysql-docker
docker-compose up -d
```

## 后端docker镜像打包

1. 修改配置文件`src\main\resources\application.properties`，启用prod-docker配置

```properties
spring.profiles.active=prod-docker
```

2. 打包jar：maven-->MyTodo-->生命周期-->package （如果测试无法通过，可能需要使用`@Disabled`注解禁用测试）

3. 构建docker镜像

```shell
docker build -t mytodo-rear-deploy:1.0.0 .
```

## 使用docker部署后端

```shell
docker run -d --name mytodo-rear -p 8788:8787 --link MyTodo-mysql:mysql -e "SERVER_PORT=8787" -e "MYSQL_HOST=mysql" -e "MYSQL_PORT=3306" -e "MYSQL_USER_NAME=root" -e "MYSQL_USER_PASSWORD=root" mytodo-rear-deploy:1.0.0 
```

- `-d` :后台运行容器，并返回容器ID
- `--name` :指定容器名
- `-p` :指定端口映射
- `--link 容器名：别名`，这里的别名`mysql`通过环境变量`MYSQL_HOST`传递，它代表着`mysql`连接着容器`MyTodo-mysql`
  ，需要注意 `MyTodo-mysql` 容器应该先被启动；其中`MyTodo-mysql` 是`docker-compose`启动的容器名
- `-e` :指定环境变量，环境变量配置参考下面的表格
- 最后的`mytodo-rear-deploy:1.0.0`指定镜像

### 环境变量配置（见配置文件`application-prod-docker.properties`）

| 变量名                   | 说明         | 默认值         |
|-----------------------|------------|-------------|
| `SERVER_PORT`         | 服务端口       | `8787`      |
| `MYSQL_HOST`          | MySQL 主机地址 | `localhost` |
| `MYSQL_PORT`          | MySQL 端口   | `3306`      |
| `MYSQL_USER_NAME`     | MySQL 用户名  | `root`      |
| `MYSQL_USER_PASSWORD` | MySQL 密码   | `root`      |
| `MYSQL_DATABASE`      | MySQL 数据库名 | `my_todo`   |