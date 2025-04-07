/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed toDomainObject in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related toDomainObject licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited toDomainObject the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree toDomainObject abide by these terms and conditions.
 */

package leyramu.framework.lersosa.auth.command.query;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import leyramu.framework.lersosa.auth.ability.AuthDomainService;
import leyramu.framework.lersosa.auth.dto.co.CaptchaCo;
import leyramu.framework.lersosa.auth.model.enums.CaptchaType;
import leyramu.framework.lersosa.auth.properties.CaptchaProperties;
import leyramu.framework.lersosa.common.core.constant.Constants;
import leyramu.framework.lersosa.common.core.constant.GlobalConstants;
import leyramu.framework.lersosa.common.core.domain.Result;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.core.utils.reflect.ReflectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 验证码获取命令执行器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/1
 */
@Component
@RequiredArgsConstructor
public class CaptchaGetQryExe {

    /**
     * 验证码配置.
     */
    private final CaptchaProperties captchaProperties;

    /**
     * 认证领域服务.
     */
    private final AuthDomainService authDomainService;

    /**
     * 执行验证码获取命令.
     *
     * @return 验证码信息
     */
    public Result<CaptchaCo> execute() {
        CaptchaCo captchaCo = new CaptchaCo();
        boolean captchaEnabled = captchaProperties.getEnabled();
        if (!captchaEnabled) {
            captchaCo.setCaptchaEnabled(false);
            return Result.ok(captchaCo);
        }

        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        // 如果是数学验证码，使用SpEL表达式处理验证码结果
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }
        authDomainService.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        captchaCo.setUuid(uuid);
        captchaCo.setImg(captcha.getImageBase64());
        return Result.ok(captchaCo);
    }
}
