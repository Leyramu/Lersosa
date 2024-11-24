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

export const useDictStore = defineStore('dict', () => {
  const dict = ref<
    Array<{
      key: string;
      value: DictDataOption[];
    }>
  >([]);

  /**
   * 获取字典
   * @param _key 字典key
   */
  const getDict = (_key: string): DictDataOption[] | null => {
    if (_key == null && _key == '') {
      return null;
    }
    try {
      for (let i = 0; i < dict.value.length; i++) {
        if (dict.value[i].key == _key) {
          return dict.value[i].value;
        }
      }
    } catch (e) {
      return null;
    }
    return null;
  };

  /**
   * 设置字典
   * @param _key 字典key
   * @param _value 字典value
   */
  const setDict = (_key: string, _value: DictDataOption[]) => {
    if (_key !== null && _key !== '') {
      dict.value.push({
        key: _key,
        value: _value
      });
    }
  };

  /**
   * 删除字典
   * @param _key
   */
  const removeDict = (_key: string): boolean => {
    let bln = false;
    try {
      for (let i = 0; i < dict.value.length; i++) {
        if (dict.value[i].key == _key) {
          dict.value.splice(i, 1);
          return true;
        }
      }
    } catch (e) {
      bln = false;
    }
    return bln;
  };

  /**
   * 清空字典
   */
  const cleanDict = (): void => {
    dict.value = [];
  };

  return {
    dict,
    getDict,
    setDict,
    removeDict,
    cleanDict
  };
});

export default useDictStore;
