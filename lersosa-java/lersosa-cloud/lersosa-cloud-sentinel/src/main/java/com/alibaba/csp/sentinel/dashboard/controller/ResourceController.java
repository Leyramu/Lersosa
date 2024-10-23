/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.domain.ResourceTreeNode;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.domain.vo.ResourceVo;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源控制器
 *
 * @author Carpenter Lee
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@RestController
@RequestMapping(value = "/resource")
@RequiredArgsConstructor
public class ResourceController {

    /**
     * Sentinel API 客户端
     */
    private final SentinelApiClient httpFetcher;

    /**
     * 获取机器的实时统计信息
     *
     * @param ip        要获取的 IP
     * @param port      IP 的端口
     * @param type      类型
     * @param searchKey 搜索键
     * @return 节点统计信息
     */
    @GetMapping("/machineResource.json")
    public Result<List<ResourceVo>> fetchResourceChainListOfMachine(String ip, Integer port, String type,
                                                                    String searchKey) {
        if (StringUtil.isEmpty(ip) || port == null) {
            return Result.ofFail(-1, "invalid param, give ip, port");
        }
        final String root = "root";
        final String dDefault = "default";
        if (StringUtil.isEmpty(type)) {
            type = root;
        }
        if (root.equalsIgnoreCase(type) || dDefault.equalsIgnoreCase(type)) {
            List<NodeVo> nodeVos = httpFetcher.fetchResourceOfMachine(ip, port, type);
            if (nodeVos == null) {
                return Result.ofSuccess(null);
            }
            ResourceTreeNode treeNode = ResourceTreeNode.fromNodeVoList(nodeVos);
            treeNode.searchIgnoreCase(searchKey);
            return Result.ofSuccess(ResourceVo.fromResourceTreeNode(treeNode));
        } else {
            List<NodeVo> nodeVos = httpFetcher.fetchClusterNodeOfMachine(ip, port, true);
            if (nodeVos == null) {
                return Result.ofSuccess(null);
            }
            if (StringUtil.isNotEmpty(searchKey)) {
                nodeVos = nodeVos.stream().filter(node -> node.getResource()
                                .toLowerCase().contains(searchKey.toLowerCase()))
                        .collect(Collectors.toList());
            }
            return Result.ofSuccess(ResourceVo.fromNodeVoList(nodeVos));
        }
    }
}
