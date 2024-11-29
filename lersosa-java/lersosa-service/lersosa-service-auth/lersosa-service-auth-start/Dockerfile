FROM bellsoft/liberica-openjdk-debian:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/service/auth/logs  \
    /lersosa/service/auth/temp  \
    /lersosa/skywalking/agent

# 安装字体包
RUN apt-get update && apt-get install -y --no-install-recommends \
    fontconfig \
    fonts-dejavu \
    && rm -rf /var/lib/apt/lists/*

# 更新 Fontconfig 配置
RUN fc-cache -f -v

WORKDIR /lersosa/service/auth

ENV SERVER_PORT=9210 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/lersosa-service-auth-encrypted.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
            #-Dskywalking.agent.service_name=lersosa-service-auth \
            #-javaagent:/lersosa/skywalking/agent/skywalking-agent.jar \
            -javaagent:app.jar="password=Zcx@223852//" \
            -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
            -jar app.jar

