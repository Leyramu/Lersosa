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
 * @Date: 2024-06-15 10:30:25
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-19 15:45:09
 * @Description:
 */
'use client';
import { FlipCard } from '../cards/FlipCard';
import { Input } from '../ui/input/Input';
import { Button } from '../ui/button/Button';
import { useState } from 'react';
import { Encrypt } from '@/lib/utils';
import { useRouter } from 'next/navigation';
import message from '../message';
import { LockClosed } from '@kasuie/icon';
import fetch from '@/lib/fetch';

export const Verify = () => {
    const [checkCode, setCheckCode] = useState<string>();
    const [loading, setLoading] = useState(false);
    const router = useRouter();
    return (
        <FlipCard
            flip
            className="flex-col gap-8 p-1 md:px-20 md:py-4"
            contentClass="backdrop-blur-md"
            warpClass="w-[96%] md:w-[750px] h-[350px]"
        >
            <Input
                label="Password"
                isClearable
                radius="lg"
                size="lg"
                onValueChange={setCheckCode}
                autoComplete="off"
                type="password"
                classNames={{
                    label: '!text-[hsl(var(--mio-primary)/1)]',
                    input: [
                        'bg-transparent',
                        'text-white/90',
                        'placeholder:text-white/70',
                        'group-data-[has-value=true]:text-white/90'
                    ],
                    clearButton: 'text-white/70',
                    innerWrapper: 'bg-transparent',
                    inputWrapper: [
                        '!bg-black/40',
                        '!hover:bg-black/70',
                        '!data-[hover=true]:bg-black/50',
                        '!group-data-[focus=true]:bg-black/40',
                        '!cursor-text'
                    ]
                }}
                placeholder="输入密码以继续..."
                startContent={
                    <LockClosed className="pointer-events-none mb-[3px] flex-shrink-0 text-white/90" />
                }
            />
            <Button
                loading={loading}
                className="mt-8 rounded-2xl text-white"
                onClick={() => {
                    if (!checkCode) return message.warning('密码不能为空~');
                    setLoading(true);
                    fetch.post('/api/verify', {
                        checkCode: Encrypt(checkCode)
                    }).then((res) => {
                        if (res.success) {
                            message.success('操作成功');
                            router.refresh();
                        } else {
                            message.error('操作失败');
                        }
                    })
                        .catch(_ => message.error('操作失败！'))
                        .finally(() => setLoading(false));
                }}
            >
                提交
            </Button>
        </FlipCard>
    );
};
