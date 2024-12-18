FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/visual/monitor/logs

WORKDIR /lersosa/visual/monitor

ENV SERVER_PORT=9120 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/lersosa-visual-monitor.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
            -javaagent:app.jar="password=Zcx@223852//" \
            -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
            -jar app.jar
