/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
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
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.gen.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.datasource.DataSourceMonitor;
import org.anyline.data.runtime.DataRuntime;
import org.anyline.util.ConfigTable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

/**
 * anyline 适配 动态数据源改造.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@Component
public class MyBatisDataSourceMonitor implements DataSourceMonitor {

    private final Map<String, String> features = new HashMap<>();

    public MyBatisDataSourceMonitor() {
        // 调整执行模式为自定义
        ConfigTable.KEEP_ADAPTER = 2;
        // 禁用缓存
        ConfigTable.METADATA_CACHE_SCOPE = 0;
    }

    /**
     * 数据源特征 用来定准 adapter 包含数据库或JDBC协议关键字.
     *
     * @param datasource 数据源
     * @return String 返回null由上层自动提取
     */
    @Override
    public String feature(DataRuntime runtime, Object datasource) {
        String feature = null;
        if (datasource instanceof JdbcTemplate jdbc) {
            DataSource ds = jdbc.getDataSource();
            if (ds instanceof DynamicRoutingDataSource) {
                String key = DynamicDataSourceContextHolder.peek();
                feature = features.get(key);
                if (null == feature) {
                    Connection con = null;
                    try {
                        con = DataSourceUtils.getConnection(ds);
                        DatabaseMetaData meta = con.getMetaData();
                        String url = meta.getURL();
                        feature = meta.getDatabaseProductName().toLowerCase().replace(" ", "") + "_" + url;
                        features.put(key, feature);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        if (null != con && !DataSourceUtils.isConnectionTransactional(con, ds)) {
                            DataSourceUtils.releaseConnection(con, ds);
                        }
                    }
                }
            }
        }
        return feature;
    }

    /**
     * 数据源唯一标识 如果不实现则默认feature.
     *
     * @param datasource 数据源
     * @return String 返回null由上层自动提取
     */
    @Override
    public String key(DataRuntime runtime, Object datasource) {
        if (datasource instanceof JdbcTemplate jdbc) {
            DataSource ds = jdbc.getDataSource();
            if (ds instanceof DynamicRoutingDataSource) {
                return DynamicDataSourceContextHolder.peek();
            }
        }
        return runtime.getKey();
    }

    /**
     * ConfigTable.KEEP_ADAPTER=2 : 根据当前接口判断是否保持同一个数据源绑定同一个adapter.
     *
     * @param datasource 数据源
     * @return boolean
     */
    @Override
    public boolean keepAdapter(DataRuntime runtime, Object datasource) {
        if (datasource instanceof JdbcTemplate jdbc) {
            DataSource ds = jdbc.getDataSource();
            return !(ds instanceof DynamicRoutingDataSource);
        }
        return true;
    }
}
