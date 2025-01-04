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

/*
 * @Author: kasuie
 * @Date: 2024-05-30 10:45:37
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-28 10:47:10
 * @Description:
 */
export const showMotion = {
    initial: 'initial',
    animate: 'animate',
    exit: 'exit',
    variants: {
        initial: { opacity: 0, y: -100 },
        animate: { opacity: 1, y: 0 },
        exit: { opacity: 0, y: -100 }
    },
    transition: {
        duration: 0.5,
        delay: 0
    }
};

export const getMotion = (
    gap: number = 0.3,
    index: number = 0,
    duration: number = 0.3,
    animate: boolean = true,
    key: string = 'toBottom'
) => {
    if (!animate) return {};
    const motions = createMotions(key);
    return {
        ...motions,
        transition: {
            ...motions.transition,
            duration: duration,
            delay: Math.max(gap * index - gap / 2, 0)
        }
    };
};

const createMotions = (key: string) => {
    let motions = Object.assign({}, showMotion);
    switch (key) {
        case 'toTop':
            motions.variants = {
                initial: { opacity: 0, y: 30 },
                animate: { opacity: 1, y: 0 },
                exit: { opacity: 0, y: 30 }
            };
            return motions;
        case 'toBottom':
            return motions;
        default:
            return motions;
    }
};

export const variants = {
    default: {
        initial: { opacity: 0 },
        animate: { opacity: 1 },
        exit: { opacity: 0 }
    },
    toBottom: {
        initial: { opacity: 0.5, y: -15, scale: 1 },
        animate: { opacity: 1, y: 0, scale: 1 },
        exit: { opacity: 0.5, y: 15, scale: 1 }
    },
    toTop: {
        initial: { opacity: 0.5, y: 15, scale: 1 },
        animate: { opacity: 1, y: 0, scale: 1 },
        exit: { opacity: 0.5, y: -15, scale: 1 }
    },
    toLeft: {
        initial: { opacity: 0.5, x: 15, scale: 1 },
        animate: { opacity: 1, x: 0, scale: 1 },
        exit: { opacity: 0.5, x: -15, scale: 1 }
    },
    toRight: {
        initial: { opacity: 0.5, x: -15, scale: 1 },
        animate: { opacity: 1, x: 0, scale: 1 },
        exit: { opacity: 0.5, x: 15, scale: 1 }
    },
    toIn: {
        initial: { opacity: 0.5, scale: 1.1 },
        animate: { opacity: 1, scale: 1 },
        exit: { opacity: 0.5, scale: 0.9 }
    },
    toOut: {
        initial: { opacity: 0.5, scale: 0.9 },
        animate: { opacity: 1, scale: 1 },
        exit: { opacity: 0.5, scale: 1.1 }
    },
    toInOut: {
        initial: { opacity: 0.5, scale: 1.1 },
        animate: { opacity: 1, scale: 1 },
        exit: { opacity: 0.5, scale: 1.1 }
    },
    toOutIn: {
        initial: { opacity: 0.5, scale: 0.9 },
        animate: { opacity: 1, scale: 1 },
        exit: { opacity: 0.5, scale: 0.9 }
    }
};
