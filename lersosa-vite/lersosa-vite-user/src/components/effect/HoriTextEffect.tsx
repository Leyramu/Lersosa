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
 * @Date: 2024-06-11 15:49:09
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-21 21:57:20
 * @Description:
 */
'use client';

import { useEffect, useState } from 'react';
import { TextUpView } from '../ui/transition/TextUpView';
import { SubTitleConfig } from '@/config/config';
import { clsx } from '@kasuie/utils';

interface Item {
    text: string;
    rootAs: string | any;
    length?: number;
    initialDelay?: number;
}

export function HoriTextEffect({
                                   content,
                                   desc,
                                   gapDelay = 0.05,
                                   loading
                               }: SubTitleConfig) {
    const [h1s, setH1s] = useState<Item[]>();

    const [ps, setPs] = useState<Item[]>();

    const [data, setData] = useState<Item[]>();

    useEffect(() => {
        if (content) {
            const arr: Item[] =
                content?.split(';').map((v: string) => {
                    return {
                        text: v,
                        rootAs: 'h1',
                        length: v.length
                    };
                }) || [];
            if (arr?.length) setH1s(arr);
        }
    }, [content]);

    useEffect(() => {
        if (desc) {
            const arr: Item[] =
                desc?.split(';').map((v: string) => {
                    return {
                        text: v,
                        rootAs: 'p',
                        length: v.length
                    };
                }) || [];
            if (arr?.length) setPs(arr);
        }
    }, [desc]);

    useEffect(() => {
        const temp = h1s?.concat(ps || []) || [];
        temp.reduce((prev: number, curr: Item, index: number) => {
            temp[index].initialDelay = prev * gapDelay;
            return (curr?.length || 0) + prev;
        }, 0);
        setData(temp);
    }, [h1s, ps]);

    return (
        <div className="max-w-2xl z-[1] text-white">
            {data?.map((v: Item, i: number) => {
                return (
                    <TextUpView
                        rootAs={v.rootAs}
                        className={clsx('', {
                            'mb-6 text-2xl leading-relaxed md:text-3xl md:leading-loose': v.rootAs == 'h1',
                            'text-sm opacity-80 leading-relaxed': v.rootAs == 'p'
                        })}
                        key={v.text}
                        as="span"
                        appear={!!loading && !!gapDelay}
                        initialDelay={v.initialDelay}
                        eachDelay={gapDelay}
                    >
                        {v.text}
                    </TextUpView>
                );
            })}
        </div>
    );
}
