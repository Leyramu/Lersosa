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
 * @Date: 2024-05-30 21:28:45
 * @LastEditors: kasuie
 * @LastEditTime: 2024-07-05 16:00:18
 * @Description:
 */
'use client';
import { FooterConfig } from '@/config/config';
import { ExternalLink } from '@kasuie/icon';
import { clsx } from '@kasuie/utils';
import { motion } from 'framer-motion';
import Link from 'next/link';

export function Footer({
                           footer,
                           motions
                       }: {
    footer?: string | FooterConfig;
    motions?: object;
}) {
    return (
        <motion.footer
            {...motions}
            className="absolute bottom-2 left-1/2 z-10 !translate-x-[-50%] cursor-pointer select-none whitespace-nowrap text-sm text-white/90"
        >
            {typeof footer === 'string' ? (
                footer
            ) : (
                <div
                    className={clsx('flex flex-col items-center gap-1', {
                        '!flex-row !gap-4': footer?.direction == 'row',
                        '!flex-row-reverse !gap-4': footer?.direction == 'row-reverse',
                        '!flex-col-reverse': footer?.direction == 'col-reverse'
                    })}
                >
                    {footer?.url ? (
                        <Link
                            href={footer.url}
                            className="flex flex-nowrap items-center gap-1"
                        >
                            <span>{footer.text}</span>
                            {footer?.isExternal && <ExternalLink size={12} />}
                        </Link>
                    ) : (
                        <span>{footer?.text}</span>
                    )}
                    {footer?.ICP && (
                        <Link
                            href={'https://beian.miit.gov.cn'}
                            className="flex flex-nowrap items-center gap-1"
                        >
                            <span>{footer.ICP}</span>
                        </Link>
                    )}
                </div>
            )}
        </motion.footer>
    );
}
