# 基础镜像
FROM openjdk:22-jdk
# 作者
MAINTAINER Miaitowa_zcx
# 挂载目录
VOLUME /home/leyramu/lersosa
# 创建目录
RUN mkdir -p /home/leyramu/lersosa
# 指定路径
WORKDIR /home/leyramu/lersosa
# 复制jar文件到路径
COPY ./jar/lersosa-cloud-sentinel.jar /home/leyramu/lersosa/lersosa-cloud-sentinel.jar
# 启动服务
ENTRYPOINT ["java","-jar","lersosa-cloud-sentinel.jar"]
