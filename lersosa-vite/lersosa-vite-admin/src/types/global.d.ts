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

import type { PropType as VuePropType, ComponentInternalInstance as ComponentInstance } from 'vue';
import { LanguageEnum } from '@/enums/LanguageEnum';

declare global {
  /** vue Instance */
  declare type ComponentInternalInstance = ComponentInstance;

  /**
   * 界面字段隐藏属性
   */
  declare interface FieldOption {
    key: number;
    label: string;
    visible: boolean;
    children?: Array<FieldOption>;
  }

  /**
   * 弹窗属性
   */
  declare interface DialogOption {
    /**
     * 弹窗标题
     */
    title?: string;
    /**
     * 是否显示
     */
    visible: boolean;
  }

  declare interface UploadOption {
    /** 设置上传的请求头部 */
    headers: { [key: string]: any };

    /** 上传的地址 */
    url: string;
  }

  /**
   * 导入属性
   */
  declare interface ImportOption extends UploadOption {
    /** 是否显示弹出层 */
    open: boolean;
    /** 弹出层标题 */
    title: string;
    /** 是否禁用上传 */
    isUploading: boolean;

    updateSupport: number;

    /** 其他参数 */
    [key: string]: any;
  }
  /**
   * 字典数据  数据配置
   */
  declare interface DictDataOption {
    label: string;
    value: string;
    elTagType?: ElTagType;
    elTagClass?: string;
  }

  declare interface BaseEntity {
    createBy?: any;
    createDept?: any;
    createTime?: string;
    updateBy?: any;
    updateTime?: any;
  }

  /**
   * 分页数据
   * T : 表单数据
   * D : 查询参数
   */
  declare interface PageData<T, D> {
    form: T;
    queryParams: D;
    rules: ElFormRules;
  }
  /**
   * 分页查询参数
   */
  declare interface PageQuery {
    pageNum: number;
    pageSize: number;
  }
  declare interface LayoutSetting {
    /**
     * 是否显示顶部导航
     */
    topNav: boolean;

    /**
     * 是否显示多标签导航
     */
    tagsView: boolean;
    /**
     * 是否固定头部
     */
    fixedHeader: boolean;
    /**
     * 是否显示侧边栏Logo
     */
    sidebarLogo: boolean;
    /**
     * 是否显示动态标题
     */
    dynamicTitle: boolean;
    /**
     * 侧边栏主题 theme-dark | theme-light
     */
    sideTheme: string;
    /**
     * 主题模式
     */
    theme: string;
  }

  declare interface DefaultSettings extends LayoutSetting {
    /**
     * 网页标题
     */
    title: string;

    /**
     * 是否显示系统布局设置
     */
    showSettings: boolean;

    /**
     * 导航栏布局
     */
    layout: string;

    /**
     * 布局大小
     */
    size: 'large' | 'default' | 'small';

    /**
     * 语言
     */
    language: LanguageEnum;

    /**
     * 是否启用动画效果
     */
    animationEnable: boolean;
    /**
     *  是否启用暗黑模式
     *
     * true:暗黑模式
     * false: 明亮模式
     */
    dark: boolean;

    errorLog: string;
  }
}
export {};
