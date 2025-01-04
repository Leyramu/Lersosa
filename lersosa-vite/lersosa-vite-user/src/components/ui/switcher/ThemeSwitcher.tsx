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
 * @Date: 2024-06-04 10:29:21
 * @LastEditors: kasuie
 * @LastEditTime: 2024-08-17 21:17:44
 * @Description:
 */
'use client';
import { useTheme } from 'next-themes';
import { BaseSyntheticEvent, useEffect, useState } from 'react';
import { clsx, storage } from '@kasuie/utils';
import { motion } from 'framer-motion';

export const ThemeSwitcher = ({
                                  className = 'relative',
                                  size = 8,
                                  theme,
                                  motions
                              }: {
    className?: string;
    size?: number;
    theme?: string;
    motions?: object;
}) => {
    const { setTheme } = useTheme();

    const [checked, setChecked] = useState(false);

    useEffect(() => {
        let ltheme =
            theme == 'switcher' ? storage.l.get('theme') : theme || 'light';
        if (theme != 'switcher') setTheme(ltheme);
        setChecked(ltheme == 'dark' ? true : false);
    }, []);

    const onChange = ({ target: { checked } }: BaseSyntheticEvent) => {
        if (checked) {
            setTheme('dark');
            setChecked(true);
        } else {
            setTheme('light');
            setChecked(false);
        }
    };

    if (theme != 'switcher') return null;

    return (
        <motion.label
            {...motions}
            className={`z-10 inline-flex cursor-pointer items-center ${className}`}
        >
            <input
                className="peer sr-only"
                checked={checked}
                onChange={onChange}
                type="checkbox"
            />
            <div
                className={clsx(
                    `peer-checked:after:rotate-360 peer overflow-hidden rounded-full bg-transparent opacity-75 shadow-lg outline-none ring-0 duration-150 before:absolute before:left-0 before:top-[calc(50%+4px)] before:flex before:-translate-y-1/2 before:items-center before:justify-center before:rounded-full before:bg-white/15 before:transition-all before:duration-150 before:content-['☀️'] after:absolute after:right-0 after:top-1 after:flex after:translate-y-full after:items-center after:justify-center after:rounded-full after:bg-white/15 after:opacity-0 after:transition-all after:duration-150 after:content-['🌙'] hover:opacity-100  peer-checked:before:-translate-y-full peer-checked:before:rotate-90 peer-checked:before:opacity-0 peer-checked:after:translate-y-0 peer-checked:after:opacity-100`,
                    {
                        'h-8 w-8': size == 8,
                        'after:h-8 after:w-8': size == 8,
                        'before:h-8 before:w-8': size == 8
                    }
                )}
            ></div>
        </motion.label>
    );
};
