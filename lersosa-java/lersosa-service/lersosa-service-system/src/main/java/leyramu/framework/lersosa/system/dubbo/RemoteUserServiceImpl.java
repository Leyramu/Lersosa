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

package leyramu.framework.lersosa.system.dubbo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import leyramu.framework.lersosa.common.core.enums.UserStatus;
import leyramu.framework.lersosa.common.core.exception.ServiceException;
import leyramu.framework.lersosa.common.core.exception.user.UserException;
import leyramu.framework.lersosa.common.core.utils.DateUtils;
import leyramu.framework.lersosa.common.core.utils.MapstructUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.mybatis.helper.DataPermissionHelper;
import leyramu.framework.lersosa.common.tenant.helper.TenantHelper;
import leyramu.framework.lersosa.system.api.RemoteUserService;
import leyramu.framework.lersosa.system.api.domain.bo.RemoteUserBo;
import leyramu.framework.lersosa.system.api.domain.vo.RemoteUserVo;
import leyramu.framework.lersosa.system.api.model.LoginUser;
import leyramu.framework.lersosa.system.api.model.RoleDTO;
import leyramu.framework.lersosa.system.api.model.XcxLoginUser;
import leyramu.framework.lersosa.system.domain.SysUser;
import leyramu.framework.lersosa.system.domain.bo.SysUserBo;
import leyramu.framework.lersosa.system.domain.vo.SysDeptVo;
import leyramu.framework.lersosa.system.domain.vo.SysRoleVo;
import leyramu.framework.lersosa.system.domain.vo.SysUserVo;
import leyramu.framework.lersosa.system.mapper.SysUserMapper;
import leyramu.framework.lersosa.system.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 用户服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteUserServiceImpl implements RemoteUserService {

    private final ISysUserService userService;
    private final ISysPermissionService permissionService;
    private final ISysConfigService configService;
    private final ISysRoleService roleService;
    private final ISysDeptService deptService;
    private final SysUserMapper userMapper;

    /**
     * 通过用户名查询用户信息.
     *
     * @param username 用户名
     * @param tenantId 租户id
     * @return 结果
     */
    @Override
    public LoginUser getUserInfo(String username, String tenantId) throws UserException {
        return TenantHelper.dynamic(tenantId, () -> {
            SysUserVo sysUser = userMapper.selectVoOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, username));
            if (ObjectUtil.isNull(sysUser)) {
                throw new UserException("user.not.exists", username);
            }
            if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
                throw new UserException("user.blocked", username);
            }
            // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
            // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
            return buildLoginUser(sysUser);
        });
    }

    /**
     * 通过用户id查询用户信息.
     *
     * @param userId   用户id
     * @param tenantId 租户id
     * @return 结果
     */
    @Override
    public LoginUser getUserInfo(Long userId, String tenantId) throws UserException {
        return TenantHelper.dynamic(tenantId, () -> {
            SysUserVo sysUser = userMapper.selectVoById(userId);
            if (ObjectUtil.isNull(sysUser)) {
                throw new UserException("user.not.exists", "");
            }
            if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
                throw new UserException("user.blocked", sysUser.getUserName());
            }
            // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
            // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
            return buildLoginUser(sysUser);
        });
    }

    /**
     * 通过手机号查询用户信息.
     *
     * @param phonenumber 手机号
     * @param tenantId    租户id
     * @return 结果
     */
    @Override
    public LoginUser getUserInfoByPhonenumber(String phonenumber, String tenantId) throws UserException {
        return TenantHelper.dynamic(tenantId, () -> {
            SysUserVo sysUser = userMapper.selectVoOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhonenumber, phonenumber));
            if (ObjectUtil.isNull(sysUser)) {
                throw new UserException("user.not.exists", phonenumber);
            }
            if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
                throw new UserException("user.blocked", phonenumber);
            }
            // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
            // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
            return buildLoginUser(sysUser);
        });
    }

    /**
     * 通过邮箱查询用户信息.
     *
     * @param email    邮箱
     * @param tenantId 租户id
     * @return 结果
     */
    @Override
    public LoginUser getUserInfoByEmail(String email, String tenantId) throws UserException {
        return TenantHelper.dynamic(tenantId, () -> {
            SysUserVo user = userMapper.selectVoOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, email));
            if (ObjectUtil.isNull(user)) {
                throw new UserException("user.not.exists", email);
            }
            if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
                throw new UserException("user.blocked", email);
            }
            // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
            // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
            return buildLoginUser(user);
        });
    }

    /**
     * 通过openid查询用户信息.
     *
     * @param openid openid
     * @return 结果
     */
    @Override
    public XcxLoginUser getUserInfoByOpenid(String openid) throws UserException {
        // todo 自行实现 userService.selectUserByOpenid(openid);
        SysUser sysUser = new SysUser();
        if (ObjectUtil.isNull(sysUser)) {
            // todo 用户不存在 业务逻辑自行实现
            throw new UserException("user.not.exists", openid);
        }
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            // todo 用户已被停用 业务逻辑自行实现
            throw new UserException("user.blocked", sysUser.getUserName());
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setUserId(sysUser.getUserId());
        loginUser.setUsername(sysUser.getUserName());
        loginUser.setNickname(sysUser.getNickName());
        loginUser.setUserType(sysUser.getUserType());
        loginUser.setOpenid(openid);
        return loginUser;
    }

    /**
     * 注册用户信息.
     *
     * @param remoteUserBo 用户信息
     * @return 结果
     */
    @Override
    public Boolean registerUserInfo(RemoteUserBo remoteUserBo) throws UserException, ServiceException {
        SysUserBo sysUserBo = MapstructUtils.convert(remoteUserBo, SysUserBo.class);
        String username = Objects.requireNonNull(sysUserBo).getUserName();
        boolean exist = TenantHelper.dynamic(remoteUserBo.getTenantId(), () -> {
            if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
                throw new ServiceException("当前系统没有开启注册功能");
            }
            return userMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, sysUserBo.getUserName()));
        });
        if (exist) {
            throw new UserException("user.register.save.error", username);
        }
        return userService.registerUser(sysUserBo, remoteUserBo.getTenantId());
    }

    /**
     * 通过用户ID查询用户账户.
     *
     * @param userId 用户ID
     * @return 用户账户
     */
    @Override
    public String selectUserNameById(Long userId) {
        return userService.selectUserNameById(userId);
    }

    /**
     * 通过用户ID查询用户昵称.
     *
     * @param userId 用户ID
     * @return 用户昵称
     */
    @Override
    public String selectNicknameById(Long userId) {
        return userService.selectNicknameById(userId);
    }

    /**
     * 通过用户ID查询用户账户.
     *
     * @param userIds 用户ID 多个用逗号隔开
     * @return 用户账户
     */
    @Override
    public String selectNicknameByIds(String userIds) {
        return userService.selectNicknameByIds(userIds);
    }

    /**
     * 通过用户ID查询用户手机号.
     *
     * @param userId 用户id
     * @return 用户手机号
     */
    @Override
    public String selectPhonenumberById(Long userId) {
        return userService.selectPhonenumberById(userId);
    }

    /**
     * 通过用户ID查询用户邮箱.
     *
     * @param userId 用户id
     * @return 用户邮箱
     */
    @Override
    public String selectEmailById(Long userId) {
        return userService.selectEmailById(userId);
    }

    /**
     * 构建登录用户.
     */
    private LoginUser buildLoginUser(SysUserVo userVo) {
        LoginUser loginUser = new LoginUser();
        loginUser.setTenantId(userVo.getTenantId());
        loginUser.setUserId(userVo.getUserId());
        loginUser.setDeptId(userVo.getDeptId());
        loginUser.setUsername(userVo.getUserName());
        loginUser.setNickname(userVo.getNickName());
        loginUser.setPassword(userVo.getPassword());
        loginUser.setUserType(userVo.getUserType());
        loginUser.setMenuPermission(permissionService.getMenuPermission(userVo.getUserId()));
        loginUser.setRolePermission(permissionService.getRolePermission(userVo.getUserId()));
        if (ObjectUtil.isNotNull(userVo.getDeptId())) {
            Opt<SysDeptVo> deptOpt = Opt.of(userVo.getDeptId()).map(deptService::selectDeptById);
            loginUser.setDeptName(deptOpt.map(SysDeptVo::getDeptName).orElse(StringUtils.EMPTY));
            loginUser.setDeptCategory(deptOpt.map(SysDeptVo::getDeptCategory).orElse(StringUtils.EMPTY));
        }
        List<SysRoleVo> roles = roleService.selectRolesByUserId(userVo.getUserId());
        loginUser.setRoles(BeanUtil.copyToList(roles, RoleDTO.class));
        return loginUser;
    }

    /**
     * 更新用户信息.
     *
     * @param userId 用户ID
     * @param ip     IP地址
     */
    @Override
    public void recordLoginInfo(Long userId, String ip) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(ip);
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysUser.setUpdateBy(userId);
        DataPermissionHelper.ignore(() -> userMapper.updateById(sysUser));
    }

    /**
     * 通过用户ID查询用户列表.
     *
     * @param userIds 用户ids
     * @return 用户列表
     */
    @Override
    public List<RemoteUserVo> selectListByIds(List<Long> userIds) {
        List<SysUserVo> sysUserVos = userService.selectUserByIds(userIds, null);
        return MapstructUtils.convert(sysUserVos, RemoteUserVo.class);
    }

    /**
     * 通过角色ID查询用户ID.
     *
     * @param roleIds 角色ids
     * @return 用户ids
     */
    @Override
    public List<Long> selectUserIdsByRoleIds(List<Long> roleIds) {
        return userService.selectUserIdsByRoleIds(roleIds);
    }

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
