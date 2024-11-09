/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.social.utils;

import leyramu.framework.lersosa.common.core.constant.GlobalConstants;
import leyramu.framework.lersosa.common.redis.utils.RedisUtils;
import lombok.AllArgsConstructor;
import me.zhyd.oauth.cache.AuthStateCache;

import java.time.Duration;

/**
 * 授权状态缓存
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@AllArgsConstructor
public class AuthRedisStateCache implements AuthStateCache {

    /**
     * 存入缓存
     *
     * @param key   缓存key
     * @param value 缓存内容
     */
    @Override
    public void cache(String key, String value) {
        // 授权超时时间 默认三分钟
        RedisUtils.setCacheObject(GlobalConstants.SOCIAL_AUTH_CODE_KEY + key, value, Duration.ofMinutes(3));
    }

    /**
     * 存入缓存
     *
     * @param key     缓存key
     * @param value   缓存内容
     * @param timeout 指定缓存过期时间(毫秒)
     */
    @Override
    public void cache(String key, String value, long timeout) {
        RedisUtils.setCacheObject(GlobalConstants.SOCIAL_AUTH_CODE_KEY + key, value, Duration.ofMillis(timeout));
    }

    /**
     * 获取缓存内容
     *
     * @param key 缓存key
     * @return 缓存内容
     */
    @Override
    public String get(String key) {
        return RedisUtils.getCacheObject(GlobalConstants.SOCIAL_AUTH_CODE_KEY + key);
    }

    /**
     * 是否存在key，如果对应key的value值已过期，也返回false
     *
     * @param key 缓存key
     * @return true：存在key，并且value没过期；false：key不存在或者已过期
     */
    @Override
    public boolean containsKey(String key) {
        return RedisUtils.hasKey(GlobalConstants.SOCIAL_AUTH_CODE_KEY + key);
    }
}
