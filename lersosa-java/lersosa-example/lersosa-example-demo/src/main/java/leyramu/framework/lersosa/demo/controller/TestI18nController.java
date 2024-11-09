/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.core.utils.MessageUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 测试国际化
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RestController
@RequestMapping("/i18n")
public class TestI18nController {

    /**
     * 通过code获取国际化内容
     * code为 messages.properties 中的 key
     * <p>
     * 测试使用 user.register.success
     *
     * @param code 国际化code
     */
    @GetMapping()
    public R<Void> get(String code) {
        return R.ok(MessageUtils.message(code));
    }

    /**
     * Validator 校验国际化
     * 不传值 分别查看异常返回
     * <p>
     * 测试使用 not.null
     */
    @GetMapping("/test1")
    public R<Void> test1(@NotBlank(message = "{not.null}") String str) {
        return R.ok(str);
    }

    /**
     * Bean 校验国际化
     * 不传值 分别查看异常返回
     * <p>
     * 测试使用 not.null
     */
    @GetMapping("/test2")
    public R<TestI18nBo> test2(@Validated TestI18nBo bo) {
        return R.ok(bo);
    }

    @Data
    public static class TestI18nBo {

        @NotBlank(message = "{not.null}")
        private String name;

        @NotNull(message = "{not.null}")
        @Range(min = 0, max = 100, message = "{length.not.valid}")
        private Integer age;
    }
}
