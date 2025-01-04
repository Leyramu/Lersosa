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
 * @Date: 2024-06-11 14:12:32
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-11 20:29:31
 * @Description:
 */
'use client';

import type { FC, ForwardRefExoticComponent, JSX, RefAttributes } from 'react';
import React from 'react';
import { HTMLMotionProps, motion } from 'framer-motion';
import { microReboundPreset } from '@kasuie/utils';

export const TextUpView: FC<
    {
        text?: string;
        children?: string | React.ReactNode;
        appear?: boolean;
        as?: keyof typeof motion;
        rootAs?: keyof typeof motion;
        eachDelay?: number;
        initialDelay?: number;
    } & JSX.IntrinsicElements['div']
> = (props) => {
    const {
        appear = true,
        eachDelay = 0.1,
        initialDelay = 0,
        children,
        text,
        rootAs = 'div',
        as = 'div',
        ...rest
    } = props;

    const MotionComponent = motion[as] as ForwardRefExoticComponent<
        HTMLMotionProps<any> & RefAttributes<HTMLElement>
    >;

    const RootElement = motion[rootAs] as any;

    if (!appear) {
        return <div {...rest}>{text ?? children}</div>;
    }

    return (
        <RootElement {...rest}>
            {Array.from(text ?? (children as string)).map((char, i) => (
                <MotionComponent
                    key={i}
                    className="inline-block whitespace-pre"
                    initial={{ transform: 'translateY(10px)', opacity: 0.001 }}
                    animate={{
                        transform: 'translateY(0px)',
                        opacity: 1,
                        transition: {
                            ...microReboundPreset,
                            duration: 0.1,
                            delay: i * eachDelay + initialDelay
                        }
                    }}
                >
                    {char}
                </MotionComponent>
            ))}
        </RootElement>
    );
};
