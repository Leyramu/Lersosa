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

package leyramu.framework.lersosa.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import leyramu.framework.lersosa.common.core.exception.ServiceException;
import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.satoken.utils.LoginHelper;
import leyramu.framework.lersosa.workflow.domain.WfTaskBackNode;
import leyramu.framework.lersosa.workflow.domain.vo.MultiInstanceVo;
import leyramu.framework.lersosa.workflow.mapper.WfTaskBackNodeMapper;
import leyramu.framework.lersosa.workflow.service.IWfTaskBackNodeService;
import leyramu.framework.lersosa.workflow.utils.WorkflowUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static leyramu.framework.lersosa.workflow.common.constant.FlowConstant.MULTI_INSTANCE;
import static leyramu.framework.lersosa.workflow.common.constant.FlowConstant.USER_TASK;


/**
 * 节点驳回记录Service业务层处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WfTaskBackNodeServiceImpl implements IWfTaskBackNodeService {

    private final WfTaskBackNodeMapper wfTaskBackNodeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordExecuteNode(Task task) {
        List<WfTaskBackNode> list = getListByInstanceId(task.getProcessInstanceId());
        WfTaskBackNode wfTaskBackNode = new WfTaskBackNode();
        wfTaskBackNode.setNodeId(task.getTaskDefinitionKey());
        wfTaskBackNode.setNodeName(task.getName());
        wfTaskBackNode.setInstanceId(task.getProcessInstanceId());
        wfTaskBackNode.setAssignee(String.valueOf(LoginHelper.getUserId()));
        MultiInstanceVo multiInstance = WorkflowUtils.isMultiInstance(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        if (ObjectUtil.isNotEmpty(multiInstance)) {
            wfTaskBackNode.setTaskType(MULTI_INSTANCE);
        } else {
            wfTaskBackNode.setTaskType(USER_TASK);
        }
        if (CollUtil.isEmpty(list)) {
            wfTaskBackNode.setOrderNo(0);
            wfTaskBackNodeMapper.insert(wfTaskBackNode);
        } else {
            WfTaskBackNode taskNode = StreamUtils.findFirst(list, e -> e.getNodeId().equals(wfTaskBackNode.getNodeId()) && e.getOrderNo() == 0);
            if (ObjectUtil.isEmpty(taskNode)) {
                wfTaskBackNode.setOrderNo(list.getFirst().getOrderNo() + 1);
                WfTaskBackNode node = getListByInstanceIdAndNodeId(wfTaskBackNode.getInstanceId(), wfTaskBackNode.getNodeId());
                if (ObjectUtil.isNotEmpty(node)) {
                    node.setAssignee(node.getAssignee() + StringUtils.SEPARATOR + LoginHelper.getUserId());
                    wfTaskBackNodeMapper.updateById(node);
                } else {
                    wfTaskBackNodeMapper.insert(wfTaskBackNode);
                }
            }
        }
    }

    @Override
    public List<WfTaskBackNode> getListByInstanceId(String processInstanceId) {
        LambdaQueryWrapper<WfTaskBackNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WfTaskBackNode::getInstanceId, processInstanceId);
        wrapper.orderByDesc(WfTaskBackNode::getOrderNo);
        return wfTaskBackNodeMapper.selectList(wrapper);
    }

    @Override
    public WfTaskBackNode getListByInstanceIdAndNodeId(String processInstanceId, String nodeId) {
        LambdaQueryWrapper<WfTaskBackNode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WfTaskBackNode::getInstanceId, processInstanceId);
        queryWrapper.eq(WfTaskBackNode::getNodeId, nodeId);
        return wfTaskBackNodeMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBackTaskNode(String processInstanceId, String targetActivityId) {
        try {
            LambdaQueryWrapper<WfTaskBackNode> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WfTaskBackNode::getInstanceId, processInstanceId);
            queryWrapper.eq(WfTaskBackNode::getNodeId, targetActivityId);
            WfTaskBackNode actTaskNode = wfTaskBackNodeMapper.selectOne(queryWrapper);
            if (ObjectUtil.isNotNull(actTaskNode)) {
                Integer orderNo = actTaskNode.getOrderNo();
                List<WfTaskBackNode> taskNodeList = getListByInstanceId(processInstanceId);
                List<Long> ids = new ArrayList<>();
                if (CollUtil.isNotEmpty(taskNodeList)) {
                    for (WfTaskBackNode taskNode : taskNodeList) {
                        if (taskNode.getOrderNo() >= orderNo) {
                            ids.add(taskNode.getId());
                        }
                    }
                }
                if (CollUtil.isNotEmpty(ids)) {
                    wfTaskBackNodeMapper.deleteByIds(ids);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByInstanceId(String processInstanceId) {
        LambdaQueryWrapper<WfTaskBackNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WfTaskBackNode::getInstanceId, processInstanceId);
        List<WfTaskBackNode> list = wfTaskBackNodeMapper.selectList(wrapper);
        int delete = wfTaskBackNodeMapper.delete(wrapper);
        if (list.size() != delete) {
            throw new ServiceException("删除失败");
        }
        return true;
    }

    @Override
    public void deleteByInstanceIds(List<String> processInstanceIds) {
        LambdaQueryWrapper<WfTaskBackNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(WfTaskBackNode::getInstanceId, processInstanceIds);
        List<WfTaskBackNode> list = wfTaskBackNodeMapper.selectList(wrapper);
        int delete = wfTaskBackNodeMapper.delete(wrapper);
        if (list.size() != delete) {
            throw new ServiceException("删除失败");
        }
    }
}
