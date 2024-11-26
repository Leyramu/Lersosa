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

package leyramu.framework.lersosa.system.web.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.core.service.DictService;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.resource.api.RemoteMessageService;
import leyramu.framework.lersosa.system.api.ISysNoticeService;
import leyramu.framework.lersosa.system.domain.bo.SysNoticeBo;
import leyramu.framework.lersosa.system.domain.vo.SysNoticeVo;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 公告 信息操作处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController {

    private final ISysNoticeService noticeService;
    private final DictService dictService;

    @DubboReference
    private final RemoteMessageService remoteMessageService;

    /**
     * 获取通知公告列表.
     */
    @SaCheckPermission("system:notice:list")
    @GetMapping("/list")
    public TableDataInfo<SysNoticeVo> list(SysNoticeBo notice, PageQuery pageQuery) {
        return noticeService.selectPageNoticeList(notice, pageQuery);
    }

    /**
     * 根据通知公告编号获取详细信息.
     *
     * @param noticeId 公告ID
     */
    @SaCheckPermission("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public R<SysNoticeVo> getInfo(@PathVariable Long noticeId) {
        return R.ok(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告.
     */
    @SaCheckPermission("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysNoticeBo notice) {
        int rows = noticeService.insertNotice(notice);
        if (rows <= 0) {
            return R.fail();
        }
        String type = dictService.getDictLabel("sys_notice_type", notice.getNoticeType());
        remoteMessageService.publishAll("[" + type + "] " + notice.getNoticeTitle());
        return R.ok();
    }

    /**
     * 修改通知公告.
     */
    @SaCheckPermission("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysNoticeBo notice) {
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告.
     *
     * @param noticeIds 公告ID串
     */
    @SaCheckPermission("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public R<Void> remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
