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
  <div>
    <el-dialog v-model="visible" :title="title" width="600px" append-to-body>
      <el-form label-width="100px">
        <el-form-item label="小时">
          <el-radio-group v-model="hourValue" @change="hourChange">
            <el-radio-button label="4" value="4" />
            <el-radio-button label="8" value="8" />
            <el-radio-button label="12" value="12" />
            <el-radio-button label="24" value="24" />
            <el-radio-button label="自定义" value="自定义" />
            <el-input-number v-show="hourValue === '自定义'" v-model="customHourValue" :min="1" @change="customHourValueChange"></el-input-number>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="天">
          <el-radio-group v-model="dayValue" @change="dayChange">
            <el-radio-button label="1" value="1" />
            <el-radio-button label="2" value="2" />
            <el-radio-button label="3" value="3" />
            <el-radio-button label="4" value="4" />
            <el-radio-button label="自定义" value="自定义" />
            <el-input-number v-show="dayValue === '自定义'" v-model="customDayValue" :min="1" @change="customDayValueChange"></el-input-number>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="周">
          <el-radio-group v-model="weekValue" @change="weekChange">
            <el-radio-button label="1" value="1" />
            <el-radio-button label="2" value="2" />
            <el-radio-button label="3" value="3" />
            <el-radio-button label="4" value="4" />
            <el-radio-button label="自定义" value="自定义" />
            <el-input-number v-show="weekValue === '自定义'" v-model="customWeekValue" :min="1" @change="customWeekValueChange"></el-input-number>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="月">
          <el-radio-group v-model="monthValue" @change="monthChange">
            <el-radio-button label="1" value="1" />
            <el-radio-button label="2" value="2" />
            <el-radio-button label="3" value="3" />
            <el-radio-button label="4" value="4" />
            <el-radio-button label="自定义" value="自定义" />
            <el-input-number v-show="monthValue === '自定义'" v-model="customMonthValue" :min="1" @change="customMonthValueChange"></el-input-number>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <div>
          <el-button @click="closeDialog">取消</el-button>
          <el-button type="primary" @click="confirm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import useDialog from '@/hooks/useDialog';

interface PropType {
  modelValue?: string;
  data?: string;
}

const prop = withDefaults(defineProps<PropType>(), {
  modelValue: '',
  data: ''
});
const emit = defineEmits(['update:modelValue', 'confirmCallBack']);

const { title, visible, openDialog, closeDialog } = useDialog({
  title: '设置任务到期时间'
});
const formValue = ref();
const valueType = ref();

const hourValue = ref('');
const dayValue = ref('');
const weekValue = ref('');
const monthValue = ref('');

const customHourValue = ref(1);
const customDayValue = ref(1);
const customWeekValue = ref(1);
const customMonthValue = ref(1);

const hourValueConst = ['4', '8', '12', '24'];
const dayAndWeekAndMonthValueConst = ['1', '2', '3', '4'];

const initValue = () => {
  formValue.value = prop.data;
  if (prop.data) {
    const lastStr = prop.data.substring(prop.data.length - 1);
    if (lastStr === 'H') {
      const hourValueValue = prop.data.substring(2, prop.data.length - 1);
      if (hourValueConst.includes(hourValueValue)) {
        hourValue.value = hourValueValue;
      } else {
        hourValue.value = '自定义';
        customHourValue.value = Number(hourValueValue);
      }
    }
    const dayAndWeekAndMonthValue = prop.data.substring(1, prop.data.length - 1);
    if (lastStr === 'D') {
      if (dayAndWeekAndMonthValueConst.includes(dayAndWeekAndMonthValue)) {
        dayValue.value = dayAndWeekAndMonthValue;
      } else {
        dayValue.value = '自定义';
        customDayValue.value = Number(dayAndWeekAndMonthValue);
      }
    }
    if (lastStr === 'W') {
      if (dayAndWeekAndMonthValueConst.includes(dayAndWeekAndMonthValue)) {
        weekValue.value = dayAndWeekAndMonthValue;
      } else {
        weekValue.value = '自定义';
        customWeekValue.value = Number(dayAndWeekAndMonthValue);
      }
    }
    if (lastStr === 'M') {
      if (dayAndWeekAndMonthValueConst.includes(dayAndWeekAndMonthValue)) {
        monthValue.value = dayAndWeekAndMonthValue;
      } else {
        monthValue.value = '自定义';
        customMonthValue.value = Number(dayAndWeekAndMonthValue);
      }
    }
  }
};

const confirm = () => {
  emit('update:modelValue', formValue.value);
  emit('confirmCallBack', formValue.value);
  closeDialog();
};

const customHourValueChange = (customHourValue: any) => {
  formValue.value = `PT${customHourValue}H`;

  dayValue.value = '';
  weekValue.value = '';
  monthValue.value = '';
  customDayValue.value = 1;
  customWeekValue.value = 1;
  customMonthValue.value = 1;
};
const customDayValueChange = (customDayValue: any) => {
  formValue.value = `P${customDayValue}D`;
  hourValue.value = '';
  weekValue.value = '';
  monthValue.value = '';

  customHourValue.value = 1;
  customWeekValue.value = 1;
  customMonthValue.value = 1;
};

const customWeekValueChange = (customWeekValue: any) => {
  formValue.value = `P${customWeekValue}W`;
  hourValue.value = '';
  dayValue.value = '';
  monthValue.value = '';

  customHourValue.value = 1;
  customDayValue.value = 1;
  customMonthValue.value = 1;
};

const customMonthValueChange = (customMonthValue: any) => {
  formValue.value = `P${customMonthValue}M`;
  hourValue.value = '';
  dayValue.value = '';
  weekValue.value = '';

  customHourValue.value = 1;
  customDayValue.value = 1;
  customWeekValue.value = 1;
};

const hourChange = (hourValue: any) => {
  if (hourValue === '自定义') {
    formValue.value = `PT${customHourValue.value}H`;
  } else {
    formValue.value = `PT${hourValue}H`;
  }

  dayValue.value = '';
  weekValue.value = '';
  monthValue.value = '';
  customDayValue.value = 1;
  customWeekValue.value = 1;
  customMonthValue.value = 1;
};
const dayChange = (dayValue: any) => {
  if (dayValue === '自定义') {
    formValue.value = `P${customDayValue.value}D`;
  } else {
    formValue.value = `P${dayValue}D`;
  }

  hourValue.value = '';
  weekValue.value = '';
  monthValue.value = '';

  customHourValue.value = 1;
  customWeekValue.value = 1;
  customMonthValue.value = 1;
};
const weekChange = (weekValue: any) => {
  if (weekValue === '自定义') {
    formValue.value = `P${customWeekValue.value}W`;
  } else {
    formValue.value = `P${weekValue}W`;
  }

  hourValue.value = '';
  dayValue.value = '';
  monthValue.value = '';

  customHourValue.value = 1;
  customDayValue.value = 1;
  customMonthValue.value = 1;
};
const monthChange = (monthValue: any) => {
  if (monthValue === '自定义') {
    formValue.value = `P${customMonthValue.value}M`;
  } else {
    formValue.value = `P${monthValue}M`;
  }

  hourValue.value = '';
  dayValue.value = '';
  weekValue.value = '';

  customHourValue.value = 1;
  customDayValue.value = 1;
  customWeekValue.value = 1;
};

watch(
  () => visible.value,
  () => {
    if (visible.value) {
      initValue();
    }
  }
);

defineExpose({
  openDialog,
  closeDialog
});
</script>
