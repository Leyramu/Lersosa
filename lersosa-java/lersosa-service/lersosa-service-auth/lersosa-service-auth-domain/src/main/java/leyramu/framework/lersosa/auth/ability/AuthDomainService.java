/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
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

package leyramu.framework.lersosa.auth.ability;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.lock.annotation.Lock4j;
import leyramu.framework.lersosa.auth.gateway.*;
import leyramu.framework.lersosa.auth.model.AuthA;
import leyramu.framework.lersosa.auth.model.LoginBody;
import leyramu.framework.lersosa.auth.model.SocialV;
import leyramu.framework.lersosa.auth.model.TenantE;
import leyramu.framework.lersosa.auth.model.form.RegisterBody;
import leyramu.framework.lersosa.auth.model.form.SocialLoginBody;
import leyramu.framework.lersosa.auth.properties.CaptchaProperties;
import leyramu.framework.lersosa.auth.properties.UserPasswordProperties;
import leyramu.framework.lersosa.common.core.constant.*;
import leyramu.framework.lersosa.common.core.enums.LoginType;
import leyramu.framework.lersosa.common.core.enums.UserType;
import leyramu.framework.lersosa.common.core.exception.ServiceException;
import leyramu.framework.lersosa.common.core.exception.user.CaptchaException;
import leyramu.framework.lersosa.common.core.exception.user.CaptchaExpireException;
import leyramu.framework.lersosa.common.core.exception.user.UserException;
import leyramu.framework.lersosa.common.core.utils.MessageUtils;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.core.utils.ValidatorUtils;
import leyramu.framework.lersosa.common.json.utils.JsonUtils;
import leyramu.framework.lersosa.common.log.event.LogininforEvent;
import leyramu.framework.lersosa.common.redis.utils.RedisUtils;
import leyramu.framework.lersosa.common.satoken.utils.LoginHelper;
import leyramu.framework.lersosa.common.social.config.properties.SocialProperties;
import leyramu.framework.lersosa.common.social.utils.SocialUtils;
import leyramu.framework.lersosa.common.tenant.exception.TenantException;
import leyramu.framework.lersosa.common.tenant.helper.TenantHelper;
import leyramu.framework.lersosa.system.api.domain.bo.RemoteSocialBo;
import leyramu.framework.lersosa.system.api.domain.bo.RemoteUserBo;
import leyramu.framework.lersosa.system.api.model.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 认证领域服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthDomainService {

    /**
     * 用户密码配置.
     */
    private final UserPasswordProperties userPasswordProperties;

    /**
     * 验证码配置.
     */
    private final CaptchaProperties captchaProperties;

    /**
     * 社交登录配置.
     */
    private final SocialProperties socialProperties;

    /**
     * 验证码网关.
     */
    private final CaptchaGateway captchaGateway;

    /**
     * 租户网关.
     */
    private final TenantGateway tenantGateway;

    /**
     * 客户端网关.
     */
    private final ClientGateway clientGateway;

    /**
     * 消息网关.
     */
    private final MessageGateway messageGateway;

    /**
     * 配置网关.
     */
    private final ConfigGateway configGateway;

    /**
     * 用户网关.
     */
    private final UserGateway userGateway;

    /**
     * 社交网关.
     */
    private final SocialGateway socialGateway;

    /**
     * 认证.
     *
     * @param authA 认证聚合
     */
    public void auth(AuthA authA) {
        LoginBody loginBody = JsonUtils.parseObject(authA.getBody(), LoginBody.class);

        ValidatorUtils.validate(loginBody);
        String clientId = Objects.requireNonNull(loginBody).getClientId();

        authA.updateLoginObject(loginBody, clientGateway.queryByClientId(clientId));
    }

    /**
     * 登录校验.
     */
    public void checkLogin(LoginType loginType, String tenantId, String username, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;
        Integer maxRetryCount = userPasswordProperties.getMaxRetryCount();
        Integer lockTime = userPasswordProperties.getLockTime();

        // 获取用户登录错误次数，默认为0 (可自定义限制策略 例如: key + username + ip)
        int errorNumber = ObjectUtil.defaultIfNull(RedisUtils.getCacheObject(errorKey), 0);
        // 锁定时间内登录 则踢出
        if (errorNumber >= maxRetryCount) {
            recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }

        if (supplier.get()) {
            // 错误次数递增
            errorNumber++;
            RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
            // 达到规定错误次数 则锁定登录
            if (errorNumber >= maxRetryCount) {
                recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数
                recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }

    /**
     * 注册.
     *
     * @param authA 认证聚合
     */
    public Boolean register(AuthA authA) {
        RegisterBody registerBody = JsonUtils.parseObject(authA.getBody(), RegisterBody.class);
        ValidatorUtils.validate(registerBody);

        if (!configGateway.selectRegisterEnabled(Objects.requireNonNull(registerBody).getTenantId())) {
            return false;
        }

        // 用户注册
        String tenantId = registerBody.getTenantId();
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();

        // 校验用户类型是否存在
        String userType = UserType.getUserType(registerBody.getUserType()).getUserType();

        boolean captchaEnabled = captchaProperties.getEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(tenantId, username, registerBody.getCode(), registerBody.getUuid());
        }

        // 注册用户信息
        RemoteUserBo remoteUserBo = new RemoteUserBo();
        remoteUserBo.setTenantId(tenantId);
        remoteUserBo.setUserName(username);
        remoteUserBo.setNickName(username);
        remoteUserBo.setPassword(BCrypt.hashpw(password));
        remoteUserBo.setUserType(userType);

        boolean regFlag = userGateway.registerUserInfo(remoteUserBo);
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
        recordLogininfor(tenantId, username, Constants.REGISTER, MessageUtils.message("user.register.success"));

        return true;
    }

    /**
     * 退出登录.
     */
    public void logout() {
        try {
            LoginUser loginUser = LoginHelper.getLoginUser();
            if (ObjectUtil.isNull(loginUser)) {
                return;
            }
            if (TenantHelper.isEnable() && LoginHelper.isSuperAdmin()) {
                // 超级管理员 登出清除动态租户
                TenantHelper.clearDynamic();
            }
            recordLogininfor(loginUser.getTenantId(), loginUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
        } catch (NotLoginException ignored) {
        } finally {
            try {
                StpUtil.logout();
            } catch (NotLoginException ignored) {
            }
        }
    }

    /**
     * 校验租户.
     *
     * @param tenantId 租户ID
     */
    public void checkTenant(String tenantId) {
        if (!TenantHelper.isEnable()) {
            return;
        }
        if (TenantConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
            return;
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new TenantException("tenant.number.not.blank");
        }
        TenantE tenant = tenantGateway.queryByTenantId(tenantId);
        if (ObjectUtil.isNull(tenant)) {
            log.info("登录租户：{} 不存在.", tenantId);
            throw new TenantException("tenant.not.exists");
        } else if (SystemConstants.DISABLE.equals(tenant.getStatus())) {
            log.info("登录租户：{} 已被停用.", tenantId);
            throw new TenantException("tenant.blocked");
        } else if (ObjectUtil.isNotNull(tenant.getExpireTime())
            && new Date().after(tenant.getExpireTime())) {
            log.info("登录租户：{} 已超过有效期.", tenantId);
            throw new TenantException("tenant.expired");
        }
    }

    /**
     * 社交登录.
     *
     * @param authA 认证聚合
     */
    public String socialCallback(AuthA authA) {
        SocialLoginBody socialLoginBody = JsonUtils.parseObject(authA.getBody(), SocialLoginBody.class);
        ValidatorUtils.validate(socialLoginBody);

        // 获取第三方登录信息
        AuthResponse<AuthUser> response = SocialUtils.loginAuth(
            Objects.requireNonNull(socialLoginBody).getSource(),
            socialLoginBody.getSocialCode(),
            socialLoginBody.getSocialState(),
            socialProperties
        );
        AuthUser authUserData = response.getData();
        // 判断授权响应是否成功
        if (!response.ok()) {
            return response.getMsg();
        }
        socialRegister(authUserData);
        return "";
    }

    /**
     * 解绑社交.
     *
     * @param socialId 社交ID
     */
    public Boolean unlockSocial(Long socialId) {
        return socialGateway.deleteWithValidById(socialId);
    }

    /**
     * 发送消息.
     *
     * @param userId 用户ID
     */
    public void schedule(Long userId) {
        messageGateway.schedule(userId);
    }

    /**
     * 设置缓存对象.
     *
     * @param key      缓存键
     * @param code     验证码
     * @param duration 缓存时间
     */
    public void setCacheObject(String key, String code, Duration duration) {
        captchaGateway.setCacheObject(key, code, duration);
    }

    /**
     * 查询租户列表.
     *
     * @return 租户列表
     */
    public List<TenantE> queryList() {
        return tenantGateway.queryList();
    }

    /**
     * 记录登录信息.
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    public void recordLogininfor(String tenantId, String username, String status, String message) {
        // 封装对象
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setTenantId(tenantId);
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        SpringUtils.context().publishEvent(logininforEvent);
    }

    /**
     * 校验验证码.
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    private void validateCaptcha(String tenantId, String username, String code, String uuid) {
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.blankToDefault(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLogininfor(tenantId, username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLogininfor(tenantId, username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

    /**
     * 绑定第三方用户.
     *
     * @param authUserData 授权响应实体
     */
    @Lock4j
    public void socialRegister(AuthUser authUserData) {
        String authId = authUserData.getSource() + authUserData.getUuid();
        // 第三方用户信息
        RemoteSocialBo bo = BeanUtil.toBean(authUserData, RemoteSocialBo.class);
        BeanUtil.copyProperties(authUserData.getToken(), bo);
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        bo.setAuthId(authId);
        bo.setOpenId(authUserData.getUuid());
        bo.setUserName(authUserData.getUsername());
        bo.setNickName(authUserData.getNickname());
        List<SocialV> checkList = socialGateway.selectByAuthId(authId);
        if (CollUtil.isNotEmpty(checkList)) {
            throw new ServiceException("此三方账号已经被绑定!");
        }
        // 查询是否已经绑定用户
        RemoteSocialBo params = new RemoteSocialBo();
        params.setUserId(userId);
        params.setSource(bo.getSource());
        List<SocialV> list = socialGateway.queryList(params);
        if (CollUtil.isEmpty(list)) {
            // 没有绑定用户, 新增用户信息
            socialGateway.insertByBo(bo);
        } else {
            // 更新用户信息
            bo.setId(list.getFirst().id());
            socialGateway.updateByBo(bo);
            // 如果要绑定的平台账号已经被绑定过了 是否抛异常自行决断
            // throw new ServiceException("此平台账号已经被绑定!");
        }
    }
}
