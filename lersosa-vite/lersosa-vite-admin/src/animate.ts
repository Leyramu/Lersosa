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

// 前缀
const animatePrefix = 'animate__animated ';
// 开启随机动画 随机动画值
const animateList: string[] = [
  animatePrefix + 'animate__pulse',
  animatePrefix + 'animate__rubberBand',
  animatePrefix + 'animate__bounceIn',
  animatePrefix + 'animate__bounceInLeft',
  animatePrefix + 'animate__fadeIn',
  animatePrefix + 'animate__fadeInLeft',
  animatePrefix + 'animate__fadeInDown',
  animatePrefix + 'animate__fadeInUp',
  animatePrefix + 'animate__flipInX',
  animatePrefix + 'animate__lightSpeedInLeft',
  animatePrefix + 'animate__rotateInDownLeft',
  animatePrefix + 'animate__rollIn',
  animatePrefix + 'animate__rotateInDownLeft',
  animatePrefix + 'animate__zoomIn',
  animatePrefix + 'animate__zoomInDown',
  animatePrefix + 'animate__slideInLeft',
  animatePrefix + 'animate__lightSpeedIn'
];
// 关闭随机动画后的默认效果
const defaultAnimate = animatePrefix + 'animate__fadeIn';
// 搜索隐藏显示动画
const searchAnimate = {
  enter: '',
  leave: ''
};

// 菜单搜索动画
const menuSearchAnimate = {
  enter: animatePrefix + 'animate__fadeIn',
  leave: animatePrefix + 'animate__fadeOut'
};
// logo动画
const logoAnimate = {
  enter: animatePrefix + 'animate__fadeIn',
  leave: animatePrefix + 'animate__fadeOut'
};

export default {
  animateList,
  defaultAnimate,
  searchAnimate,
  menuSearchAnimate,
  logoAnimate
};
