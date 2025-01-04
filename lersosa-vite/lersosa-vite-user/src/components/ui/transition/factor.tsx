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
 * @Date: 2024-06-06 21:23:41
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-11 17:28:55
 * @Description:
 */
'use client';

import type { ForwardRefExoticComponent, PropsWithChildren, RefAttributes } from 'react';
import { forwardRef, memo, useState } from 'react';
import type { HTMLMotionProps, MotionProps, Spring, Target, TargetAndTransition } from 'framer-motion';
import { motion } from 'framer-motion';
import type { BaseTransitionProps } from './typings';

// import { isHydrationEnded } from '~/components/common/HydrationEndDetector'
import { microReboundPreset } from '@kasuie/utils';

export interface TransitionViewParams {
    from?: Target;
    to?: Target;
    initial?: Target;
    preset?: Spring;
}

export const createTransitionView = (params: TransitionViewParams) => {
    const { from, to, initial, preset } = params;

    const TransitionView = forwardRef<
        HTMLElement,
        PropsWithChildren<BaseTransitionProps>
    >((props, ref) => {
        const {
            timeout = {},
            duration = 0.5,
            animate = true,
            animation = {},
            as = 'div',
            delay = 0,
            lcpOptimization = false,
            ...rest
        } = props;

        const { enter = delay, exit = delay } = timeout;

        const MotionComponent = motion[as] as ForwardRefExoticComponent<
            HTMLMotionProps<any> & RefAttributes<HTMLElement>
        >;

        const [stableIsHydrationEnded] = useState(true);

        if (!animate) {
            return <MotionComponent {...rest}>{props.children}</MotionComponent>;
        }

        const motionProps: MotionProps = {
            initial: initial || from,
            animate: {
                ...to,
                transition: {
                    duration,
                    ...(preset || microReboundPreset),
                    ...animation.enter,
                    delay: enter / 1000
                }
            },
            transition: {
                duration
            },
            exit: {
                ...from,
                transition: {
                    duration,
                    ...animation.exit,
                    delay: exit / 1000
                } as TargetAndTransition['transition']
            }
        };

        // if (lcpOptimization && !stableIsHydrationEnded) {
        //   motionProps.initial = to;
        //   delete motionProps.animate;
        // }

        return (
            <MotionComponent ref={ref} {...motionProps} {...rest}>
                {props.children}
            </MotionComponent>
        );
    });
    TransitionView.displayName = `forwardRef(TransitionView)`;
    const MemoedTransitionView = memo(TransitionView); // 优化性能，防止不必要的重新渲染
    MemoedTransitionView.displayName = `MemoedTransitionView`;
    return MemoedTransitionView;
};
