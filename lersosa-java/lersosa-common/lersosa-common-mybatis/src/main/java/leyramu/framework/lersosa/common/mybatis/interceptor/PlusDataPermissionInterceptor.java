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

package leyramu.framework.lersosa.common.mybatis.interceptor;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BaseMultiTableInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import leyramu.framework.lersosa.common.mybatis.handler.PlusDataPermissionHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.List;

/**
 * 数据权限拦截器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
public class PlusDataPermissionInterceptor extends BaseMultiTableInnerInterceptor implements InnerInterceptor {

    private final PlusDataPermissionHandler dataPermissionHandler;

    /**
     * 构造函数，初始化 PlusDataPermissionHandler 实例.
     *
     * @param mapperPackage 扫描的映射器包
     */
    public PlusDataPermissionInterceptor(String mapperPackage) {
        this.dataPermissionHandler = new PlusDataPermissionHandler(mapperPackage);
    }

    /**
     * 在执行查询之前，检查并处理数据权限相关逻辑.
     *
     * @param executor      MyBatis 执行器对象
     * @param ms            映射语句对象
     * @param parameter     方法参数
     * @param rowBounds     分页对象
     * @param resultHandler 结果处理器
     * @param boundSql      绑定的 SQL 对象
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        // 检查是否需要忽略数据权限处理
        if (InterceptorIgnoreHelper.willIgnoreDataPermission(ms.getId())) {
            return;
        }
        // 检查是否缺少有效的数据权限注解
        if (dataPermissionHandler.invalid(ms.getId())) {
            return;
        }
        // 解析 sql 分配对应方法
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), ms.getId()));
    }

    /**
     * 在准备 SQL 语句之前，检查并处理更新和删除操作的数据权限相关逻辑.
     *
     * @param sh                 MyBatis StatementHandler 对象
     * @param connection         数据库连接对象
     * @param transactionTimeout 事务超时时间
     */
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        // 获取 SQL 命令类型（增、删、改、查）
        SqlCommandType sct = ms.getSqlCommandType();

        // 只处理更新和删除操作的 SQL 语句
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            if (InterceptorIgnoreHelper.willIgnoreDataPermission(ms.getId())) {
                return;
            }
            // 检查是否缺少有效的数据权限注解
            if (dataPermissionHandler.invalid(ms.getId())) {
                return;
            }
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(parserMulti(mpBs.sql(), ms.getId()));
        }
    }

    /**
     * 处理 SELECT 查询语句中的 WHERE 条件.
     *
     * @param select SELECT 查询对象
     * @param index  查询语句的索引
     * @param sql    查询语句
     * @param obj    WHERE 条件参数
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        if (select instanceof PlainSelect) {
            this.setWhere((PlainSelect) select, (String) obj);
        } else if (select instanceof SetOperationList setOperationList) {
            List<Select> selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach(s -> this.setWhere((PlainSelect) s, (String) obj));
        }
    }

    /**
     * 处理 UPDATE 语句中的 WHERE 条件.
     *
     * @param update UPDATE 查询对象
     * @param index  查询语句的索引
     * @param sql    查询语句
     * @param obj    WHERE 条件参数
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        Expression sqlSegment = dataPermissionHandler.getSqlSegment(update.getWhere(), (String) obj, false);
        if (null != sqlSegment) {
            update.setWhere(sqlSegment);
        }
    }

    /**
     * 处理 DELETE 语句中的 WHERE 条件.
     *
     * @param delete DELETE 查询对象
     * @param index  查询语句的索引
     * @param sql    查询语句
     * @param obj    WHERE 条件参数
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        Expression sqlSegment = dataPermissionHandler.getSqlSegment(delete.getWhere(), (String) obj, false);
        if (null != sqlSegment) {
            delete.setWhere(sqlSegment);
        }
    }

    /**
     * 设置 SELECT 语句的 WHERE 条件.
     *
     * @param plainSelect       SELECT 查询对象
     * @param mappedStatementId 映射语句的 ID
     */
    protected void setWhere(PlainSelect plainSelect, String mappedStatementId) {
        Expression sqlSegment = dataPermissionHandler.getSqlSegment(plainSelect.getWhere(), mappedStatementId, true);
        if (null != sqlSegment) {
            plainSelect.setWhere(sqlSegment);
        }
    }

    /**
     * 构建表达式，用于处理表的数据权限.
     *
     * @param table        表对象
     * @param where        WHERE 条件表达式
     * @param whereSegment WHERE 条件片段
     * @return 构建的表达式
     */
    @Override
    public Expression buildTableExpression(Table table, Expression where, String whereSegment) {
        // 只有新版数据权限处理器才会执行到这里
        final MultiDataPermissionHandler handler = (MultiDataPermissionHandler) dataPermissionHandler;
        return handler.getSqlSegment(table, where, whereSegment);
    }
}

