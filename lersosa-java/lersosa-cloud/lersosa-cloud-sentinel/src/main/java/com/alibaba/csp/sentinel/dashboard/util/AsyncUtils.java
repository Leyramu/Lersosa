/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.csp.sentinel.dashboard.util;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用于异步操作的 Utils
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@NoArgsConstructor
public final class AsyncUtils {

    /**
     * 日志记录器
     */
    private static final Logger LOG = LoggerFactory.getLogger(AsyncUtils.class);

    /**
     * 创建一个失败的 CompletableFuture
     *
     * @param ex 异常
     * @return 失败的 CompletableFuture
     */
    public static <R> CompletableFuture<R> newFailedFuture(Throwable ex) {
        CompletableFuture<R> future = new CompletableFuture<>();
        future.completeExceptionally(ex);
        return future;
    }

    /**
     * 将多个 CompletableFuture 转换为 List
     *
     * @param futures CompletableFuture 列表
     * @return 转换后的 List
     */
    public static <R> CompletableFuture<List<R>> sequenceFuture(List<CompletableFuture<R>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(AsyncUtils::getValue)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                );
    }

    /**
     * 并行执行 CompletableFuture，并返回成功的结果列表
     *
     * @param futures CompletableFuture 列表
     * @param <R>     泛型
     * @return List
     */
    public static <R> CompletableFuture<List<R>> sequenceSuccessFuture(List<CompletableFuture<R>> futures) {
        return CompletableFuture.supplyAsync(() -> futures.parallelStream()
                .map(AsyncUtils::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
    }

    /**
     * 获取 CompletableFuture 的值，如果超时则返回 null
     *
     * @param future CompletableFuture
     * @param <T>    泛型
     * @return T
     */
    public static <T> T getValue(CompletableFuture<T> future) {
        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            LOG.error("getValue for async result failed", ex);
        }
        return null;
    }

    /**
     * 判断 CompletableFuture 是否成功
     *
     * @param future CompletableFuture
     * @return boolean
     */
    public static boolean isSuccessFuture(CompletableFuture<Void> future) {
        return future.isDone() && !future.isCompletedExceptionally() && !future.isCancelled();
    }
}
