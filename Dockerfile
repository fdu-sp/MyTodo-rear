FROM openjdk:17-jdk-alpine
MAINTAINER ZMark <351479188@qq.com> 
ADD target/MyTodo-0.0.1-SNAPSHOT.jar target.jar
ENTRYPOINT ["java","-jar","target.jar"]