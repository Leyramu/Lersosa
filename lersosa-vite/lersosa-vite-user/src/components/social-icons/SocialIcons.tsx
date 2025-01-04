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
 * @Date: 2024-05-22 15:54:06
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-28 10:14:15
 * @Description:
 */
'use client';
import { Bilibili, Discord, Email, Github, Instargram, QQ, Steam, SvgProps, Telegram, Twitter, X } from '@kasuie/icon';
import Link from 'next/link';
import { Avatar } from '../ui/image/Avatar';
import { clsx, isValidUrl } from '@kasuie/utils';
import { motion } from 'framer-motion';
import { Link as LinkType } from '@/config/config';
import { ATransition } from '../ui/transition/ATransition';

export const SocialIcons = ({
                                links,
                                autoAnimate = true,
                                ripple = true,
                                loading = false,
                                motions = {},
                                initialDelay = 0,
                                warpClass = ''
                            }: {
    links: Array<LinkType>;
    autoAnimate?: boolean;
    ripple?: boolean;
    loading?: boolean | string;
    motions?: any;
    initialDelay?: number;
    warpClass?: string;
}) => {
    const IconsMap: any = {
        github: (props: SvgProps) => <Github {...props} />,
        twitter: (props: SvgProps) => <Twitter {...props} />,
        qq: (props: SvgProps) => <QQ {...props} />,
        telegram: (props: SvgProps) => <Telegram {...props} />,
        email: (props: SvgProps) => <Email {...props} />,
        steam: (props: SvgProps) => <Steam {...props} />,
        bilibili: (props: SvgProps) => <Bilibili {...props} />,
        discord: (props: SvgProps) => <Discord {...props} />,
        instargram: (props: SvgProps) => <Instargram {...props} />,
        x: (props: SvgProps) => <X {...props} />
    };

    const renderIcon = (key: any, color?: string, size: number = 16) => {
        if (key && Object.keys(IconsMap).includes(key)) {
            return IconsMap[key]({ color, size });
        }
        return null;
    };

    return (
        <motion.div
            className={clsx('z-[1] px-4 md:px-0', {
                [`${warpClass}`]: warpClass
            })}
            {...motions}
        >
            <ul className="relative m-0 flex flex-wrap justify-center gap-6 md:gap-9">
                {links?.map((v: LinkType, index: number) => {
                    return (
                        <ATransition
                            key={index}
                            animate={loading == 'wave'}
                            delay={index * 100 + initialDelay * 1000 + 300}
                            className="inline-block"
                            as="li"
                        >
                            <Link
                                key={index}
                                href={v?.url || ''}
                                target="_blank"
                                className={clsx(
                                    'group relative flex h-9 w-9 flex-shrink-0 flex-grow-0 cursor-pointer items-center justify-center rounded-full bg-white/60 text-center text-[#3f345f] shadow-[0_5px_25px_#5d46e826] duration-500 ease-linear before:absolute before:left-[-8px] before:top-[-8px] before:h-[calc(100%+16px)] before:w-[calc(100%+16px)] before:rounded-full before:border before:border-[#ffffff8c] before:opacity-0 hover:animate-[move_0.9s_both] hover:before:animate-[1.5s_linear_0s_normal_none_infinite_focuse] dark:bg-transparent',
                                    {
                                        'before:animate-[1.5s_linear_0s_normal_none_infinite_focuse]':
                                            autoAnimate && ripple
                                    }
                                )}
                            >
                                {v.title && (
                                    <span
                                        className="mio-tooltip group-hover:pointer-events-auto group-hover:visible group-hover:bottom-[-50px] group-hover:opacity-100">
                    {v.title}
                  </span>
                                )}
                                {v.icon && isValidUrl(v.icon) ? (
                                    <Avatar
                                        alt={v.title}
                                        src={v.icon}
                                        width={18}
                                        height={18}
                                        className={'rounded-full'}
                                    />
                                ) : (
                                    renderIcon(v.icon, v.color)
                                )}
                            </Link>
                        </ATransition>
                    );
                })}
            </ul>
        </motion.div>
    );
};
