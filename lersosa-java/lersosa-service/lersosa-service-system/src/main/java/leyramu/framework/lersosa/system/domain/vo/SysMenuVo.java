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

package leyramu.framework.lersosa.system.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import leyramu.framework.lersosa.system.domain.SysMenu;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单权限视图对象 sys_menu.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@AutoMapper(target = SysMenu.class)
public class SysMenuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID.
     */
    private Long menuId;

    /**
     * 菜单名称.
     */
    private String menuName;

    /**
     * 父菜单ID.
     */
    private Long parentId;

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
     * 菜单类型（M目录 C菜单 F按钮）.
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
     * 权限标识.
     */
    private String perms;

    /**
     * 菜单图标.
     */
    private String icon;

    /**
     * 创建部门.
     */
    private Long createDept;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 子菜单.
     */
    private List<SysMenuVo> children = new ArrayList<>();
}
