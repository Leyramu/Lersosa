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
 * @Date: 2024-06-24 22:12:19
 * @LastEditors: kasuie
 * @LastEditTime: 2024-08-17 21:16:13
 * @Description:
 */
'use client';
import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import { clsx } from '@kasuie/utils';
import { SpeakerLoud, SpeakerOff } from '@kasuie/icon';

export const MuteSwitcher = ({
                                 className = 'relative',
                                 size = 8,
                                 onSwitch,
                                 motions
                             }: {
    className?: string;
    size?: number;
    onSwitch?: Function;
    motions?: object;
}) => {
    const [checked, setChecked] = useState(false);

    useEffect(() => {
        setChecked(true);
    }, [onSwitch]);

    const onChange = () => {
        if (!checked) {
            setChecked(true);
            onSwitch?.(true);
        } else {
            setChecked(false);
            onSwitch?.(false);
        }
    };

    return (
        <motion.div {...motions} className={className}>
            <div
                onClick={onChange}
                className="relative flex h-8 w-8 cursor-pointer items-center justify-center overflow-hidden rounded-full bg-white/15 opacity-75 shadow-[2px_2px_10px_rgba(0,0,0,0.13)] transition duration-300 hover:opacity-100"
            >
                <div
                    className={clsx(
                        'z-2 flex h-full w-full items-center justify-center transition duration-300',
                        {
                            'opacity-0': checked
                        }
                    )}
                >
                    <SpeakerLoud size={16} />
                </div>
                <div
                    className={clsx(
                        'z-3 absolute flex h-full w-full items-center justify-center opacity-0 transition duration-300',
                        {
                            'opacity-100': checked
                        }
                    )}
                >
                    <SpeakerOff size={16} />
                </div>
            </div>
        </motion.div>
    );
};
