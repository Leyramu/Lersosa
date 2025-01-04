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
 * @Date: 2024-06-12 19:52:57
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-29 15:40:01
 * @Description:
 */
'use client';
import { motion } from 'framer-motion';
import { AppConfig } from '@/config/config';
import { AppRules } from '@/lib/rules';
import { Form, FormObj } from '../ui/form/Form';
import { memo, useRef, useState } from 'react';
import { Button } from '../ui/button/Button';
import fetch from '@/lib/fetch';
import { useRouter } from 'next/navigation';
import message from '@/components/message';
import { Accordion, AccordionItem } from '@nextui-org/accordion';
import Link from 'next/link';

const MemoizedForm = memo(Form);

export const Settings = ({
                             config,
                             cardOpacity = 0.3
                         }: {
    config: AppConfig;
    cardOpacity?: number;
}) => {
    const [result, setResult] = useState<FormObj>(config);
    const [loading, setLoading] = useState(false);
    const fileInputRef = useRef<HTMLInputElement>(null);
    const router = useRouter();

    const onMerge = (data: FormObj, field?: string) => {
        setResult({
            ...result,
            ...(field ? { [field]: data } : data)
        });
    };

    const handleFileChange = (event: any) => {
        const file = event.target.files[0];
        if (!file) return;
        setLoading(true);
        const reader = new FileReader();
        reader.onload = (e: any) => {
            try {
                const config = JSON.parse(e.target.result);
                config && onSubmit(config);
            } catch (error) {
                setLoading(false);
                message.error('JSON 文件加载失败');
            }
        };
        reader.readAsText(file);
    };

    const onSubmit = (data?: FormObj) => {
        setLoading(true);
        fetch
            .post('/api/config', data || result)
            .then((res) => {
                if (res.success) {
                    message.success(data ? '上传成功，配置已更新~' : '保存成功~');
                    router.refresh();
                } else {
                    message.error(res.message || '操作失败！');
                }
            })
            .catch((_) => message.error('操作失败！'))
            .finally(() => setLoading(false));
    };

    return (
        <motion.div
            className="flex h-[85vh] w-[95vw] flex-col items-center gap-4 overflow-hidden rounded p-4 text-[hsl(var(--mio-foreground)/1)] shadow-mio-link backdrop-blur-lg md:w-[65vw]">
            <div className="relative w-full md:text-center">
                <h1 className="text-2xl font-bold">当前配置</h1>
                <div className="absolute right-2 top-2 flex w-full flex-row items-center justify-end gap-2">
                    <input
                        ref={fileInputRef}
                        onChange={handleFileChange}
                        type="file"
                        name="upload"
                        accept=".json"
                        hidden
                    />
                    <span
                        className="cursor-pointer underline"
                        onClick={() =>
                            fileInputRef?.current && fileInputRef.current.click()
                        }
                    >
            上传配置
          </span>
                    <Link className="underline" href={`/api/file`}>
                        下载配置
                    </Link>
                </div>
            </div>
            <Accordion
                showDivider
                className="mio-scroll flex w-full flex-col overflow-y-auto px-2"
                selectionMode="multiple"
            >
                {AppRules?.map(({ title, rules, field }) => {
                    const form = field ? (config as any)[field as any] : config;
                    return (
                        <AccordionItem
                            key={title}
                            aria-label={title}
                            title={
                                <span className="flex flex-nowrap items-center gap-2">
                  <span className="h-2 w-2 rounded-full bg-[var(--primary-color)]"></span>
                  <span className="font-semibold">{title}</span>
                </span>
                            }
                        >
                            <MemoizedForm
                                key={title}
                                title={title}
                                onMerge={(data: FormObj) => onMerge(data, field)}
                                rules={rules}
                                form={form}
                            />
                        </AccordionItem>
                    );
                })}
            </Accordion>
            <div>
                <Button
                    loading={loading}
                    className="rounded-2xl"
                    onClick={() => onSubmit()}
                >
                    保存
                </Button>
            </div>
        </motion.div>
    );
};
