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

package leyramu.framework.lersosa.resource.web;

import cn.hutool.core.util.RandomUtil;
import jakarta.validation.constraints.NotBlank;
import leyramu.framework.lersosa.common.core.constant.Constants;
import leyramu.framework.lersosa.common.core.constant.GlobalConstants;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.ratelimiter.annotation.RateLimiter;
import leyramu.framework.lersosa.common.redis.utils.RedisUtils;
import leyramu.framework.lersosa.common.web.core.BaseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.LinkedHashMap;

/**
 * 短信功能.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/sms")
public class SysSmsController extends BaseController {

    /**
     * 短信验证码.
     *
     * @param phonenumber 用户手机号
     */
    @RateLimiter(key = "#phonenumber", count = 1)
    @GetMapping("/code")
    public R<Void> smsCaptcha(@NotBlank(message = "{user.phonenumber.not.blank}") String phonenumber) {
        String key = GlobalConstants.CAPTCHA_CODE_KEY + phonenumber;
        String code = RandomUtil.randomNumbers(4);
        RedisUtils.setCacheObject(key, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        // 验证码模板id 自行处理 (查数据库或写死均可)
        String templateId = "";
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put("code", code);
        SmsBlend smsBlend = SmsFactory.getSmsBlend("tx1");
        SmsResponse smsResponse = smsBlend.sendMessage(phonenumber, templateId, map);
        if (!smsResponse.isSuccess()) {
            log.error("验证码短信发送异常 => {}", smsResponse);
            return R.fail(smsResponse.getData().toString());
        }
        return R.ok();
    }
}
