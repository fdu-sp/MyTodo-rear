FROM mysql:8.0
MAINTAINER ZMark <351479188@qq.com> 
# 将初始化脚本复制到镜像中的 /docker-entrypoint-initdb.d/ 目录
# todo 还是无法自动执行
COPY init.sql /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/init.sql

