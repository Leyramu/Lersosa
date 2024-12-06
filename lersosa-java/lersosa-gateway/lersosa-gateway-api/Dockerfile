FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/gateway/api/logs  \
    /lersosa/gateway/api/temp  \
    /lersosa/skywalking/agent

WORKDIR /lersosa/gateway/api

ENV SERVER_PORT=8080 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/lersosa-gateway-api-encrypted.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
            -Dskywalking.agent.service_name=lersosa-gateway-api \
            -javaagent:app.jar="password=Zcx@223852//,/lersosa/skywalking/agent/skywalking-agent.jar" \
            -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
            -jar app.jar
