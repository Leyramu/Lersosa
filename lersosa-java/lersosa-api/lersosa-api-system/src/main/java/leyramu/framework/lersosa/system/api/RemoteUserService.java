/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.api;

import leyramu.framework.lersosa.common.core.exception.ServiceException;
import leyramu.framework.lersosa.common.core.exception.user.UserException;
import leyramu.framework.lersosa.system.api.domain.bo.RemoteUserBo;
import leyramu.framework.lersosa.system.api.domain.vo.RemoteUserVo;
import leyramu.framework.lersosa.system.api.model.LoginUser;
import leyramu.framework.lersosa.system.api.model.XcxLoginUser;

import java.util.List;

/**
 * 用户服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface RemoteUserService {

    /**
     * 通过用户名查询用户信息.
     *
     * @param username 用户名
     * @param tenantId 租户id
     * @return 结果
     */
    LoginUser getUserInfo(String username, String tenantId) throws UserException;

    /**
     * 通过用户id查询用户信息.
     *
     * @param userId   用户id
     * @param tenantId 租户id
     * @return 结果
     */
    LoginUser getUserInfo(Long userId, String tenantId) throws UserException;

    /**
     * 通过手机号查询用户信息.
     *
     * @param phonenumber 手机号
     * @param tenantId    租户id
     * @return 结果
     */
    LoginUser getUserInfoByPhonenumber(String phonenumber, String tenantId) throws UserException;

    /**
     * 通过邮箱查询用户信息.
     *
     * @param email    邮箱
     * @param tenantId 租户id
     * @return 结果
     */
    LoginUser getUserInfoByEmail(String email, String tenantId) throws UserException;

    /**
     * 通过openid查询用户信息.
     *
     * @param openid openid
     * @return 结果
     */
    XcxLoginUser getUserInfoByOpenid(String openid) throws UserException;

    /**
     * 注册用户信息.
     *
     * @param remoteUserBo 用户信息
     * @return 结果
     */
    Boolean registerUserInfo(RemoteUserBo remoteUserBo) throws UserException, ServiceException;

    /**
     * 通过userId查询用户账户.
     *
     * @param userId 用户id
     * @return 结果
     */
    String selectUserNameById(Long userId);

    /**
     * 通过用户ID查询用户昵称.
     *
     * @param userId 用户id
     * @return 结果
     */
    @SuppressWarnings("unused")
    String selectNicknameById(Long userId);

    /**
     * 通过用户ID查询用户账户.
     *
     * @param userIds 用户ID 多个用逗号隔开
     * @return 用户名称
     */
    String selectNicknameByIds(String userIds);

    /**
     * 通过用户ID查询用户手机号.
     *
     * @param userId 用户id
     * @return 用户手机号
     */
    @SuppressWarnings("unused")
    String selectPhonenumberById(Long userId);

    /**
     * 通过用户ID查询用户邮箱.
     *
     * @param userId 用户id
     * @return 用户邮箱
     */
    @SuppressWarnings("unused")
    String selectEmailById(Long userId);

    /**
     * 更新用户信息.
     *
     * @param userId 用户ID
     * @param ip     IP地址
     */
    void recordLoginInfo(Long userId, String ip);

    /**
     * 通过用户ID查询用户列表.
     *
     * @param userIds 用户ids
     * @return 用户列表
     */
    List<RemoteUserVo> selectListByIds(List<Long> userIds);

    /**
     * 通过角色ID查询用户ID.
     *
     * @param roleIds 角色ids
     * @return 用户ids
     */
    List<Long> selectUserIdsByRoleIds(List<Long> roleIds);

    String hello(String name);
}
