/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
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
