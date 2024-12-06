FROM bellsoft/liberica-openjdk-alpine:23.0.1-cds

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/service/grpc/logs

WORKDIR /lersosa/service/grpc

ENV SERVER_PORT=9207 SERVER_GRPC_PORT=6001 JAVA_OPTS=""

EXPOSE ${SERVER_PORT} ${SERVER_GRPC_PORT}

ADD ./target/lersosa-service-grpc-encrypted.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
            -javaagent:app.jar="password=Zcx@223852//" \
            ${JAVA_OPTS} \
            -jar app.jar
