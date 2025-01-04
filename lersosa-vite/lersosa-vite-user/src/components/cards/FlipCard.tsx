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
 * @Date: 2024-06-05 10:09:09
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-29 16:46:59
 * @Description:
 */
import { Site } from '@/config/config';
import { clsx } from '@kasuie/utils';
import { Avatar } from '../ui/image/Avatar';
import { DotsHorizontal, ExternalLink } from '@kasuie/icon';
import { CSSProperties, ReactNode } from 'react';

export function FlipCard({
                             animate,
                             style = {},
                             outer,
                             data,
                             direction = 'col',
                             hoverFlip = true,
                             children,
                             flip,
                             warpClass,
                             contentClass,
                             className
                         }: {
    data?: Site;
    style?: CSSProperties | undefined;
    outer?: boolean;
    animate?: boolean;
    direction?: string;
    hoverFlip?: boolean;
    children?: ReactNode;
    flip?: boolean;
    warpClass?: string;
    contentClass?: string;
    className?: string;
}) {
    const renderWrap = (children: ReactNode) => {
        return (
            <div
                className={clsx('group/flip z-[1] overflow-visible', {
                    'h-[100px] min-w-48': !flip,
                    [`${warpClass}`]: warpClass
                })}
            >
                <div
                    className={clsx(
                        'transform-preserve-3d relative text-white h-full w-full rounded-2xl shadow-mio-link transition-transform duration-500',
                        {
                            'group-hover/flip:rotate-y-180': outer && hoverFlip,
                            [`${contentClass}`]: contentClass
                        }
                    )}
                >
                    {flip ? (
                        <div
                            className="backface-hidden absolute h-full w-full overflow-hidden rounded-2xl duration-300">
                            <div
                                className={clsx(
                                    'relative z-[1] flex h-full w-full items-center justify-center',
                                    {
                                        [`${className}`]: className
                                    }
                                )}
                            >
                                {children}
                            </div>
                            <div className="absolute top-0 h-full w-full object-cover object-center">
                                <div
                                    className="absolute left-0 top-0 h-20 w-20 animate-[mio-floating_4000ms_infinite_linear] rounded-full bg-[var(--primary-color)] blur-md"></div>
                                <div
                                    className="mio-delay-1800 absolute right-0 top-0 h-12 w-12 animate-[mio-floating_3000ms_infinite_linear] rounded-full bg-[#ff2233] blur-md"></div>
                                <div
                                    className="mio-delay-800 absolute bottom-0 right-0 h-8 w-8 animate-[mio-floating_2600ms_infinite_linear] rounded-full bg-[#ff8866] blur-md"></div>
                            </div>
                        </div>
                    ) : (
                        children
                    )}
                    {outer && hoverFlip && (
                        <div
                            style={style}
                            className={clsx(
                                'backface-hidden group-active/flip:mio-flip-active absolute h-full w-full overflow-hidden rounded-2xl duration-300',
                                {
                                    'rotate-y-180 backdrop-blur': outer
                                }
                            )}
                        >
                            <div className="flex h-[calc(100%-2px)] w-[calc(100%-2px)] items-center justify-center">
                                <span>{data?.desc}</span>
                                <span className="absolute bottom-[5px] right-[7px]">
                  {data?.url ? (
                      <ExternalLink size={14} />
                  ) : (
                      <DotsHorizontal size={14} />
                  )}
                </span>
                            </div>
                            {outer && (
                                <div className="absolute top-0 h-full w-full object-cover object-center">
                                    <div
                                        className="absolute left-0 top-0 h-16 w-16 animate-[mio-floating_2600ms_infinite_linear] rounded-full bg-[#ffbb66] blur-md"></div>
                                    <div
                                        className="mio-delay-1800 absolute right-0 top-0 h-12 w-12 animate-[mio-floating_2600ms_infinite_linear] rounded-full bg-[#ff2233] blur-md"></div>
                                    <div
                                        className="mio-delay-800 absolute bottom-0 right-0 h-6 w-6 animate-[mio-floating_2600ms_infinite_linear] rounded-full bg-[#ff8866] blur-md"></div>
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </div>
        );
    };

    if (flip) {
        return renderWrap(children);
    }

    return renderWrap(
        <div
            style={style}
            className={clsx(
                'backface-hidden absolute z-[1] flex h-full w-full items-center justify-center overflow-hidden rounded-2xl backdrop-blur'
            )}
        >
            <div
                className={clsx(
                    'absolute flex h-[calc(100%-2px)] w-[calc(100%-2px)] items-center justify-center gap-2',
                    {
                        'flex-row': direction == 'row',
                        'flex-col': direction != 'row'
                    }
                )}
            >
                <Avatar
                    warpClass="rounded-full overflow-hidden"
                    width={30}
                    height={30}
                    src={data?.icon || ''}
                    alt={data?.title || ''}
                />
                <span>{data?.title}</span>
                {(!outer || !hoverFlip) && (
                    <span className="absolute bottom-[5px] right-[7px]">
            {data?.url ? (
                <ExternalLink size={14} />
            ) : (
                <DotsHorizontal size={14} />
            )}
          </span>
                )}
            </div>
        </div>
    );
}
