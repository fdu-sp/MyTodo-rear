# MyTodo-rear

初版：2023年秋季学期《智能移动平台应用开发》课程PJ的后端

后续：作为2024年春季学期《软件实践》课程小组项目继续开发和维护

# 更新说明

v1.0.7: 2024年06月20日
v1.0.6: 2024年06月14日

# 技术栈

- Java 17
- Spring Boot 3
- MySQL 8.0
- 数据库操作：Jpa
- 日志：Slf4j

# 开发指南

1. 配置好数据库
2. 基于[application-secrets.properties.example](./src/main/resources/application-secrets.properties.example)创建
   application-secrets.properties，并进行相应的配置
3. 用idea打开本项目进行开发

## 数据库配置

数据库配置有两种方式，可以使用本地数据库或者使用docker启动的mysql服务。

### 方式一：使用本地的mysql服务

1. 修改配置文件[application.properties](./src/main/resources/application.properties)，启用dev配置

```properties
spring.profiles.active=dev
```

2. MySQL初始化：

- 方式一：运行 `src/main/resources/init.sql` 文件
- 方式二：手动运行下面的 SQL 语句

```shell
mysql> CREATE DATABASE my_todo;
mysql> CREATE USER 'my_todo_admin'@'%' IDENTIFIED BY 'password123';
mysql> GRANT ALL ON my_todo.* TO 'my_todo_admin'@'%';
mysql> FLUSH PRIVILEGES;
mysql> quit
```

### 方式二：使用docker-compose部署MySQL

1. 修改配置文件[application.properties](./src/main/resources/application.properties)，启用prod置

  ```properties
  spring.profiles.active=prod
  ```

2. 使用docker-compose部署MySQL-->见[使用docker-compose部署MySQL](#使用docker-compose部署mysql)

# 使用docker与docker-compose进行部署

## 使用docker-compose部署mysql

启动MySQL-Docker，会使用sh脚本，如果启动失败，可以通过vscode修改 转换换行符：

- 打开[脚本文件](mytodo-mysql-docker/wait-for-it.sh)。
- 点击底部的换行符显示选项，将行尾序列更改为`LF`。

```Shell
cd mytodo-mysql-docker
docker-compose up -d
```

## 后端docker镜像打包

> 不选择进行多阶段构建，而是本机构建 jar 包，因为多阶段构建速度慢

1. 打包jar：maven-->MyTodo-->生命周期-->package （如果测试无法通过，可能需要使用`@Disabled`注解禁用测试）

2. 构建docker镜像

```shell
# 在项目根目录下进行
docker build -t mytodo-rear:latest .
```

## 使用docker部署后端

```shell
# 单行命令
docker run -d --add-host=host.docker.internal:host-gateway --name mytodo-rear -p 8787:8787 -e SERVER_PORT=8787 -e MYSQL_HOST=host.docker.internal -e MYSQL_PORT=9003 -e MYSQL_USER_NAME=my_todo_admin -e MYSQL_USER_PASSWORD=password123 mytodo-rear:latest 

# 多行命令
docker run -d \
--add-host=host.docker.internal:host-gateway \
--name mytodo-rear \
-p 8788:8787 \
-e SERVER_PORT=8787 \
-e MYSQL_HOST=host.docker.internal \
-e MYSQL_PORT=9003 \
-e MYSQL_USER_NAME=my_todo_admin \
-e MYSQL_USER_PASSWORD=password123 \
mytodo-rear:latest 
```

- `-d` :后台运行容器，并返回容器ID
- `-add-host=host.docker.internal:host-gateway` : 添加一个自定义的主机到容器中，将容器内部的`host.docker.internal`
  解析为Docker宿主机的IP地址
- `--name` :指定容器名
- `-p` :指定端口映射
- `-e` :指定环境变量，环境变量配置参考下面的表格；
    - `host.docker.internal`是Docker Desktop中的一个特殊域名，用于在容器内访问宿主机的地址
- 最后的`mytodo-rear:latest`指定镜像

### 环境变量配置

在配置文件[application-prod.properties](./src/main/resources/application-prod.properties)中使用

| 变量名                   | 说明         | 默认值         |
|-----------------------|------------|-------------|
| `SERVER_PORT`         | 服务端口       | `8787`      |
| `MYSQL_HOST`          | MySQL 主机地址 | `localhost` |
| `MYSQL_PORT`          | MySQL 端口   | `9003`      |
| `MYSQL_USER_NAME`     | MySQL 用户名  | `root`      |
| `MYSQL_USER_PASSWORD` | MySQL 密码   | `root`      |
| `MYSQL_DATABASE`      | MySQL 数据库名 | `my_todo`   |

# CI/CD

## CI部分：

- main分支上的push会触发CI，进行编译构建，在构建过程中会运行测试用例。

## CD部分：

采用主干开发，主干发布的方式。

先设置 GitHub Secrets ，请按照以下步骤操作：

1. 打开 GitHub 仓库。
2. 转到 Settings（设置）。
3. 在左侧菜单中选择 Secrets and variables -> Actions。
4. 点击 New repository secret。
5. 添加以下两个秘密变量：

  - DOCKER_USERNAME: 你的 Docker Hub 用户名。
  - DOCKER_PASSWORD: 你的 Docker Hub 密码。

main分支上，提交时给提交打上vx.x.x的tag，推送到github后，action会被触发，完成jar包构建、镜像构建和镜像推送。

会推送两个镜像，一个带有tag x.x.x，一个带有tag latest。