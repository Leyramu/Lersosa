/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.repository.rule;

import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;

import java.util.List;

/**
 * 用于存储和查找规则的接口
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
public interface RuleRepository<T, ID> {

    /**
     * 保存一个
     *
     * @param entity 要保存的实体
     * @return 保存的实体
     */
    T save(T entity);

    /**
     * 全部保存
     *
     * @param rules 规则
     * @return 保存的规则
     */
    List<T> saveAll(List<T> rules);

    /**
     * 按 ID 删除
     *
     * @param id ID
     * @return 实体已删除
     */
    T delete(ID id);

    /**
     * 按 ID 查找
     *
     * @param id ID
     * @return 实体
     */
    T findById(ID id);

    /**
     * 按机器查找全部
     *
     * @param machineInfo 机器信息
     * @return 实体
     */
    List<T> findAllByMachine(MachineInfo machineInfo);

    /**
     * 按应用查找全部
     *
     * @param appName 有效的 App 名称
     * @return 应用程序的所有规则
     */
    List<T> findAllByApp(String appName);
}
