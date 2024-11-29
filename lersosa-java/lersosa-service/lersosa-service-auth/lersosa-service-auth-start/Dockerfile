FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/service/auth/logs  \
    /lersosa/service/auth/temp  \
    /lersosa/skywalking/agent

RUN apk update && apk add fontconfig

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

