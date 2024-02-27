# MyTodo-rear

2023年秋季学期《智能移动平台应用开发》课程PJ的后端

## 运行指南

### 数据库配置

方式一：运行 `src/main/resources/init.sql` 文件

方式二：手动运行下面的 SQL 语句

```shell
mysql> CREATE DATABASE my_todo;
mysql> CREATE USER 'my_todo_admin'@'%' IDENTIFIED BY 'password123';
mysql> GRANT ALL ON my_todo.* TO 'my_todo_admin'@'%';
mysql> FLUSH PRIVILEGES;
mysql> quit
```

# 使用docker部署

## MySQL docker镜像打包

```Shell
cd data
docker build -t mytodo-mysql:1.0.0 .
```

## 使用docker部署mysql

1. 设置docker卷来持久化数据库

```Shell
docker volume create mytodo-mysql-volume
docker volume ls
```

2. 运行MySQL Docker容器

```Shell
docker run -d --name=mytodo-mysql -p 9003:3306 -v mytodo-mysql-volume:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root mytodo-mysql:1.0.0
```

- `-d` 将以分离模式运行此容器，以便它在后台运行。
- `--name` 将名称`mytodo-mysql`分配给容器实例。如果不指定此项，Docker 将生成一个随机名称。
- `-p` 将 MySQL 容器端口3306绑定到主机上的9003端口（因为我们本机的3306被本机的mysql占用了，所以映射到9003端口）。之后可以在主机上运行的MySQL客户端连接到127.0.0.1:9003
- `-v` 选项将容器卷 ( /var/lib/mysql)内的数据文件夹绑定到`mytodo-mysql-volume`卷，即上一步中创建的本地 Docker 卷
- `-e` 设置环境变量。这里，我们设置了MySQL容器root用户的密码。
- 最后的`mytodo-mysql:1.0.0` 是我们用来创建容器的image的名称

## 后端docker镜像打包

1. 修改配置文件`src\main\resources\application.properties`，启用prod-docker配置

```properties
spring.profiles.active=prod-docker
```

2. 打包jar：maven-->MyTTodo-->生命周期-->package （如果测试无法通过，可能需要使用`@Disabled`注解禁用测试）

3. 构建docker镜像

```shell
docker build -t mytodo-rear-deploy:1.0.0 .
```

## 使用docker部署后端

```shell
docker run -d --name mytodo-rear -p 8788:8787 --link mytodo-mysql:mysql -e "SERVER_PORT=8787" -e "MYSQL_HOST=mysql" -e "MYSQL_PORT=3306" -e "MYSQL_USER_NAME=root" -e "MYSQL_USER_PASSWORD=root" mytodo-rear-deploy:1.0.0 
```

- `-d` :后台运行容器，并返回容器ID
- `--name` :指定容器名
- `-p` :指定端口映射
- `--link 容器名：别名`，这里的别名`mysql`通过环境变量`MYSQL_HOST`传递，它代表着`mysql`连接着容器`mytodo-mysql`，需要注意mytod-mysql是容器应该先被启动
- `-e` :指定环境变量，环境变量配置参考下面的表格
- 最后的`mytodo-rear-deploy:1.0.0`指定镜像

### 环境变量配置（配置文件`application-prod-docker.properties`）

| 变量名                | 说明           | 默认值      |
| --------------------- | -------------- | ----------- |
| `SERVER_PORT`         | 服务端口       | `8787`      |
| `MYSQL_HOST`          | MySQL 主机地址 | `localhost` |
| `MYSQL_PORT`          | MySQL 端口     | `3306`      |
| `MYSQL_USER_NAME`     | MySQL 用户名   | `root`      |
| `MYSQL_USER_PASSWORD` | MySQL 密码     | `root`      |
| `MYSQL_DATABASE`      | MySQL 数据库名 | `my_todo`   |