/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.demo.domain.TestDemo;
import leyramu.framework.lersosa.demo.mapper.TestDemoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试批量方法
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/batch")
public class TestBatchController extends BaseController {

    /**
     * 为了便于测试 直接引入mapper
     */
    private final TestDemoMapper testDemoMapper;

    /**
     * 新增批量方法 可完美替代 saveBatch 秒级插入上万数据 (对mysql负荷较大)
     * <p>
     * 3.5.0 版本 增加 rewriteBatchedStatements=true 批处理参数 使 MP 原生批处理可以达到同样的速度
     */
    @PostMapping("/add")
//    @DS("slave")
    public R<Void> add() {
        List<TestDemo> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            TestDemo testDemo = new TestDemo();
            testDemo.setOrderNum(-1);
            testDemo.setTestKey("批量新增");
            testDemo.setValue("测试新增");
            list.add(testDemo);
        }
        return toAjax(testDemoMapper.insertBatch(list));
    }

    /**
     * 新增或更新 可完美替代 saveOrUpdateBatch 高性能
     * <p>
     * 3.5.0 版本 增加 rewriteBatchedStatements=true 批处理参数 使 MP 原生批处理可以达到同样的速度
     */
    @PostMapping("/addOrUpdate")
//    @DS("slave")
    public R<Void> addOrUpdate() {
        List<TestDemo> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            TestDemo testDemo = new TestDemo();
            testDemo.setOrderNum(-1);
            testDemo.setTestKey("批量新增");
            testDemo.setValue("测试新增");
            list.add(testDemo);
        }
        testDemoMapper.insertBatch(list);
        for (int i = 0; i < list.size(); i++) {
            TestDemo testDemo = list.get(i);
            testDemo.setTestKey("批量新增或修改");
            testDemo.setValue("批量新增或修改");
            if (i % 2 == 0) {
                testDemo.setId(null);
            }
        }
        return toAjax(testDemoMapper.insertOrUpdateBatch(list));
    }

    /**
     * 删除批量方法
     */
    @DeleteMapping()
//    @DS("slave")
    public R<Void> remove() {
        return toAjax(testDemoMapper.delete(new LambdaQueryWrapper<TestDemo>()
            .eq(TestDemo::getOrderNum, -1L)));
    }

}
