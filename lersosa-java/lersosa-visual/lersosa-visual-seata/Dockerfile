FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/visual/seata/logs \
    /lersosa/skywalking/agent

WORKDIR /lersosa/visual/seata

ENV TZ=Asia/Shanghai JAVA_OPTS="" SEATA_IP="" SEATA_PORT="" SERVER_PORT=7091

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE ${SERVER_PORT}
EXPOSE ${SEATA_PORT}

ADD ./target/lersosa-visual-seata.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom \
            -Dskywalking.agent.service_name=lersosa-visual-seata \
            -Dskywalking.plugin.seata.server=true \
            -javaagent:/lersosa/skywalking/agent/skywalking-agent.jar \
            ${JAVA_OPTS} \
            -jar app.jar
