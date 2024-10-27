/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

// 日期格式化
export function parseTime(time: string | number | Date, pattern?: string): string | null {
    if (arguments.length === 0 || !time) {
        return null;
    }
    const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}';
    let date: Date;
    if (typeof time === 'object') {
        date = time as Date;
    } else {
        if (typeof time === 'string' && /^[0-9]+$/.test(time)) {
            time = parseInt(time);
        } else if (typeof time === 'string') {
            time = time.replace(/-/gm, '/').replace('T', ' ').replace(/\.\d{3}/gm, '');
        }
        if (typeof time === 'number' && time.toString().length === 10) {
            time = time * 1000;
        }
        date = new Date(time);
    }
    const formatObj: any = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
    };
    return format.replace(/{([ymdhisa])+}/g, (result, key) => {
        let value = formatObj[key];
        if (key === 'a') {
            return ['日', '一', '二', '三', '四', '五', '六'][value];
        }
        if (result.length > 0 && value < 10) {
            value = '0' + value;
        }
        return value || 0;
    });
}

// 表单重置
export function resetForm(this: { $refs: { [key: string]: { resetFields: () => void } } }, refName: string) {
    if (this.$refs[refName]) {
        this.$refs[refName].resetFields();
    }
}

// 添加日期范围
export function addDateRange(params: any, dateRange: [string, string] | [], propName?: string): any {
    let search = params;
    search.params = typeof search.params === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
    dateRange = Array.isArray(dateRange) ? dateRange : [];
    if (dateRange.length === 2) {
        if (typeof propName === 'undefined') {
            search.params['beginTime'] = dateRange[0];
            search.params['endTime'] = dateRange[1];
        } else {
            search.params['begin' + propName] = dateRange[0];
            search.params['end' + propName] = dateRange[1];
        }
    }
    return search;
}

// 回显数据字典
export function selectDictLabel(datas: { [key: string]: { value: string; label: string } }, value: string): string {
    if (value === undefined) {
        return "";
    }
    let actions: string[] = [];
    Object.keys(datas).some((key) => {
        if (datas[key].value === ('' + value)) {
            actions.push(datas[key].label);
            return true;
        }
    });
    if (actions.length === 0) {
        actions.push(value);
    }
    return actions.join('');
}

// 回显数据字典（字符串数组）
export function selectDictLabels(datas: {
    [key: string]: { value: string; label: string }
}, value: string | string[], separator?: string): string {
    if (value === undefined || (Array.isArray(value) && value.length === 0)) {
        return "";
    }
    if (Array.isArray(value)) {
        value = value.join(",");
    }
    let actions: string[] = [];
    let currentSeparator = separator || ",";
    let temp = value.split(currentSeparator);
    temp.forEach((val) => {
        let match = false;
        Object.keys(datas).some((key) => {
            if (datas[key].value === ('' + val)) {
                actions.push(datas[key].label + currentSeparator);
                match = true;
            }
        });
        if (!match) {
            actions.push(val + currentSeparator);
        }
    });
    return actions.join('').substring(0, actions.join('').length - 1);
}

// 字符串格式化(%s )
export function sprintf(str: string, ...args: any[]): string {
    let flag = true;
    str = str.replace(/%s/g, function () {
        let arg = args.shift();
        if (typeof arg === 'undefined') {
            flag = false;
            return '';
        }
        return arg;
    });
    return flag ? str : '';
}

// 转换字符串，undefined,null等转化为""
export function parseStrEmpty(str: string): string {
    if (!str || str === "undefined" || str === "null") {
        return "";
    }
    return str;
}

// 数据合并
export function mergeRecursive(source: any, target: any): any {
    for (const p in target) {
        try {
            if (target[p].constructor === Object) {
                source[p] = mergeRecursive(source[p], target[p]);
            } else {
                source[p] = target[p];
            }
        } catch (e) {
            source[p] = target[p];
        }
    }
    return source;
}

/**
 * 构造树型结构数据
 * @param data 数据源
 * @param id id字段 默认 'id'
 * @param parentId 父节点字段 默认 'parentId'
 * @param children 孩子节点字段 默认 'children'
 */
export function handleTree(data: any[], id?: string, parentId?: string, children?: string): any[] {
    let config = {
        id: id || 'id',
        parentId: parentId || 'parentId',
        childrenList: children || 'children'
    };

    let childrenListMap: { [key: string]: any[] } = {};
    let nodeIds: { [key: string]: any } = {};
    let tree: any[] = [];

    for (let d of data) {
        let parentId = d[config.parentId];
        if (childrenListMap[parentId] == null) {
            childrenListMap[parentId] = [];
        }
        nodeIds[d[config.id]] = d;
        childrenListMap[parentId].push(d);
    }

    for (let d of data) {
        let parentId = d[config.parentId];
        if (nodeIds[parentId] == null) {
            tree.push(d);
        }
    }

    for (let t of tree) {
        adaptToChildrenList(t);
    }

    function adaptToChildrenList(o: any) {
        if (childrenListMap[o[config.id]] !== null) {
            o[config.childrenList] = childrenListMap[o[config.id]];
        }
        if (o[config.childrenList]) {
            for (let c of o[config.childrenList]) {
                adaptToChildrenList(c);
            }
        }
    }

    return tree;
}

/**
 * 参数处理
 * @param params 参数
 */
export function tansParams(params: { [key: string]: any }): string {
    let result = '';
    for (const propName of Object.keys(params)) {
        const value = params[propName];
        let part = encodeURIComponent(propName) + '=';
        if (value !== null && value !== '' && typeof value !== 'undefined') {
            if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                    if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
                        let params = propName + '[' + key + ']';
                        let subPart = encodeURIComponent(params) + '=';
                        result += subPart + encodeURIComponent(value[key]) + '&';
                    }
                }
            } else {
                result += part + encodeURIComponent(value) + '&';
            }
        }
    }
    return result;
}

// 返回项目路径
export function getNormalPath(p: string): string {
    if (p.length === 0 || !p || p === 'undefined') {
        return p;
    }
    let res = p.replace('//', '/');
    if (res[res.length - 1] === '/') {
        return res.slice(0, res.length - 1);
    }
    return res;
}

// 验证是否为blob格式
export function blobValidate(data: Blob): boolean {
    return data.type !== 'application/json';
}
