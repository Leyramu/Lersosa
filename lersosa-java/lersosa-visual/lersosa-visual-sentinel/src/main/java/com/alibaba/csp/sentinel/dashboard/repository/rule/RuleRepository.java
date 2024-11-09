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
 * Interface to store and find rules.
 *
 * @author leyou
 */
public interface RuleRepository<T, ID> {

    /**
     * Save one.
     *
     * @param entity
     * @return
     */
    T save(T entity);

    /**
     * Save all.
     *
     * @param rules
     * @return rules saved.
     */
    List<T> saveAll(List<T> rules);

    /**
     * Delete by id
     *
     * @param id
     * @return entity deleted
     */
    T delete(ID id);

    /**
     * Find by id.
     *
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * Find all by machine.
     *
     * @param machineInfo
     * @return
     */
    List<T> findAllByMachine(MachineInfo machineInfo);

    /**
     * Find all by application.
     *
     * @param appName valid app name
     * @return all rules of the application
     * @since 1.4.0
     */
    List<T> findAllByApp(String appName);

    ///**
    // * Find all by app and enable switch.
    // * @param app
    // * @param enable
    // * @return
    // */
    //List<T> findAllByAppAndEnable(String app, boolean enable);
}
