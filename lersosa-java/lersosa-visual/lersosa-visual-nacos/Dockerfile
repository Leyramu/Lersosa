FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/visual/nacos

WORKDIR /lersosa/visual/nacos

ENV TZ=Asia/Shanghai SERVER_PORT=8848 JAVA_OPTS="-Xms512m -Xmx1024m"

EXPOSE ${SERVER_PORT}

ADD ./target/lersosa-visual-nacos.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
            ${JAVA_OPTS} \
            -jar app.jar
