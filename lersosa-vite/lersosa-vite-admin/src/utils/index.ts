/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {parseTime} from './lersosa.ts'

/**
 * 表格时间格式化
 */
export function formatDate(cellValue: any) {
    if (cellValue == null || cellValue == "") return "";
    const date = new Date(cellValue);
    const year = date.getFullYear();
    const month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
    const day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
    const hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
    const minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
    const seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
}

/**
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time: any, option: any): any {
    if (('' + time).length === 10) {
        time = parseInt(time) * 1000
    } else {
        time = +time
    }
    const d = new Date(time)
    const now = Date.now()

    const diff = (now - d.getTime()) / 1000

    if (diff < 30) {
        return '刚刚'
    } else if (diff < 3600) {
        // less 1 hour
        return Math.ceil(diff / 60) + '分钟前'
    } else if (diff < 3600 * 24) {
        return Math.ceil(diff / 3600) + '小时前'
    } else if (diff < 3600 * 24 * 2) {
        return '1天前'
    }
    if (option) {
        return parseTime(time, option)
    } else {
        return (
            d.getMonth() +
            1 +
            '月' +
            d.getDate() +
            '日' +
            d.getHours() +
            '时' +
            d.getMinutes() +
            '分'
        )
    }
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function getQueryObject(url: any): any {
    url = url == null ? window.location.href : url
    const search = url.substring(url.lastIndexOf('?') + 1)
    const obj: Record<string, string> = {}
    const reg = /([^?&=]+)=([^?&=]*)/g
    search.replace(reg, (rs: any, $1: any, $2: any) => {
        const name = decodeURIComponent($1)
        let val = decodeURIComponent($2)
        val = String(val)
        obj[name] = val
        return rs
    })
    return obj
}

/**
 * @returns {number} output value
 * @param str
 */
export function byteLength(str: any): any {
    // returns the byte length of an utf8 string
    let s = str.length
    for (let i = str.length - 1; i >= 0; i--) {
        const code = str.charCodeAt(i)
        if (code > 0x7f && code <= 0x7ff) s++
        else if (code > 0x7ff && code <= 0xffff) s += 2
        if (code >= 0xDC00 && code <= 0xDFFF) i--
    }
    return s
}

/**
 * @param {Array} actual
 * @returns {Array}
 */
export function cleanArray(actual: any): any {
    const newArray = []
    for (let i = 0; i < actual.length; i++) {
        if (actual[i]) {
            newArray.push(actual[i])
        }
    }
    return newArray
}

/**
 * @param {Object} json
 * @returns {Array}
 */
export function param(json: any): any {
    if (!json) return ''
    return cleanArray(
        Object.keys(json).map(key => {
            if (json[key] === undefined) return ''
            return encodeURIComponent(key) + '=' + encodeURIComponent(json[key])
        })
    ).join('&')
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url: any): any {
    const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
    if (!search) {
        return {}
    }
    const obj: Record<string, string> = {}
    const searchArr = search.split('&')
    searchArr.forEach(v => {
        const index = v.indexOf('=')
        if (index !== -1) {
            const name = v.substring(0, index)
            obj[name] = v.substring(index + 1, v.length)
        }
    })
    return obj
}

/**
 * @param {string} val
 * @returns {string}
 */
export function html2Text(val: any): any {
    const div = document.createElement('div')
    div.innerHTML = val
    return div.textContent || div.innerText
}

/**
 * Merges two objects, giving the last one precedence
 * @param {Object} target
 * @param {(Object|Array)} source
 * @returns {Object}
 */
export function objectMerge(target: any, source: any): any {
    if (typeof target !== 'object') {
        target = {}
    }
    if (Array.isArray(source)) {
        return source.slice()
    }
    Object.keys(source).forEach(property => {
        const sourceProperty = source[property]
        if (typeof sourceProperty === 'object') {
            target[property] = objectMerge(target[property], sourceProperty)
        } else {
            target[property] = sourceProperty
        }
    })
    return target
}

/**
 * @param {HTMLElement} element
 * @param {string} className
 */
export function toggleClass(element: any, className: any) {
    if (!element || !className) {
        return
    }
    let classString = element.className
    const nameIndex = classString.indexOf(className)
    if (nameIndex === -1) {
        classString += '' + className
    } else {
        classString =
            classString.substring(0, nameIndex) +
            classString.substring(nameIndex + className.length)
    }
    element.className = classString
}

/**
 * @param {string} type
 * @returns {Date}
 */
export function getTime(type: any): any {
    if (type === 'start') {
        return new Date().getTime() - 3600 * 1000 * 24 * 90
    } else {
        return new Date(new Date().toDateString())
    }
}

/**
 * @param {Function} func
 * @param {number} wait
 * @param {boolean} immediate
 * @return {*}
 */
export function debounce<T>(func: (this: T, ...args: any[]) => void, wait: number, immediate: boolean): (this: T, ...args: any[]) => void {
    let timeout: any, args: any[] | null = null, context: T | null = null, timestamp: number

    const later = function () {
        // 据上一次触发时间间隔
        const last = +new Date() - timestamp

        // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
        if (last < wait && last > 0) {
            timeout = setTimeout(later, wait - last)
        } else {
            timeout = null
            // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
            if (!immediate) {
                func.apply(context as T, args as any[])
                context = args = null
            }
        }
    }

    return function (this: T, ...args: any) {
        context = this
        timestamp = +new Date()
        const callNow = immediate && !timeout
        // 如果延时不存在，重新设定延时
        if (!timeout) timeout = setTimeout(later, wait)
        if (callNow) {
            func.apply(context, args)
            context = args = null
        }

        // 返回 undefined 或者其他需要的值
        return undefined
    }
}

/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 * @param {Object} source
 * @returns {Object}
 */
export function deepClone(source: any): any {
    if (!source && typeof source !== 'object') {
        throw new Error('error arguments: deepClone requires an object');
    }
    const targetObj: Record<string, any> = source.constructor === Array ? [] : {}
    Object.keys(source).forEach(keys => {
        if (source[keys] && typeof source[keys] === 'object') {
            targetObj[keys] = deepClone(source[keys])
        } else {
            targetObj[keys] = source[keys]
        }
    })
    return targetObj
}

/**
 * @param {Array} arr
 * @returns {Array}
 */
export function uniqueArr(arr: any): any {
    return Array.from(new Set(arr))
}

/**
 * @returns {string}
 */
export function createUniqueString(): any {
    const timestamp = +new Date() + ''
    const randomNum = parseInt(String((1 + Math.random()) * 65536)) + ''
    return (+(randomNum + timestamp)).toString(32)
}

/**
 * Check if an element has a class
 * @param ele
 * @param {string} cls
 * @returns {boolean}
 */
export function hasClass(ele: any, cls: any): boolean {
    return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
}

/**
 * Add class to element
 * @param ele
 * @param {string} cls
 */
export function addClass(ele: any, cls: any) {
    if (!hasClass(ele, cls)) ele.className += ' ' + cls
}

/**
 * Remove class from element
 * @param ele
 * @param {string} cls
 */
export function removeClass(ele: any, cls: any) {
    if (hasClass(ele, cls)) {
        const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
        ele.className = ele.className.replace(reg, ' ')
    }
}

export function makeMap(str: any, expectsLowerCase: any) {
    const map = Object.create(null)
    const list = str.split(',')
    for (let i = 0; i < list.length; i++) {
        map[list[i]] = true
    }
    return expectsLowerCase
        ? (val: any) => map[val.toLowerCase()]
        : (val: any) => map[val]
}

export const exportDefault = 'export default '

export const beautifierConf = {
    html: {
        indent_size: '2',
        indent_char: ' ',
        max_preserve_newlines: '-1',
        preserve_newlines: false,
        keep_array_indentation: false,
        break_chained_methods: false,
        indent_scripts: 'separate',
        brace_style: 'end-expand',
        space_before_conditional: true,
        unescape_strings: false,
        jslint_happy: false,
        end_with_newline: true,
        wrap_line_length: '110',
        indent_inner_html: true,
        comma_first: false,
        e4x: true,
        indent_empty_lines: true
    },
    js: {
        indent_size: '2',
        indent_char: ' ',
        max_preserve_newlines: '-1',
        preserve_newlines: false,
        keep_array_indentation: false,
        break_chained_methods: false,
        indent_scripts: 'normal',
        brace_style: 'end-expand',
        space_before_conditional: true,
        unescape_strings: false,
        jslint_happy: true,
        end_with_newline: true,
        wrap_line_length: '110',
        indent_inner_html: true,
        comma_first: false,
        e4x: true,
        indent_empty_lines: true
    }
}

// 首字母大小
export function titleCase(str: any) {
    return str.replace(/( |^)[a-z]/g, (L: any) => L.toUpperCase())
}

// 下划转驼峰
export function camelCase(str: any) {
    return str.replace(/_[a-z]/g, (str1: any) => str1.substring(-1).toUpperCase())
}

export function isNumberStr(str: any) {
    return /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(str)
}

