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

package leyramu.framework.lersosa.grpc.file.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import leyramu.framework.lersosa.common.grpc.lib.file.FileGrpc;
import leyramu.framework.lersosa.common.grpc.lib.file.FileOuterClass;
import leyramu.framework.lersosa.grpc.api.FileService;
import leyramu.framework.lersosa.grpc.file.command.FileSaveCmdExe;
import leyramu.framework.lersosa.resource.api.domain.RemoteFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 文件服务业务层 接口
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/7
 */
@Slf4j
@GrpcService
@DubboService
@RequiredArgsConstructor
public class FileServiceImpl extends FileGrpc.FileImplBase implements FileService {

    /**
     * 文件保存命令执行器
     */
    private final FileSaveCmdExe fileSaveCmdExe;

    /**
     * Grpc 上传文件
     *
     * @param responseObserver 响应对象
     * @return 文件对象
     */
    @Override
    public StreamObserver<FileOuterClass.FileChunk> uploadFile(StreamObserver<FileOuterClass.FileReply> responseObserver) {
        return new StreamObserver<>() {

            /**
             * 缓存数据
             */
            private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            /**
             * 文件名
             */
            private String fileName;

            /**
             * 文件类型
             */
            private String mimeType;

            /**
             * 接收数据
             *
             * @param chunk 文件块
             */
            @Override
            public void onNext(FileOuterClass.FileChunk chunk) {
                try {
                    if (fileName == null) {
                        fileName = chunk.getFileName();
                        mimeType = chunk.getMimeType();
                    }
                    chunk.getData().writeTo(buffer);
                } catch (IOException e) {
                    responseObserver.onError(e);
                }
            }

            /**
             * 完成处理
             */
            @Override
            public void onCompleted() {
                try {
                    MultipartFile multipartFile = new MockMultipartFile(
                        fileName,
                        fileName,
                        mimeType,
                        buffer.toByteArray()
                    );

                    RemoteFile remoteFile = fileSaveCmdExe.save(multipartFile);

                    responseObserver.onNext(FileOuterClass.FileReply.newBuilder()
                        .setCode(200)
                        .setMessage("文件已保存。路径：" + remoteFile.getUrl())
                        .build());
                    responseObserver.onCompleted();
                } catch (Exception e) {
                    responseObserver.onError(Status.INTERNAL
                        .withDescription(e.getMessage())
                        .asRuntimeException());
                }
            }

            /**
             * 错误处理
             * @param t Throwable
             */
            @Override
            public void onError(Throwable t) {
                log.error("文件上传失败", t);
            }
        };
    }
}
