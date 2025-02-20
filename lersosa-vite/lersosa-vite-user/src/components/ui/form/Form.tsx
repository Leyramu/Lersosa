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
 * @Date: 2024-06-13 11:03:00
 * @LastEditors: kasuie
 * @LastEditTime: 2024-07-05 17:51:44
 * @Description:
 */
'use client';
import { useEffect, useState } from 'react';
import { Input } from '../input/Input';
import { Radio } from '../radio/Radio';
import { Select } from '../select/Select';
import { Checkbox } from '../checkbox/Checkbox';
import { RuleItem } from '@/lib/rules';
import { FormList } from './FormList';
import { Textarea } from '../textarea/Textarea';

export interface FormObj {
    [key: string]: any;
}

export const Form = ({
                         className = '',
                         title,
                         rules,
                         form,
                         controlProps = {
                             size: 'sm',
                             variant: 'underlined',
                             color: 'primary',
                             classNames: {
                                 label: 'text-[hsl(var(--mio-foreground)/0.8)]',
                                 description: 'text-[hsl(var(--mio-foreground)/0.6)] indent-1',
                                 popoverContent: 'bg-black/80',
                                 listbox: 'text-white/90',
                                 value: 'text-[hsl(var(--mio-primary)/1)]',
                                 input: 'text-[hsl(var(--mio-primary)/1)]'
                             }
                         },
                         transform = true,
                         onMerge
                     }: {
    className?: string;
    title?: string;
    rules?: Array<RuleItem>;
    form?: FormObj;
    controlProps?: FormObj;
    transform?: boolean;
    onMerge?: Function;
}) => {
    const [formData, setFormData] = useState<FormObj>();
    const [resetData, setResetData] = useState<FormObj>();
    const [hasBooleans, setHasBooleans] = useState<Boolean>(false);

    useEffect(() => {
        if (rules?.length && form) {
            const object = rules.reduce((prev: FormObj, curr) => {
                let value =
                    typeof form[curr.field] != 'undefined' && form[curr.field] != ''
                        ? form[curr.field]
                        : curr.default;
                if (curr.field.includes('$boolean')) {
                    setHasBooleans(true);
                    const booleans: any = curr.items?.reduce((iprev, icurr) => {
                        const ivalue = form[icurr.value];
                        if (ivalue) {
                            if (Array.isArray(value)) {
                                !value.includes(icurr.value) && value.push(icurr.value);
                            } else {
                                value = [icurr.value];
                            }
                        } else if (typeof ivalue === 'undefined') {
                            return {
                                ...iprev,
                                [icurr.value]: value?.includes(icurr.value) || false
                            };
                        } else if (Array.isArray(value)) {
                            value = value.filter((v) => v != icurr.value);
                        }
                        return { ...iprev, [icurr.value]: ivalue };
                    }, {});
                    return {
                        ...prev,
                        ...booleans,
                        $fields:
                            prev && prev['$fields']
                                ? [...prev['$fields'], ...Object.keys(booleans)]
                                : Object.keys(booleans),
                        [curr.field]: value
                    };
                } else {
                    return {
                        ...prev,
                        [curr.field]: value || null
                    };
                }
            }, {});
            setFormData(object);
            setResetData(object);
        } else if (form) {
            setFormData(form);
            setResetData(form);
        }
    }, [rules, form]);

    useEffect(() => {
        const result = Object.assign({}, formData);
        if (formData) {
            if (transform && hasBooleans) {
                const fields = result['$fields'];
                delete result['$fields'];
                Object.keys(result)?.forEach((key) => {
                    if (key.includes('$boolean') && fields) {
                        fields?.map((v: string) => {
                            result[v] = result[key]?.includes(v) || false;
                        });
                        delete result[key];
                    }
                });
            }
            onMerge?.(result);
        }
    }, [formData]);

    return (
        <div className="flex flex-wrap justify-between gap-y-4">
            {formData &&
                rules?.map(
                    (
                        {
                            field,
                            transform,
                            controlKey,
                            items,
                            desc,
                            controlProps: _props,
                            ...others
                        },
                        index
                    ) => {
                        const props = {
                            ...others,
                            ...controlProps,
                            ..._props,
                            description: desc,
                            className: 'md:mio-col-2-full'
                        };
                        switch (controlKey) {
                            case 'select':
                                return (
                                    <Select
                                        {...props}
                                        key={field}
                                        selectedKeys={formData[field] ? [formData[field]] : []}
                                        items={items}
                                        onSelectionChange={(val: any) => {
                                            if (!val || !val['currentKey']) return;
                                            setFormData({
                                                ...formData,
                                                [field]: val['currentKey']
                                            });
                                        }}
                                    />
                                );
                            case 'checkbox':
                                const value = formData[field] || [];
                                return (
                                    <Checkbox
                                        {...props}
                                        key={field}
                                        value={value}
                                        items={items}
                                        onValueChange={(val: string[]) => {
                                            setFormData({
                                                ...formData,
                                                [field]: val
                                            });
                                        }}
                                    />
                                );
                            case 'radio':
                                return (
                                    <Radio
                                        key={field}
                                        value={formData[field]}
                                        items={items}
                                        {...props}
                                        onValueChange={(val: string) => {
                                            setFormData({
                                                ...formData,
                                                [field]: val
                                            });
                                        }}
                                    />
                                );
                            case 'list':
                                return (
                                    <FormList
                                        key={field}
                                        title={props.label}
                                        controlProps={{
                                            ...controlProps,
                                            ..._props
                                        }}
                                        rules={items}
                                        data={formData[field]}
                                        onProxy={(key: string, index?: number) => {
                                            if (key === 'del' && typeof index != 'undefined') {
                                                const newFormData = [...formData[field]];
                                                newFormData.splice(index, 1);
                                                setFormData({
                                                    ...formData,
                                                    [field]: newFormData
                                                });
                                            } else if (key === 'add') {
                                                const newFormData = formData[field]
                                                    ? [...formData[field], {}]
                                                    : [{}];
                                                setFormData({
                                                    ...formData,
                                                    [field]: newFormData
                                                });
                                            }
                                        }}
                                        onChange={(
                                            index: number,
                                            colField: string,
                                            value: string
                                        ) => {
                                            const newFormData = [...formData[field]];
                                            newFormData[index][colField] = value;
                                            setFormData({
                                                ...formData,
                                                [field]: newFormData
                                            });
                                        }}
                                    />
                                );
                            case 'textarea':
                                return (
                                    <Textarea
                                        key={field}
                                        value={formData[field] || ''}
                                        {...props}
                                        onValueChange={(val: string) => {
                                            setFormData({
                                                ...formData,
                                                [field]: val
                                            });
                                        }}
                                    />
                                );
                            default:
                                return (
                                    <Input
                                        key={field}
                                        value={
                                            transform
                                                ? transform(formData[field], true) || ''
                                                : formData[field] || ''
                                        }
                                        {...props}
                                        onValueChange={(val: string) => {
                                            const data = transform ? transform(val) : val;
                                            setFormData({
                                                ...formData,
                                                [field]: props?.type == 'number' ? +data : data
                                            });
                                        }}
                                    />
                                );
                        }
                    }
                )}
        </div>
    );
};
