/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.demo.domain.ShardingOrder;
import leyramu.framework.lersosa.demo.mapper.ShardingOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用方式 看官网文档扩展项目
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sharding")
public class TestShardingController {

    private final ShardingOrderMapper torderMapper;

    @GetMapping("/page")
    public R<Page<ShardingOrder>> page() {
        Page<ShardingOrder> page = new Page<>();
        page.setCurrent(3L);
        LambdaQueryWrapper<ShardingOrder> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(ShardingOrder::getOrderId);
        torderMapper.selectPage(page, lqw);
        return R.ok(page);
    }

    @GetMapping("/insert")
    public R<Void> insert() {
        for (Long i = 1L; i <= 100L; i++) {
            ShardingOrder torder = new ShardingOrder();
            torder.setUserId(i);
            torder.setTotalMoney(100 + Integer.parseInt(i + ""));
            torderMapper.insert(torder);
        }

        return R.ok("分库分表数据批量插入成功！");

    }

}