/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.repository.metric;

import java.util.List;

/**
 * 用于聚合指标数据的存储库界面.
 *
 * @param <T> type of metrics
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
public interface MetricsRepository<T> {

    /**
     * 将指标保存到存储库.
     *
     * @param metric 要保存的指标数据
     */
    void save(T metric);

    /**
     * 将所有指标保存到存储库.
     *
     * @param metrics 要保存的指标
     */
    void saveAll(Iterable<T> metrics);

    /**
     * 通过 {@code appName} 和 {@code resourceName} 获取一段时间内的所有指标.
     *
     * @param app       Sentinel 的应用程序名称
     * @param resource  资源名称
     * @param startTime 开始时间戳
     * @param endTime   结束时间戳
     * @return 查询条件中的所有指标
     */
    List<T> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime);

    /**
     * 列出提供的应用程序名称的资源名称.
     *
     * @param app 应用程序名称
     * @return 资源列表
     */
    List<String> listResourcesOfApp(String app);
}
