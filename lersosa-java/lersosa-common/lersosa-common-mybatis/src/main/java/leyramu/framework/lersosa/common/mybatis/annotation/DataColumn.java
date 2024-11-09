/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解，用于标记数据权限的占位符关键字和替换值
 * <p>
 * 一个注解只能对应一个模板
 * </p>
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataColumn {

    /**
     * 数据权限模板的占位符关键字，默认为 "deptName"
     *
     * @return 占位符关键字数组
     */
    String[] key() default "deptName";

    /**
     * 数据权限模板的占位符替换值，默认为 "dept_id"
     *
     * @return 占位符替换值数组
     */
    String[] value() default "dept_id";

    /**
     * 权限标识符 用于通过菜单权限标识符来获取数据权限
     * 拥有此标识符的角色 将不会拼接此角色的数据过滤sql
     *
     * @return 权限标识符
     */
    String permission() default "";

}
