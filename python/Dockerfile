FROM pytorch/pytorch:2.7.0-cuda12.8-cudnn9-runtime

LABEL maintainer="Miraitowa_zcx"

WORKDIR /lersosa/service/python

COPY requirements.txt ./

RUN --mount=type=cache,target=/root/.cache/pip pip install --upgrade pip setuptools wheel && \
    pip install -r requirements.txt

COPY src ./src

ENV SERVER_PORT=8000 \
    GRPC_PORT=6001

EXPOSE ${SERVER_PORT} ${GRPC_PORT}

ENTRYPOINT ["python", "src/main.py"]
