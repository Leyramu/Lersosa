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
 * Repository interface for aggregated metrics data.
 *
 * @param <T> type of metrics
 * @author Eric Zhao
 */
public interface MetricsRepository<T> {

    /**
     * Save the metric to the storage repository.
     *
     * @param metric metric data to save
     */
    void save(T metric);

    /**
     * Save all metrics to the storage repository.
     *
     * @param metrics metrics to save
     */
    void saveAll(Iterable<T> metrics);

    /**
     * Get all metrics by {@code appName} and {@code resourceName} between a period of time.
     *
     * @param app       application name for Sentinel
     * @param resource  resource name
     * @param startTime start timestamp
     * @param endTime   end timestamp
     * @return all metrics in query conditions
     */
    List<T> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime);

    /**
     * List resource name of provided application name.
     *
     * @param app application name
     * @return list of resources
     */
    List<String> listResourcesOfApp(String app);
}
