FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/service/openai/logs \
    /lersosa/skywalking/agent

WORKDIR /lersosa/service/openai

ENV SERVER_PORT=9206 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/lersosa-service-openai-encrypted.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
            -Dskywalking.agent.service_name=lersosa-system \
            -javaagent:app.jar="password=Zcx@223852//,/lersosa/skywalking/agent/skywalking-agent.jar" \
            -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
            -jar app.jar

