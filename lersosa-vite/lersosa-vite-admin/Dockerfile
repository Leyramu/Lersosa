# 创建编译镜像
FROM nginx:1.27.5 AS builder

# 安装编译依赖
RUN apt update && apt install -y \
    build-essential \
    libpcre3-dev \
    zlib1g-dev \
    libssl-dev \
    wget \
    git && \
    rm -rf /var/lib/apt/lists/*

# 下载 Nginx 源码
ARG NGINX_VERSION=1.27.5
ARG NGX_CACHE_PURGE=2.5.3
RUN wget http://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz && \
    tar -zxvf nginx-${NGINX_VERSION}.tar.gz && \
    rm nginx-${NGINX_VERSION}.tar.gz

# 克隆 ngx_cache_purge 模块
RUN wget https://github.com/nginx-modules/ngx_cache_purge/archive/refs/tags/${NGX_CACHE_PURGE}.tar.gz && \
    tar -zxvf ${NGX_CACHE_PURGE}.tar.gz && \
    mv ngx_cache_purge-${NGX_CACHE_PURGE} /ngx_cache_purge && \
    rm ${NGX_CACHE_PURGE}.tar.gz

# 编译 Nginx 并添加 ngx_cache_purge 模块
WORKDIR /nginx-${NGINX_VERSION}
RUN ./configure \
    --prefix=/etc/nginx \
    --sbin-path=/usr/sbin/nginx \
    --conf-path=/etc/nginx/nginx.conf \
    --error-log-path=/var/log/nginx/error.log \
    --http-log-path=/var/log/nginx/access.log \
    --pid-path=/var/run/nginx.pid \
    --with-http_ssl_module \
    --add-module=/ngx_cache_purge && \
    make && make install

# 创建最终镜像
FROM nginx:1.27.5 AS runtime

# 安装运行时依赖
RUN apt update && apt install -y \
    libpcre3 \
    zlib1g \
    openssl && \
    rm -rf /var/lib/apt/lists/*

# 从构建阶段复制编译好的 Nginx
COPY --from=builder /usr/sbin/nginx /usr/sbin/nginx
COPY --from=builder /etc/nginx /etc/nginx
COPY --from=builder /var/log/nginx /var/log/nginx
