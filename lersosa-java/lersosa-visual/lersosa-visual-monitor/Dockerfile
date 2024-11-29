FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/visual/monitor/logs

WORKDIR /lersosa/visual/monitor

ENV JAVA_OPTS=""

EXPOSE 9120

ADD ./target/lersosa-visual-monitor-encrypted.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom \
            -javaagent:app.jar="password=Zcx@223852//" \
            -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
            -jar app.jar
