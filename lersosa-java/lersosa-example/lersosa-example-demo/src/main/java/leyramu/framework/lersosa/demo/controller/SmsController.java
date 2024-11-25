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

package leyramu.framework.lersosa.demo.controller;

import leyramu.framework.lersosa.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

/**
 * 短信演示案例.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/sms")
public class SmsController {
    /**
     * 发送短信Aliyun.
     *
     * @param phones     电话号
     * @param templateId 模板ID
     */
    @GetMapping("/sendAliyun")
    public R<Object> sendAliyun(String phones, String templateId) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put("code", "1234");
        SmsBlend smsBlend = SmsFactory.getSmsBlend("config1");
        SmsResponse smsResponse = smsBlend.sendMessage(phones, templateId, map);
        return R.ok(smsResponse);
    }

    /**
     * 发送短信Tencent.
     *
     * @param phones     电话号
     * @param templateId 模板ID
     */
    @GetMapping("/sendTencent")
    public R<Object> sendTencent(String phones, String templateId) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
//        map.put("2", "测试测试");
        map.put("1", "1234");
        SmsBlend smsBlend = SmsFactory.getSmsBlend("config2");
        SmsResponse smsResponse = smsBlend.sendMessage(phones, templateId, map);
        return R.ok(smsResponse);
    }

    /**
     * 添加黑名单.
     *
     * @param phone 手机号
     */
    @GetMapping("/addBlacklist")
    public R<Object> addBlacklist(String phone) {
        SmsBlend smsBlend = SmsFactory.getSmsBlend("config1");
        smsBlend.joinInBlacklist(phone);
        return R.ok();
    }

    /**
     * 移除黑名单.
     *
     * @param phone 手机号
     */
    @GetMapping("/removeBlacklist")
    public R<Object> removeBlacklist(String phone) {
        SmsBlend smsBlend = SmsFactory.getSmsBlend("config1");
        smsBlend.removeFromBlacklist(phone);
        return R.ok();
    }
}
