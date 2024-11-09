/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import leyramu.framework.lersosa.demo.domain.ShardingOrder;
import leyramu.framework.lersosa.demo.mapper.ShardingOrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TOrderTest {

    @Autowired
    ShardingOrderMapper torderMapper;


    @Test
    void find() {
        //Order order = orderMapper.selectById(1640990702722723841L);
    }

    @Test
    void page() {
        Page<ShardingOrder> page = new Page<>();
        page.setCurrent(3L);
        QueryWrapper<ShardingOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("order_id");
        torderMapper.selectPage(page, queryWrapper);
        System.out.println(page.getTotal());
        for (ShardingOrder order : page.getRecords()) {
            System.out.print(order.getTotalMoney() + " ");
        }
    }

    @Test
    void insert() {
        for (Long i = 1L; i <= 100L; i++) {
            ShardingOrder torder = new ShardingOrder();
            torder.setUserId(i);
            torder.setTotalMoney(100 + Integer.parseInt(i + ""));
            torderMapper.insert(torder);
        }

    }


}
