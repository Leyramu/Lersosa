FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/service/job/logs \
    /lersosa/service/job/temp \
    /lersosa/skywalking/agent

WORKDIR /lersosa/service/job

ENV SERVER_PORT=9203 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/lersosa-service-job-encrypted.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
            #-Dskywalking.agent.service_name=lersosa-service-job \
            #-javaagent:/lersosa/skywalking/agent/skywalking-agent.jar \
            -javaagent:app.jar="password=Zcx@223852//" \
            -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
            -jar app.jar

