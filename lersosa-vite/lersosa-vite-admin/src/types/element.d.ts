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

import type * as ep from 'element-plus';
declare global {
  declare type ElTagType = 'primary' | 'success' | 'info' | 'warning' | 'danger';
  declare type ElFormInstance = ep.FormInstance;
  declare type ElTableInstance = ep.TableInstance;
  declare type ElUploadInstance = ep.UploadInstance;
  declare type ElScrollbarInstance = ep.ScrollbarInstance;
  declare type ElInputInstance = ep.InputInstance;
  declare type ElInputNumberInstance = ep.InputNumberInstance;
  declare type ElRadioInstance = ep.RadioInstance;
  declare type ElRadioGroupInstance = ep.RadioGroupInstance;
  declare type ElRadioButtonInstance = ep.RadioButtonInstance;
  declare type ElCheckboxInstance = ep.CheckboxInstance;
  declare type ElSwitchInstance = ep.SwitchInstance;
  declare type ElCascaderInstance = ep.CascaderInstance;
  declare type ElColorPickerInstance = ep.ColorPickerInstance;
  declare type ElRateInstance = ep.RateInstance;
  declare type ElSliderInstance = ep.SliderInstance;

  declare type ElTreeInstance = InstanceType<typeof ep.ElTree>;
  declare type ElTreeSelectInstance = InstanceType<typeof ep.ElTreeSelect>;
  declare type ElSelectInstance = InstanceType<typeof ep.ElSelect>;
  declare type ElCardInstance = InstanceType<typeof ep.ElCard>;
  declare type ElDialogInstance = InstanceType<typeof ep.ElDialog>;
  declare type ElCheckboxGroupInstance = InstanceType<typeof ep.ElCheckboxGroup>;
  declare type ElDatePickerInstance = InstanceType<typeof ep.ElDatePicker>;
  declare type ElTimePickerInstance = InstanceType<typeof ep.ElTimePicker>;
  declare type ElTimeSelectInstance = InstanceType<typeof ep.ElTimeSelect>;

  declare type TransferKey = ep.TransferKey;
  declare type CheckboxValueType = ep.CheckboxValueType;
  declare type ElFormRules = ep.FormRules;
  declare type DateModelType = ep.DateModelType;
  declare type UploadFile = ep.UploadFile;
}
