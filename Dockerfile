FROM openjdk:17-jdk
WORKDIR /app
# 复制编译后的jar文件到新的镜像中
COPY  ./target/app.jar app.jar
ENV env=prod
# 运行Spring Boot应用
ENTRYPOINT ["java","-jar","app.jar", "--spring.config.location=classpath:/application-${env}.properties"]