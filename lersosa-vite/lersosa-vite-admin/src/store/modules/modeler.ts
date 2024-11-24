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

import type { Modeler, Modeling, Canvas, ElementRegistry, Moddle, BpmnFactory } from 'bpmn';

type ModelerStore = {
  modeler: Modeler | undefined;
  moddle: Moddle | undefined;
  modeling: Modeling | undefined;
  canvas: Canvas | undefined;
  elementRegistry: ElementRegistry | undefined;
  bpmnFactory: BpmnFactory | undefined;
  // 流程定义根节点信息
  procDefId: string | undefined;
  procDefName: string | undefined;
};

const defaultState: ModelerStore = {
  modeler: undefined,
  moddle: undefined,
  modeling: undefined,
  canvas: undefined,
  elementRegistry: undefined,
  bpmnFactory: undefined,
  procDefId: undefined,
  procDefName: undefined
};
export const useModelerStore = defineStore('modeler', () => {
  let modeler = defaultState.modeler;
  let moddle = defaultState.moddle;
  let modeling = defaultState.modeling;
  let canvas = defaultState.canvas;
  let elementRegistry = defaultState.elementRegistry;
  let bpmnFactory = defaultState.bpmnFactory;
  const procDefId = ref(defaultState.procDefId);
  const procDefName = ref(defaultState.procDefName);

  const getModeler = () => modeler;
  const getModdle = () => moddle;
  const getModeling = (): Modeling | undefined => modeling;
  const getCanvas = (): Canvas | undefined => canvas;
  const getElRegistry = (): ElementRegistry | undefined => elementRegistry;
  const getBpmnFactory = (): BpmnFactory | undefined => bpmnFactory;
  const getProcDefId = (): string | undefined => procDefId.value;
  const getProcDefName = (): string | undefined => procDefName.value;

  // 设置根节点
  const setModeler = (modelers: Modeler | undefined) => {
    if (modelers) {
      modeler = modelers;
      modeling = modelers.get<Modeling>('modeling');
      moddle = modelers.get<Moddle>('moddle');
      canvas = modelers.get<Canvas>('canvas');
      bpmnFactory = modelers.get<BpmnFactory>('bpmnFactory');
      elementRegistry = modelers.get<ElementRegistry>('elementRegistry');
    } else {
      modeling = moddle = canvas = elementRegistry = bpmnFactory = undefined;
    }
  };
  // 设置流程定义根节点信息
  const setProcDef = (modeler: Modeler | undefined) => {
    procDefId.value = modeler.get<Canvas>('canvas').getRootElement().businessObject.get('id');
    procDefName.value = modeler.get<Canvas>('canvas').getRootElement().businessObject.get('name');
  };

  return {
    getModeler,
    getModdle,
    getModeling,
    getCanvas,
    getElRegistry,
    getBpmnFactory,
    getProcDefId,
    getProcDefName,
    setModeler,
    setProcDef
  };
});
export default useModelerStore;
