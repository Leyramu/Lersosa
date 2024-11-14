/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Demo 控制器.
 *
 * @author Sentinel
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@Slf4j
@Controller
@SuppressWarnings("all")
@RequestMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoController {

    /**
     * 模拟首页.
     *
     * @return 首页
     */
    @RequestMapping("/greeting")
    public String greeting() {
        return "index";
    }

    /**
     * 模拟调用链.
     *
     * @return 模拟结果
     */
    @RequestMapping("/link")
    @ResponseBody
    public String link() throws BlockException {
        Entry entry = SphU.entry("head1", EntryType.IN);
        Entry entry1 = SphU.entry("head2", EntryType.IN);
        Entry entry2 = SphU.entry("head3", EntryType.IN);
        Entry entry3 = SphU.entry("head4", EntryType.IN);

        entry3.exit();
        entry2.exit();
        entry1.exit();
        entry.exit();
        return "successfully create a call link";
    }

    /**
     * 模拟循环任务.
     *
     * @param name  任务名称
     * @param time  任务执行时间
     * @return 模拟结果
     */
    @RequestMapping("/loop")
    @ResponseBody
    public String loop(String name, int time) {
        for (int i = 0; i < 10; i++) {
            Thread timer = new Thread(new RunTask(name, time, false));
            timer.setName("false");
            timer.start();
        }
        return "successfully create a loop thread";
    }

    /**
     * 模拟慢任务.
     *
     * @param name  任务名称
     * @param time  任务执行时间
     * @return 模拟结果
     */
    @RequestMapping("/slow")
    @ResponseBody
    public String slow(String name, int time) {
        for (int i = 0; i < 10; i++) {
            Thread timer = new Thread(new RunTask(name, time, true));
            timer.setName("false");
            timer.start();
        }
        return "successfully create a loop thread";
    }

    /**
     * 模拟任务类，实现Runnable接口，定义任务的具体执行逻辑.
     */
    static class RunTask implements Runnable {
        int time;
        boolean stop = false;
        String name;
        boolean slow;

        /**
         * 构造函数，初始化任务参数.
         *
         * @param name 任务名称
         * @param time 任务执行时间
         * @param slow 是否慢任务
         */
        public RunTask(String name, int time, boolean slow) {
            super();
            this.time = time;
            this.name = name;
            this.slow = slow;
        }

        /**
         * 实现Runnable接口的run方法，定义任务的具体执行逻辑.
         */
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            ContextUtil.enter(String.valueOf(startTime));
            while (!stop) {

                long now = System.currentTimeMillis();
                if (now - startTime > time * 1000L) {
                    stop = true;
                }
                Entry e1 = null;
                try {
                    e1 = SphU.entry(name);

                    if (slow) {
                        TimeUnit.MILLISECONDS.sleep(3000);
                    }

                } catch (Exception ignored) {
                } finally {
                    if (e1 != null) {
                        e1.exit();
                    }
                }
                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(200));
                } catch (InterruptedException e) {
                    log.error("sleep error", e);
                }
            }
            ContextUtil.exit();
        }
    }
}
