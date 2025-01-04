FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/visual/sentinel/logs \
    /lersosa/skywalking/agent

WORKDIR /lersosa/visual/sentinel

ENV SERVER_PORT=8718 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/lersosa-visual-sentinel.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom \
            -Dskywalking.agent.service_name=lersosa-visual-sentinel \
            -javaagent:app.jar="password=Zcx@223852//,/lersosa/skywalking/agent/skywalking-agent.jar" \
            ${JAVA_OPTS} \
            -jar app.jar
