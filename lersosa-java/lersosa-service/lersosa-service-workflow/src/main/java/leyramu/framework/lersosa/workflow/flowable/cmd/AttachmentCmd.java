/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.flowable.cmd;

import cn.hutool.core.collection.CollUtil;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.satoken.utils.LoginHelper;
import leyramu.framework.lersosa.resource.api.RemoteFileService;
import leyramu.framework.lersosa.resource.api.domain.RemoteFile;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.AttachmentEntity;
import org.flowable.engine.impl.persistence.entity.AttachmentEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;

import java.util.Date;
import java.util.List;

/**
 * 附件上传
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class AttachmentCmd implements Command<Boolean> {

    private final String fileId;

    private final String taskId;

    private final String processInstanceId;

    private final RemoteFileService remoteFileService;

    public AttachmentCmd(String fileId, String taskId, String processInstanceId,
                         RemoteFileService remoteFileService) {
        this.fileId = fileId;
        this.taskId = taskId;
        this.processInstanceId = processInstanceId;
        this.remoteFileService = remoteFileService;
    }

    @Override
    public Boolean execute(CommandContext commandContext) {
        try {
            if (StringUtils.isNotBlank(fileId)) {
                List<RemoteFile> ossList = remoteFileService.selectByIds(fileId);
                if (CollUtil.isNotEmpty(ossList)) {
                    for (RemoteFile oss : ossList) {
                        AttachmentEntityManager attachmentEntityManager = CommandContextUtil.getAttachmentEntityManager();
                        AttachmentEntity attachmentEntity = attachmentEntityManager.create();
                        attachmentEntity.setRevision(1);
                        attachmentEntity.setUserId(LoginHelper.getUserId().toString());
                        attachmentEntity.setName(oss.getOriginalName());
                        attachmentEntity.setDescription(oss.getOriginalName());
                        attachmentEntity.setType(oss.getFileSuffix());
                        attachmentEntity.setTaskId(taskId);
                        attachmentEntity.setProcessInstanceId(processInstanceId);
                        attachmentEntity.setContentId(oss.getOssId().toString());
                        attachmentEntity.setTime(new Date());
                        attachmentEntityManager.insert(attachmentEntity);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
