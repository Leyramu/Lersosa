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

import BaseRenderer from 'diagram-js/lib/draw/BaseRenderer';
import {
  append as svgAppend,
  attr as svgAttr,
  create as svgCreate,
  select as svgSelect,
  selectAll as svgSelectAll,
  clone as svgClone,
  clear as svgClear,
  remove as svgRemove
} from 'tiny-svg';

const HIGH_PRIORITY = 1500;
export default class CustomRenderer extends BaseRenderer {
  bpmnRenderer: BaseRenderer;
  modeling: any;
  constructor(eventBus, bpmnRenderer, modeling) {
    super(eventBus, HIGH_PRIORITY);
    this.bpmnRenderer = bpmnRenderer;
    this.modeling = modeling;
  }
  canRender(element) {
    // ignore labels
    return !element.labelTarget;
  }

  /**
   * 自定义节点图形
   * @param {*} parentNode 当前元素的svgNode
   * @param {*} element
   * @returns
   */
  drawShape(parentNode, element) {
    const shape = this.bpmnRenderer.drawShape(parentNode, element);
    const { type, width, height } = element;
    // 开始 填充绿色
    if (type === 'bpmn:StartEvent') {
      svgAttr(shape, { fill: '#77DF6D' });
      return shape;
    }
    if (type === 'bpmn:EndEvent') {
      svgAttr(shape, { fill: '#EE7B77' });
      return shape;
    }
    if (type === 'bpmn:UserTask') {
      svgAttr(shape, { fill: '#A9C4F8' });
      return shape;
    }
    return shape;
  }

  getShapePath(shape) {
    return this.bpmnRenderer.getShapePath(shape);
  }
}
CustomRenderer['$inject'] = ['eventBus', 'bpmnRenderer'];
