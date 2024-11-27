# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
FROM bellsoft/liberica-openjdk-debian:17.0.11-cds
#FROM bellsoft/liberica-openjdk-debian:21.0.3-cds
#FROM findepi/graalvm:java17-native

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/service/workflow/logs \
    /lersosa/service/workflow/temp \
    /lersosa/skywalking/agent

WORKDIR /lersosa/service/workflow

ENV SERVER_PORT=9205 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/lersosa-service-workflow-encrypted.jar ./app.jar

ENTRYPOINT java -javaagent:app.jar="password=***" -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
           #-Dskywalking.agent.service_name=lersosa-system \
           #-javaagent:/lersosa/skywalking/agent/skywalking-agent.jar \
           -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
           -jar app.jar

