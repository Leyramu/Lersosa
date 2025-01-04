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
 * @Date: 2024-05-24 09:39:33
 * @LastEditors: kasuie
 * @LastEditTime: 2024-08-16 16:27:34
 * @Description:
 */
'use client';
import { isClientSide } from '@kasuie/utils';
import { ThemeProvider } from 'next-themes';
import { useRouter } from 'next/navigation';
import { NextUIProvider } from '@nextui-org/system';
import { AppConfig } from '@/config/config';
import StyledRegistry from './StyleJsxProvider';
import { createContext } from 'react';
import Script from 'next/script';

export const ConfigProvider = createContext({});

export function AppProviders({
                                 appConfig,
                                 children,
                                 ver
                             }: Readonly<{
    appConfig?: AppConfig;
    children: React.ReactNode;
    ver?: string;
}>) {
    const router = useRouter();

    const { js, css } = appConfig?.resources || {};

    if (isClientSide) {
        console.log(
            `\n %c Remio-home${
                ver ? ' v' + ver : ''
            } By kasuie %c https://github.com/kasuie`,
            'color:#555;background:linear-gradient(to right, #a8edea 0%, #fed6e3 100%);padding:5px 0;',
            'color:#fff;background:#fff;padding:5px 10px 5px 0px;'
        );
    }

    return (
        <NextUIProvider navigate={router.push}>
            <ThemeProvider
                attribute="class"
                key="themeProvider"
                defaultTheme="light"
                enableSystem
            >
                {css?.length
                    ? css?.map((v: string) => <link rel="stylesheet" key={v} href={v} />)
                    : null}
                {js?.length
                    ? js?.map((v: string) => (
                        <Script key={v} src={v} strategy={'afterInteractive'} />
                    ))
                    : null}
                <ConfigProvider.Provider value={{ appConfig: appConfig }}>
                    <StyledRegistry>{children}</StyledRegistry>
                </ConfigProvider.Provider>
            </ThemeProvider>
        </NextUIProvider>
    );
}
