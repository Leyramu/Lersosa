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

package leyramu.framework.lersosa.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import leyramu.framework.lersosa.common.core.constant.Constants;
import leyramu.framework.lersosa.common.core.constant.UserConstants;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    /**
     * 菜单ID.
     */
    @TableId(value = "menu_id")
    private Long menuId;

    /**
     * 父菜单ID.
     */
    private Long parentId;

    /**
     * 菜单名称.
     */
    private String menuName;

    /**
     * 显示顺序.
     */
    private Integer orderNum;

    /**
     * 路由地址.
     */
    private String path;

    /**
     * 组件路径.
     */
    private String component;

    /**
     * 路由参数.
     */
    private String queryParam;

    /**
     * 是否为外链（0是 1否）.
     */
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）.
     */
    private String isCache;

    /**
     * 类型（M目录 C菜单 F按钮）.
     */
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）.
     */
    private String visible;

    /**
     * 菜单状态（0正常 1停用）.
     */
    private String status;

    /**
     * 权限字符串.
     */
    private String perms;

    /**
     * 菜单图标.
     */
    private String icon;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 父菜单名称.
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 子菜单.
     */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

    /**
     * 内链域名特殊字符替换.
     */
    public static String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
            new String[]{"", "", "", "/", "/"});
    }

    /**
     * 获取路由名称.
     */
    public String getRouteName() {
        String routerName = StringUtils.capitalize(path);
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame()) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址.
     */
    public String getRouterPath() {
        String routerPath = this.path;
        // 内链打开外网方式
        if (getParentId() != 0L && isInnerLink()) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0L == getParentId() && UserConstants.TYPE_DIR.equals(getMenuType())
            && UserConstants.NO_FRAME.equals(getIsFrame())) {
            routerPath = "/" + this.path;
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame()) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息.
     */
    public String getComponentInfo() {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(this.component) && !isMenuFrame()) {
            component = this.component;
        } else if (StringUtils.isEmpty(this.component) && getParentId() != 0L && isInnerLink()) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(this.component) && isParentView()) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转.
     */
    public boolean isMenuFrame() {
        return getParentId() == 0L && UserConstants.TYPE_MENU.equals(menuType) && isFrame.equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件.
     */
    public boolean isInnerLink() {
        return isFrame.equals(UserConstants.NO_FRAME) && StringUtils.ishttp(path);
    }

    /**
     * 是否为parent_view组件.
     */
    public boolean isParentView() {
        return getParentId() != 0L && UserConstants.TYPE_DIR.equals(menuType);
    }
}
