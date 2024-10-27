/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

// 处理主题样式
export function handleThemeStyle(theme: string): void {
    document.documentElement.style.setProperty('--el-color-primary', theme);
    for (let i = 1; i <= 9; i++) {
        document.documentElement.style.setProperty(`--el-color-primary-light-${i}`, `${getLightColor(theme, i / 10)}`);
    }
    for (let i = 1; i <= 9; i++) {
        document.documentElement.style.setProperty(`--el-color-primary-dark-${i}`, `${getDarkColor(theme, i / 10)}`);
    }
}

// hex颜色转rgb颜色
export function hexToRgb(str: string): number[] {
    str = str.replace('#', '');
    return (str.match(/../g) || []).map(hex => parseInt(hex, 16));
}

// rgb颜色转Hex颜色
export function rgbToHex(r: number, g: number, b: number): string {
    const hexs = [r.toString(16), g.toString(16), b.toString(16)];
    for (let i = 0; i < 3; i++) {
        if (hexs[i].length === 1) {
            hexs[i] = `0${hexs[i]}`;
        }
    }
    return `#${hexs.join('')}`;
}

// 变浅颜色值
export function getLightColor(color: string, level: number): string {
    const rgb = hexToRgb(color);
    for (let i = 0; i < 3; i++) {
        rgb[i] = Math.floor((255 - rgb[i]) * level + rgb[i]);
    }
    return rgbToHex(rgb[0], rgb[1], rgb[2]);
}

// 变深颜色值
export function getDarkColor(color: string, level: number): string {
    const rgb = hexToRgb(color);
    for (let i = 0; i < 3; i++) {
        rgb[i] = Math.floor(rgb[i] * (1 - level));
    }
    return rgbToHex(rgb[0], rgb[1], rgb[2]);
}
