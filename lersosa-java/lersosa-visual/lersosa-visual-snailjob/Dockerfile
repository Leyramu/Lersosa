FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/visual/snailjob/logs

WORKDIR /lersosa/visual/snailjob

ENV JAVA_OPTS="-Xms512m -Xmx1024m" SERVER_PORT=8800 SNAILJOB_PORT=17888

EXPOSE ${SERVER_PORT}
EXPOSE ${SNAILJOB_PORT}

ADD ./target/lersosa-visual-snailjob.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom \
            -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
            -jar app.jar
