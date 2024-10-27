/*
 * Copyright (c) 2022-2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {DirectiveBinding} from 'vue';

// 扩展 HTMLElement 类型
interface CustomHTMLElement extends HTMLElement {
    $copyCallback?: (value: string) => void;
    $copyValue?: string;
    $destroyCopy?: () => void;
}

export default {
    beforeMount(el: CustomHTMLElement, binding: DirectiveBinding<string | ((value: string) => void)>) {
        const {value, arg} = binding;
        if (arg === "callback") {
            el.$copyCallback = value as ((value: string) => void);
        } else {
            el.$copyValue = value as string;
            const handler = () => {
                if (el.$copyValue != null) {
                    copyTextToClipboard(el.$copyValue).then(_ => {
                    });
                }
                if (el.$copyCallback) {
                    if (el.$copyValue != null) {
                        el.$copyCallback(el.$copyValue);
                    }
                }
            };
            el.addEventListener("click", handler);
            el.$destroyCopy = () => el.removeEventListener("click", handler);
        }
    }
};

async function copyTextToClipboard(input: string, options: { target?: HTMLElement } = {}): Promise<boolean> {
    const {target = document.body} = options;
    const element = document.createElement('textarea');
    const previouslyFocusedElement = document.activeElement as HTMLElement | null;

    element.value = input;

    element.setAttribute('readonly', '');

    element.style.contain = 'strict';
    element.style.position = 'absolute';
    element.style.left = '-9999px';
    element.style.fontSize = '12pt';

    const selection = document.getSelection();
    const originalRange = selection && selection.rangeCount > 0 ? selection.getRangeAt(0) : null;

    target.append(element);
    element.select();

    element.selectionStart = 0;
    element.selectionEnd = input.length;

    let isSuccess = false;
    try {
        // 使用 Clipboard API 替代 execCommand
        if (navigator.clipboard) {
            await navigator.clipboard.writeText(input);
            isSuccess = true;
        }
    } catch (error) {
        console.error('Failed to copy text to clipboard:', error);
    }

    element.remove();

    if (originalRange) {
        selection?.removeAllRanges();
        selection?.addRange(originalRange);
    }

    if (previouslyFocusedElement) {
        previouslyFocusedElement.focus();
    }

    return isSuccess;
}
