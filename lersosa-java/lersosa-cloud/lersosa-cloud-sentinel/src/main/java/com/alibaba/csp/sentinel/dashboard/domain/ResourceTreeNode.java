/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
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
 */

package com.alibaba.csp.sentinel.dashboard.domain;

import com.alibaba.csp.sentinel.command.vo.NodeVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点对象
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
public class ResourceTreeNode {

    /**
     * 节点 id
     */
    private String id;

    /**
     * 父节点 id
     */
    private String parentId;

    /**
     * 资源名
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
     * 平均响应时间
     */
    private Long averageRt;

    /**
     * 成功 QPS
     */
    private Long successQps;

    /**
     * 异常 QPS
     */
    private Long exceptionQps;

    /**
     * 1分钟统计数据 - 通过
     */
    private Long oneMinutePass;

    /**
     * 1分钟统计数据 - 阻塞
     */
    private Long oneMinuteBlock;

    /**
     * 1分钟统计数据 - 异常
     */
    private Long oneMinuteException;

    /**
     * 1分钟统计数据 - 总
     */
    private Long oneMinuteTotal;

    /**
     * 节点是否可见
     */
    private boolean visible = true;

    /**
     * 子节点列表
     */
    private List<ResourceTreeNode> children = new ArrayList<>();

    /**
     * 从 NodeVo 列表转换为 ResourceTreeNode 对象
     *
     * @param nodeVos NodeVo 列表
     * @return ResourceTreeNode 对象
     */
    public static ResourceTreeNode fromNodeVoList(List<NodeVo> nodeVos) {
        if (nodeVos == null || nodeVos.isEmpty()) {
            return null;
        }
        ResourceTreeNode root = null;
        Map<String, ResourceTreeNode> map = new HashMap<>();
        for (NodeVo vo : nodeVos) {
            ResourceTreeNode node = fromNodeVo(vo);
            map.put(node.id, node);
            if (node.parentId == null || node.parentId.isEmpty()) {
                root = node;
            } else if (map.containsKey(node.parentId)) {
                map.get(node.parentId).children.add(node);
            }
        }
        return root;
    }

    /**
     * 从 NodeVo 转换为 ResourceTreeNode 对象
     *
     * @param vo NodeVo 对象
     * @return ResourceTreeNode 对象
     */
    public static ResourceTreeNode fromNodeVo(NodeVo vo) {
        ResourceTreeNode node = new ResourceTreeNode();
        node.id = vo.getId();
        node.parentId = vo.getParentId();
        node.resource = vo.getResource();
        node.threadNum = vo.getThreadNum();
        node.passQps = vo.getPassQps();
        node.blockQps = vo.getBlockQps();
        node.totalQps = vo.getTotalQps();
        node.averageRt = vo.getAverageRt();
        node.successQps = vo.getSuccessQps();
        node.exceptionQps = vo.getExceptionQps();
        node.oneMinutePass = vo.getOneMinutePass();
        node.oneMinuteBlock = vo.getOneMinuteBlock();
        node.oneMinuteException = vo.getOneMinuteException();
        node.oneMinuteTotal = vo.getOneMinuteTotal();
        return node;
    }

    /**
     * 搜索节点，忽略大小写
     *
     * @param searchKey 搜索关键字
     */
    public void searchIgnoreCase(String searchKey) {
        search(this, searchKey);
    }

    /**
     * 递归搜索，找到匹配的节点，设置 visible 为 true，同时向上递归设置 visible 为 true
     *
     * @param node      当前节点
     * @param searchKey 搜索关键字
     * @return 搜索结果
     */
    private boolean search(ResourceTreeNode node, String searchKey) {
        node.visible = searchKey == null || searchKey.isEmpty() ||
                       node.resource.toLowerCase().contains(searchKey.toLowerCase());

        boolean found = false;
        for (ResourceTreeNode c : node.children) {
            found |= search(c, searchKey);
        }
        node.visible |= found;
        return node.visible;
    }
}

