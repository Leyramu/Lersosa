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

export const NodeName = {
  'bpmn:Process': '流程',
  'bpmn:StartEvent': '开始事件',
  'bpmn:IntermediateThrowEvent': '中间事件',
  'bpmn:Task': '任务',
  'bpmn:SendTask': '发送任务',
  'bpmn:ReceiveTask': '接收任务',
  'bpmn:UserTask': '用户任务',
  'bpmn:ManualTask': '手工任务',
  'bpmn:BusinessRuleTask': '业务规则任务',
  'bpmn:ServiceTask': '服务任务',
  'bpmn:ScriptTask': '脚本任务',
  'bpmn:EndEvent': '结束事件',
  'bpmn:SequenceFlow': '流程线',
  'bpmn:ExclusiveGateway': '互斥网关',
  'bpmn:ParallelGateway': '并行网关',
  'bpmn:InclusiveGateway': '相容网关',
  'bpmn:ComplexGateway': '复杂网关',
  'bpmn:EventBasedGateway': '事件网关',
  'bpmn:Participant': '池/参与者',
  'bpmn:SubProcess': '子流程',
  'bpmn:DataObjectReference': '数据对象引用',
  'bpmn:DataStoreReference': '数据存储引用',
  'bpmn:Group': '组'
};

export default {
  'Activate hand tool': '启动手动工具',
  'Activate lasso tool': '启动 Lasso 工具',
  'Activate create/remove space tool': '启动创建/删除空间工具',
  'Activate global connect tool': '启动全局连接工具',
  'Ad-hoc': 'Ad-hoc',
  'Add lane above': '在上方添加泳道',
  'Add lane below': '在下方添加泳道',
  'Business rule task': '规则任务',
  'Call activity': '引用流程',
  'Compensation end event': '结束补偿事件',
  'Compensation intermediate throw event': '中间补偿抛出事件',
  'Complex gateway': '复杂网关',
  'Conditional intermediate catch event': '中间条件捕获事件',
  'Conditional start event (non-interrupting)': '条件启动事件 (非中断)',
  'Conditional start event': '条件启动事件',
  'Connect using association': '文本关联',
  'Connect using sequence/message flow or association': '消息关联',
  'Change element': '更改元素',
  'Change type': '更改类型',
  'Create data object reference': '创建数据对象引用',
  'Create data store reference': '创建数据存储引用',
  'Create expanded sub-process': '创建可折叠子流程',
  'Create pool/participant': '创建池/参与者',
  'Collection': '集合',
  'Connect using data input association': '数据输入关联',
  'Data store reference': '数据存储引用',
  'Data object reference': '数据对象引用',
  'Divide into two lanes': '分成两个泳道',
  'Divide into three lanes': '分成三个泳道',
  'End event': '结束事件',
  'Error end event': '结束错误事件',
  'Escalation end event': '结束升级事件',
  'Escalation intermediate throw event': '中间升级抛出事件',
  'Event sub-process': '事件子流程',
  'Event-based gateway': '事件网关',
  'Exclusive gateway': '互斥网关',
  'Empty pool/participant (removes content)': '清空池/参与者 (删除内容)',
  'Empty pool/participant': '清空池/参与者',
  'Expanded pool/participant': '展开池/参与者',
  'Inclusive gateway': '相容网关',
  'Intermediate throw event': '中间抛出事件',
  'Loop': '循环',
  'Link intermediate catch event': '中间链接捕获事件',
  'Link intermediate throw event': '中间链接抛出事件',
  'Manual task': '手动任务',
  'Message end event': '结束消息事件',
  'Message intermediate catch event': '中间消息捕获事件',
  'Message intermediate throw event': '中间消息抛出事件',
  'Message start event': '消息启动事件',
  'Parallel gateway': '并行网关',
  'Parallel multi-instance': '并行多实例',
  'Participant multiplicity': '参与者多重性',
  'Receive task': '接受任务',
  'Remove': '移除',
  'Script task': '脚本任务',
  'Send task': '发送任务',
  'Sequential multi-instance': '串行多实例',
  'Service task': '服务任务',
  'Signal end event': '结束信号事件',
  'Signal intermediate catch event': '中间信号捕获事件',
  'Signal intermediate throw event': '中间信号抛出事件',
  'Signal start event (non-interrupting)': '信号启动事件 (非中断)',
  'Signal start event': '信号启动事件',
  'Start event': '开始事件',
  'Sub-process (collapsed)': '可折叠子流程',
  'Sub-process (expanded)': '可展开子流程',
  'Sub rocess': '子流程',
  'Task': '任务',
  'Transaction': '事务',
  'Terminate end event': '终止边界事件',
  'Timer intermediate catch event': '中间定时捕获事件',
  'Timer start event (non-interrupting)': '定时启动事件 (非中断)',
  'Timer start event': '定时启动事件',
  'User task': '用户任务',
  'Create start event': '创建开始事件',
  'Create gateway': '创建网关',
  'Create intermediate/boundary event': '创建中间/边界事件',
  'Create end event': '创建结束事件',
  'Create group': '创建组',
  'Create startEvent': '开始节点',
  'Create endEvent': '结束节点',
  'Create exclusiveGateway': '互斥网关',
  'Create parallelGateway': '并行网关',
  'Create task': '任务节点',
  'Create userTask': '用户任务节点',
  'Condition type': '条件类型',
  'Append end event': '追加结束事件节点',
  'Append gateway': '追加网关节点',
  'Append task': '追加任务',
  'Append user task': '追加用户任务节点',
  'Append text annotation': '追加文本注释',
  'Append intermediate/boundary event': '追加中间或边界事件',
  'Append receive task': '追加接收任务节点',
  'Append message intermediate catch event': '追加中间消息捕获事件',
  'Append timer intermediate catch event': '追加中间定时捕获事件',
  'Append conditional intermediate catch event': '追加中间条件捕获事件',
  'Append signal intermediate catch event': '追加中间信号捕获事件',
  'flow elements must be children of pools/participants': '流程元素必须是池/参与者的子元素'
};
