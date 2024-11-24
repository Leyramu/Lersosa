<!--
  - Copyright (c) 2024 Leyramu Group. All rights reserved.
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -      http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  -
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  -
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  -
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <div class="">
    <v-form-render ref="vFormRef" :form-json="formJson" :form-data="formData" />
  </div>
</template>

<!-- 动态表单渲染 -->
<script setup name="Render" lang="ts">
interface Props {
  formJson: string | object;
  formData: string | object;
  isView: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  formJson: '',
  formData: '',
  isView: false
});

const vFormRef = ref();
// 获取表单数据-异步
const getFormData = () => {
  return vFormRef.value.getFormData();
};

/**
 * 设置表单内容
 * @param {} formConf
 * formConfig：{ formTemplate：表单模板，formData：表单数据，hiddenField：需要隐藏的字段字符串集合，disabledField：需要禁用的自读字符串集合}
 */
const initForm = (formConf: any) => {
  const { formTemplate, formData, hiddenField, disabledField } = toRaw(formConf);
  if (formTemplate) {
    vFormRef.value.setFormJson(formTemplate);
    if (formData) {
      vFormRef.value.setFormData(formData);
    }
    if (disabledField && disabledField.length > 0) {
      setTimeout(() => {
        vFormRef.value.disableWidgets(disabledField);
      }, 200);
    }
    if (hiddenField && hiddenField.length > 0) {
      setTimeout(() => {
        vFormRef.value.hideWidgets(hiddenField);
      }, 200);
    }
    if (props.isView) {
      setTimeout(() => {
        vFormRef.value.disableForm();
      }, 100);
    }
  }
};
defineExpose({ getFormData, initForm });
</script>
