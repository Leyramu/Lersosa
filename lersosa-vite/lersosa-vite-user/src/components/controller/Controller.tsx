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
 * @Date: 2024-08-17 10:47:06
 * @LastEditors: kasuie
 * @LastEditTime: 2024-08-18 14:35:35
 * @Description:
 */
'use client';
import { Setting } from '@kasuie/icon';
import { clsx } from '@kasuie/utils';
import { motion } from 'framer-motion';
import { ThemeSwitcher } from '../ui/switcher/ThemeSwitcher';
import dynamic from 'next/dynamic';

const MuteSwitcher = dynamic(
    async () =>
        (await import('@/components/ui/switcher/MuteSwitcher')).MuteSwitcher
);

export function Controller({
                               motions,
                               theme,
                               hasVideo,
                               handleMuteUnmute
                           }: {
    motions?: object;
    theme?: string;
    hasVideo?: boolean;
    handleMuteUnmute?: Function;
}) {
    const hidden = !hasVideo && theme != 'switcher';

    return (
        <motion.div
            {...motions}
            className={clsx(
                'group fixed bottom-6 right-6 z-10 flex cursor-pointer select-none flex-col items-center justify-center gap-2 whitespace-nowrap text-sm text-white/90',
                {
                    hidden: hidden
                }
            )}
        >
            <div className="hidden h-0 flex-col gap-2 duration-300 group-hover:flex group-hover:h-auto">
                <ThemeSwitcher theme={theme} />
                {hasVideo && <MuteSwitcher onSwitch={handleMuteUnmute} />}
            </div>
            <div className="px-2">
                <Setting size={22} />
            </div>
        </motion.div>
    );
}
