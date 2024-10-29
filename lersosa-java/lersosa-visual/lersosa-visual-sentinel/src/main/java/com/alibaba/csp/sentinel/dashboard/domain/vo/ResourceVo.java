/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.vo;

import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.dashboard.domain.ResourceTreeNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源 VO
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
@NoArgsConstructor
public class ResourceVo {

    /**
     * 父节点ID
     */
    private String parentTtId;

    /**
     * 资源ID
     */
    private String ttId;

    /**
     * 资源名称
     */
    private String resource;

    /**
     * 线程数
     */
    private Integer threadNum;

    /**
     * 通过 QPS
     */
    private Long passQps;

    /**
     * 阻塞 QPS
     */
    private Long blockQps;

    /**
     * 总 QPS
     */
    private Long totalQps;

    /**
     * 平均 RT
     */
    private Long averageRt;

    /**
     * 通过的请求 QPS
     */
    private Long passRequestQps;

    /**
     * 异常 QPS
     */
    private Long exceptionQps;

    /**
     * 1分钟通过的 QPS
     */
    private Long oneMinutePass;

    /**
     * 1分钟阻塞的 QPS
     */
    private Long oneMinuteBlock;

    /**
     * 1分钟异常的 QPS
     */
    private Long oneMinuteException;

    /**
     * 1分钟总 QPS
     */
    private Long oneMinuteTotal;

    /**
     * 是否可见
     */
    private boolean visible = true;

    /**
     * 转换为 NodeVo 列表
     *
     * @param nodeVos NodeVo 列表
     * @return 资源VO列表
     */
    public static List<ResourceVo> fromNodeVoList(List<NodeVo> nodeVos) {
        if (nodeVos == null) {
            return null;
        }
        List<ResourceVo> list = new ArrayList<>();
        for (NodeVo nodeVo : nodeVos) {
            ResourceVo vo = new ResourceVo();
            vo.parentTtId = nodeVo.getParentId();
            vo.ttId = nodeVo.getId();
            vo.resource = nodeVo.getResource();
            vo.threadNum = nodeVo.getThreadNum();
            vo.passQps = nodeVo.getPassQps();
            vo.blockQps = nodeVo.getBlockQps();
            vo.totalQps = nodeVo.getTotalQps();
            vo.averageRt = nodeVo.getAverageRt();
            vo.exceptionQps = nodeVo.getExceptionQps();
            vo.oneMinutePass = nodeVo.getOneMinutePass();
            vo.oneMinuteBlock = nodeVo.getOneMinuteBlock();
            vo.oneMinuteException = nodeVo.getOneMinuteException();
            vo.oneMinuteTotal = nodeVo.getOneMinuteTotal();
            list.add(vo);
        }
        return list;
    }

    /**
     * 树形结构转换
     *
     * @param root 资源树节点
     * @return 资源VO列表
     */
    public static List<ResourceVo> fromResourceTreeNode(ResourceTreeNode root) {
        if (root == null) {
            return null;
        }
        List<ResourceVo> list = new ArrayList<>();
        visit(root, list, false, true);

        return list;
    }

    /**
     * 递归遍历
     *
     * @param node          资源树节点
     * @param list          资源VO列表
     * @param parentVisible 父节点是否可见
     * @param isRoot        是否为根节点
     */
    private static void visit(ResourceTreeNode node, List<ResourceVo> list, boolean parentVisible, boolean isRoot) {
        boolean visible = !isRoot && (node.isVisible() || parentVisible);
        if (visible) {
            ResourceVo vo = new ResourceVo();
            vo.parentTtId = node.getParentId();
            vo.ttId = node.getId();
            vo.resource = node.getResource();
            vo.threadNum = node.getThreadNum();
            vo.passQps = node.getPassQps();
            vo.blockQps = node.getBlockQps();
            vo.totalQps = node.getTotalQps();
            vo.averageRt = node.getAverageRt();
            vo.exceptionQps = node.getExceptionQps();
            vo.oneMinutePass = node.getOneMinutePass();
            vo.oneMinuteBlock = node.getOneMinuteBlock();
            vo.oneMinuteException = node.getOneMinuteException();
            vo.oneMinuteTotal = node.getOneMinuteTotal();
            vo.visible = node.isVisible();
            list.add(vo);
        }
        for (ResourceTreeNode c : node.getChildren()) {
            visit(c, list, visible, false);
        }
    }
}
