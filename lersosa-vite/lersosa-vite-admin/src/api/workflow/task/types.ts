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

import { NodeConfigVO } from '@/api/workflow/nodeConfig/types';
import { DefinitionConfigVO } from '@/api/workflow/definitionConfig/types';
export interface TaskQuery extends PageQuery {
  name?: string;
  processDefinitionKey?: string;
  processDefinitionName?: string;
}

export interface ParticipantVo {
  groupIds?: string[] | number[];
  candidate: string[] | number[];
  candidateName: string[];
  claim: boolean;
}

export interface TaskVO extends BaseEntity {
  id: string;
  name: string;
  description?: string;
  priority: number;
  owner?: string;
  assignee?: string | number;
  assigneeName?: string;
  processInstanceId: string;
  executionId: string;
  taskDefinitionId?: any;
  processDefinitionId: string;
  endTime?: string;
  taskDefinitionKey: string;
  dueDate?: string;
  category?: any;
  parentTaskId?: any;
  tenantId: string;
  claimTime?: string;
  businessStatus?: string;
  businessStatusName?: string;
  processDefinitionName?: string;
  processDefinitionKey?: string;
  participantVo?: ParticipantVo;
  multiInstance?: boolean;
  businessKey?: string;
  wfNodeConfigVo?: NodeConfigVO;
  wfDefinitionConfigVo?: DefinitionConfigVO;
}

export interface VariableVo {
  key: string;
  value: string;
}
