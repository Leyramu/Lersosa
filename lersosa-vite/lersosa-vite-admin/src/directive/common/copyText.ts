/*
 * Copyright (c) 2022-2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

export default {
    beforeMount(el, {value, arg}) {
        if (arg === "callback") {
            el.$copyCallback = value;
        } else {
            el.$copyValue = value;
            const handler = () => {
                copyTextToClipboard(el.$copyValue);
                if (el.$copyCallback) {
                    el.$copyCallback(el.$copyValue);
                }
            };
            el.addEventListener("click", handler);
            el.$destroyCopy = () => el.removeEventListener("click", handler);
        }
    }
}

function copyTextToClipboard(input, {target = document.body} = {}) {
    const element = document.createElement('textarea');
    const previouslyFocusedElement = document.activeElement;

    element.value = input;

    // Prevent keyboard from showing on mobile
    element.setAttribute('readonly', '');

    element.style.contain = 'strict';
    element.style.position = 'absolute';
    element.style.left = '-9999px';
    element.style.fontSize = '12pt'; // Prevent zooming on iOS

    const selection = document.getSelection();
    const originalRange = selection.rangeCount > 0 && selection.getRangeAt(0);

    target.append(element);
    element.select();

    // Explicit selection workaround for iOS
    element.selectionStart = 0;
    element.selectionEnd = input.length;

    let isSuccess = false;
    try {
        isSuccess = document.execCommand('copy');
    } catch {
    }

    element.remove();

    if (originalRange) {
        selection.removeAllRanges();
        selection.addRange(originalRange);
    }

    // Get the focus back on the previously focused element, if any
    if (previouslyFocusedElement) {
        previouslyFocusedElement.focus();
    }

    return isSuccess;
}
