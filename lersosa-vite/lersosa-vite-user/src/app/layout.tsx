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
 * @Date: 2024-05-20 16:08:41
 * @LastEditors: kasuie
 * @LastEditTime: 2024-08-16 15:44:12
 * @Description:
 */
import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import { Layout } from '@/components/layout/Layout';
import { AppProviders } from '@/providers';
import { getConfig } from '@/lib/config';
import { GoogleAnalytics, GoogleTagManager } from '@next/third-parties/google';
import Script from 'next/script';
import '@/styles/index.css';
import StyleRegistry from '@/components/layout/StyleRegistry';

const inter = Inter({ subsets: ['latin'] });

export async function generateMetadata() {
    const appConfig = await getConfig('config.json');

    return {
        title: appConfig.name,
        description: appConfig.description,
        keywords: appConfig.keywords,
        manifest: '/api/manifest',
        icons: {
            icon: appConfig.favicon || '/favicon.ico',
            shortcut: '/icons/favicon192.png',
            apple: '/icons/favicon192.png'
        },
        other: { 'baidu-site-verification': process.env.BaiduSiteVerify || '' }
    } satisfies Metadata;
}

export default async function RootLayout({
                                             children
                                         }: Readonly<{
    children: React.ReactNode;
}>) {
    const appConfig = await getConfig('config.json');
    return (
        <html lang="zh-CN" suppressHydrationWarning>
        <body
            className={`${inter.className} mio-scroll mio-fonts overflow-y-auto`}
        >
        <AppProviders
            appConfig={appConfig}
            ver={process.env.VERSION || ''}
        >
            <Layout>{children}</Layout>
            <StyleRegistry />
        </AppProviders>
        {process.env.GTAGID && <GoogleAnalytics gaId={process.env.GTAGID} />}
        {process.env.GTMID && <GoogleTagManager gtmId={process.env.GTMID} />}
        {process.env.BAIDUID && (
            <Script
                strategy={'afterInteractive'}
                src={`https://hm.baidu.com/hm.js?${process.env.BAIDUID}`}
            />
        )}
        </body>
        </html>
    );
}
