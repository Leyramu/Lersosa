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
 * @LastEditTime: 2024-08-18 14:34:34
 * @Description:
 */
import { Loader } from '@/components/ui/loader/Loader';
import { Suspense } from 'react';
import { getConfig, transformConfig } from '@/lib/config';
import { MainEffect } from '@/components/effect/MainEffect';
import { getMotion } from '@/lib/motion';
import { Footer } from '@/components/layout/Footer';
import dynamic from 'next/dynamic';
import { Weather } from '@/components/weather/Weather';

export const revalidate = 0;

const Horizontal = dynamic(
    async () => (await import('@/components/content/Horizontal')).Horizontal
);

const Vertical = dynamic(
    async () => (await import('@/components/content/Vertical')).Vertical
);

export default async function Home() {
    const {
        staticSites,
        modalSites,
        varStyle,
        istTransition,
        gapSize,
        style,
        bgConfig,
        footer,
        globalStyle,
        resources,
        ...others
    } = transformConfig(await getConfig('config.json'));

    const { bodyHtml } = resources || {};

    const renderMain = (props: any) => {
        if (style === 'horizontal') {
            return <Horizontal {...props} />;
        } else {
            return <Vertical {...props} />;
        }
    };

    return (
        <Suspense
            fallback={
                <Loader warpClass="h-screen bg-black" miao>
                    ᓚᘏᗢ猫猫正在努力加载...
                </Loader>
            }
        >
            {globalStyle?.weather && <Weather size={18} />}
            {renderMain({
                ...others,
                gapSize,
                istTransition,
                staticSites,
                modalSites,
                style: varStyle
            })}
            <MainEffect
                bgArr={bgConfig.bgs}
                mbgArr={bgConfig.mbgs}
                bgStyle={bgConfig?.bgStyle}
                blur={bgConfig?.blur || 'sm'}
                theme={globalStyle?.theme}
                motions={getMotion(0.1, 4, 0.2, istTransition)}
            />
            {footer ? (
                <Footer
                    motions={getMotion(0.1, 4, 0.2, istTransition)}
                    footer={footer}
                />
            ) : null}
            {bodyHtml && (
                <section
                    id="remio-bodyHtml"
                    className="relative z-20"
                    dangerouslySetInnerHTML={{ __html: bodyHtml }}
                ></section>
            )}
        </Suspense>
    );
}
