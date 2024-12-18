FROM continuumio/miniconda3:latest

LABEL maintainer="Miraitowa_zcx"

RUN mkdir -p /lersosa/service/python/miruns

WORKDIR /lersosa/service/python

ADD environment.yml /lersosa/service/python/environment.yml

RUN conda update -n base conda -y

RUN conda env create -f environment.yml && \
    conda clean --all -f -y

ENV SERVER_PORT=8000 PATH="/opt/conda/envs/lersosa-python-env/bin:${PATH}"

EXPOSE ${SERVER_PORT}

ADD src /lersosa/service/python/src

ENTRYPOINT ["python", "src/main.py"]
