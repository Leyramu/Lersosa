/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

/**
 * 判断url是否是http或https
 * @param {string} url
 * @returns {boolean}
 */
export function isHttp(url: string): boolean {
    return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1;
}

/**
 * 判断path是否为外链
 * @param {string} path
 * @returns {boolean}
 */
export function isExternal(path: string): boolean {
    return /^(https?:|mailto:|tel:)/.test(path);
}

/**
 * @param {string} str
 * @returns {boolean}
 */
export function validUsername(str: string): boolean {
    const validMap = ['admin', 'editor'];
    return validMap.includes(str.trim());
}

/**
 * @param {string} url
 * @returns {boolean}
 */
export function validURL(url: string): boolean {
    const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
    return reg.test(url);
}

/**
 * @param {string} str
 * @returns {boolean}
 */
export function validLowerCase(str: string): boolean {
    const reg = /^[a-z]+$/;
    return reg.test(str);
}

/**
 * @param {string} str
 * @returns {boolean}
 */
export function validUpperCase(str: string): boolean {
    const reg = /^[A-Z]+$/;
    return reg.test(str);
}

/**
 * @param {string} str
 * @returns {boolean}
 */
export function validAlphabets(str: string): boolean {
    const reg = /^[A-Za-z]+$/;
    return reg.test(str);
}

/**
 * @param {string} email
 * @returns {boolean}
 */
export function validEmail(email: string): boolean {
    const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return reg.test(email);
}

/**
 * @param {any} str
 * @returns {boolean}
 */
export function isString(str: any): boolean {
    return typeof str === 'string' || str instanceof String;
}

/**
 * @param {any} arg
 * @returns {boolean}
 */
export function isArray(arg: any): boolean {
    if (typeof Array.isArray === 'undefined') {
        return Object.prototype.toString.call(arg) === '[object Array]';
    }
    return Array.isArray(arg);
}
