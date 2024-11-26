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

package leyramu.framework.lersosa.tool;

import leyramu.framework.lersosa.common.core.utils.FileUtils;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static leyramu.framework.lersosa.common.core.utils.SystemUtils.isWindows;

/**
 * 一键修改项目.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/25
 */
public class ModifyProjectBoot {

    /**
     * 模块名称集合
     */
    private static final List<String> MODULES = List.of(
        "lersosa-api",
        "lersosa-common",
        "lersosa-example",
        "lersosa-service",
        "lersosa-gateway"
    );

    /**
     * pom.xml、java、xml文件后缀
     */
    private static final String MODIFY_POM_FILE_SUFFIX = "pom.xml";

    /**
     * java文件后缀
     */
    private static final String MODIFY_JAVA_FILE_SUFFIX = ".java";

    /**
     * xml文件后缀
     */
    private static final String MODIFY_XML_FILE_SUFFIX = ".xml";

    /**
     * 新模块名称
     */
    private static final String NEW_MODULE_NAME = "lersosa";

    /**
     * 新分组ID
     */
    private static final String NEW_GROUP_ID = "leyramu.framework.lersosa";

    /**
     * 新包名路径[Linux]
     */
    private static final String NEW_PACKAGE_PATH_LINUX = "/leyramu/framework/lersosa/";

    /**
     * 新包名路径[Window]
     */
    private static final String NEW_PACKAGE_PATH_WINDOW = "\\\\leyramu\\\\framework\\\\lersosa\\\\";

    /**
     * 新包名名称
     */
    private static final String NEW_PACKAGE_NAME = "leyramu.framework.lersosa";

    /**
     * 新项目名称
     */
    private static final String NEW_PROJECT_NAME = "Lersosa";

    private static int count = 0;

    public static void main(String[] args) {

        // 修改projectName、packageName、groupId、artifactId
        String projectPath = System.getProperty("user.dir");
        FileUtils.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<>() {

            @Override
            @NonNull
            public FileVisitResult visitFile(Path path, @NonNull BasicFileAttributes attrs) throws IOException {
                String filePath = path.toAbsolutePath().toString();
                String newPath = getNewPath(filePath);
                // 创建目录或文件
                createDirOrFile(newPath);
                byte[] buff;
                if (filePath.endsWith(MODIFY_JAVA_FILE_SUFFIX)) {
                    buff = getJavaFileAsByte(filePath);
                } else if (filePath.endsWith(MODIFY_POM_FILE_SUFFIX)) {
                    buff = getPomFileAsByte(filePath);
                } else if (filePath.endsWith(MODIFY_XML_FILE_SUFFIX)) {
                    buff = getXmlFileAsByte(filePath);
                } else {
                    buff = FileUtils.getBytes(Paths.get(filePath));
                }
                FileUtils.write(Paths.get(newPath), buff);
                return FileVisitResult.CONTINUE;
            }

            @Override
            @NonNull
            public FileVisitResult preVisitDirectory(Path path, @NonNull BasicFileAttributes attrs) {
                boolean isExclude = false;
                String dir = path.toString();
                for (String module : MODULES) {
                    if (dir.contains(module)) {
                        isExclude = true;
                        break;
                    }
                }
                if (isExclude || count++ < 1) {
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.SKIP_SUBTREE;
            }

            @Override
            @NonNull
            public FileVisitResult visitFileFailed(Path file, @NonNull IOException exc) {
                return FileVisitResult.CONTINUE;
            }

        });
    }

    private static void createDirOrFile(String path) {
        int index = path.lastIndexOf(File.separator);
        String dir = path.substring(0, index);
        String fileName = path.substring(index + 1);
        FileUtils.create(dir, fileName);
    }

    private static String getNewPath(String path) {
        return path.replaceAll("lersosa-", NEW_MODULE_NAME + "-")
            .replaceAll(getOldPackagePath(), getNewPackagePath())
            .replace("Lersosa", NEW_PROJECT_NAME);
    }

    private static byte[] getJavaFileAsByte(String path) throws IOException {
        String str = FileUtils.getStr(Paths.get(path));
        return str.replaceAll("leyramu.framework.lersosa", NEW_PACKAGE_NAME).getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] getPomFileAsByte(String path) throws IOException {
        String str = FileUtils.getStr(Paths.get(path));
        return str.replaceAll("leyramu.framework.lersosa", NEW_GROUP_ID)
            .replaceAll("lersosa-", NEW_MODULE_NAME + "-")
            .replace("Lersosa", NEW_PROJECT_NAME)
            .getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] getXmlFileAsByte(String path) throws IOException {
        String str = FileUtils.getStr(Paths.get(path));
        return str.replaceAll("leyramu.framework.lersosa", NEW_PACKAGE_NAME).getBytes(StandardCharsets.UTF_8);
    }

    private static String getOldPackagePath() {
        return isWindows() ? "\\\\leyramu\\\\framework\\\\lersosa\\\\" : "/leyramu/framework/lersosa/";
    }

    private static String getNewPackagePath() {
        return isWindows() ? NEW_PACKAGE_PATH_WINDOW : NEW_PACKAGE_PATH_LINUX;
    }
}
