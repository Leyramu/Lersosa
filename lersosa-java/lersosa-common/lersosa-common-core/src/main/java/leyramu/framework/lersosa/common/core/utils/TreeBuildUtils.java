/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;
import leyramu.framework.lersosa.common.core.utils.reflect.ReflectUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 扩展 hutool TreeUtil 封装系统树构建.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TreeBuildUtils extends TreeUtil {

    /**
     * 根据前端定制差异化字段.
     */
    public static final TreeNodeConfig DEFAULT_CONFIG = TreeNodeConfig.DEFAULT_CONFIG.setNameKey("label");

    /**
     * 构建树形结构.
     *
     * @param <T>        输入节点的类型
     * @param <K>        节点ID的类型
     * @param list       节点列表，其中包含了要构建树形结构的所有节点
     * @param nodeParser 解析器，用于将输入节点转换为树节点
     * @return 构建好的树形结构列表
     */
    public static <T, K> List<Tree<K>> build(List<T> list, NodeParser<T, K> nodeParser) {
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        K k = ReflectUtils.invokeGetter(list.getFirst(), "parentId");
        return TreeUtil.build(list, k, DEFAULT_CONFIG, nodeParser);
    }

    /**
     * 获取节点列表中所有节点的叶子节点.
     *
     * @param <K>   节点ID的类型
     * @param nodes 节点列表
     * @return 包含所有叶子节点的列表
     */
    @SuppressWarnings("unused")
    public static <K> List<Tree<K>> getLeafNodes(List<Tree<K>> nodes) {
        if (CollUtil.isEmpty(nodes)) {
            return CollUtil.newArrayList();
        }
        return nodes.stream()
            .flatMap(TreeBuildUtils::extractLeafNodes)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定节点下的所有叶子节点.
     *
     * @param <K>  节点ID的类型
     * @param node 要查找叶子节点的根节点
     * @return 包含所有叶子节点的列表
     */
    private static <K> Stream<Tree<K>> extractLeafNodes(Tree<K> node) {
        if (!node.hasChild()) {
            return Stream.of(node);
        } else {
            // 递归调用，获取所有子节点的叶子节点
            return node.getChildren().stream()
                .flatMap(TreeBuildUtils::extractLeafNodes);
        }
    }
}
