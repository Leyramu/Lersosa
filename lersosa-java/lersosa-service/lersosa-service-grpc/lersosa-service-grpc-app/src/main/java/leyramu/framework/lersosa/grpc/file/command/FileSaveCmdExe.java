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

package leyramu.framework.lersosa.grpc.file.command;

import leyramu.framework.lersosa.common.core.exception.ServiceException;
import leyramu.framework.lersosa.resource.api.RemoteFileService;
import leyramu.framework.lersosa.resource.api.domain.RemoteFile;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件保存命令执行器
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/7
 */
@Component
@RequiredArgsConstructor
public class FileSaveCmdExe {

    /**
     * 远程文件服务
     */
    @DubboReference
    private final RemoteFileService remoteFileService;

    /**
     * 保存文件
     *
     * @param pulsarFile 文件对象
     * @return 文件对象
     * @throws ServiceException 服务异常
     * @throws IOException      IO异常
     */
    public RemoteFile save(MultipartFile pulsarFile) throws ServiceException, IOException {
        return remoteFileService.upload(pulsarFile.getName(), pulsarFile.getOriginalFilename(), pulsarFile.getContentType(), pulsarFile.getBytes());
    }
}
