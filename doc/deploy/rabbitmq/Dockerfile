FROM rabbitmq:4.04-management

LABEL maintainer="Miraitowa_zcx"

ADD ./rabbitmq_delayed_message_exchange-4.0.2.ez /plugins

RUN rabbitmq-plugins enable rabbitmq_delayed_message_exchange

ENTRYPOINT ["rabbitmq-server"]
