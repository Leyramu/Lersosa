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
 * @Date: 2024-06-12 19:46:02
 * @LastEditors: kasuie
 * @LastEditTime: 2024-08-18 15:27:56
 * @Description:
 */
import { MainEffect } from '@/components/effect/MainEffect';
import { Settings } from '@/components/settings/Settings';
import { Loader } from '@/components/ui/loader/Loader';
import { Verify } from '@/components/verify/Verify';
import { getConfig, mergeConfig, transformConfig } from '@/lib/config';
import { toHsl } from '@kasuie/utils';
import { Suspense } from 'react';

export const revalidate = 0;

export default async function Config({ searchParams }: {
    searchParams: { [key: string]: string | string[] | undefined };
}) {
    const verify = Object.hasOwn(searchParams, 'verify');

    const appConfig = await getConfig('config.json');

    const { bgConfig, primaryColor } = transformConfig(appConfig);

    const style: any = {
        '--mio-foreground': '210 5.56% 92.94%',
        '--primary-color': primaryColor,
        '--mio-primary': toHsl(primaryColor)
    };

    return (
        <Suspense
            fallback={
                <Loader warpClass="h-screen bg-black" miao>
                    ᓚᘏᗢ猫猫正在努力加载...
                </Loader>
            }
        >
            <div
                style={style}
                className="relative z-[1] flex h-screen w-full items-center justify-center"
            >
                {!verify ? <Settings config={mergeConfig(appConfig)} /> : <Verify />}
            </div>
            <MainEffect
                bgArr={bgConfig.bgs}
                mbgArr={bgConfig.mbgs}
                bgStyle={appConfig.bgConfig?.bgStyle}
                blur={appConfig.bgConfig?.blur || 'sm'}
            />
        </Suspense>
    );
}
