FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/visual/sentinel/logs \
    /lersosa/skywalking/agent

WORKDIR /lersosa/visual/sentinel

ENV JAVA_OPTS=""

EXPOSE 8718

ADD ./target/lersosa-visual-sentinel-encrypted.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom \
            #-Dskywalking.agent.service_name=lersosa-visual-sentinel \
            #-javaagent:/lersosa/skywalking/agent/skywalking-agent.jar \
            -javaagent:app.jar="password=Zcx@223852//" \
            ${JAVA_OPTS} \
            -jar app.jar
