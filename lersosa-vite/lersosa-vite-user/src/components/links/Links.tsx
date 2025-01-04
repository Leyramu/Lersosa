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
 * @Date: 2024-05-22 19:32:38
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-24 21:51:34
 * @Description:
 */
'use client';
import { Image } from '@/components/ui/image/Image';
import useModal from '@/components/ui/modal/useModal';
import { Modal } from '@/components/ui/modal/Modal';
import Link from 'next/link';
import { DotsHorizontal, ExternalLink } from '@kasuie/icon';
import { clsx } from '@kasuie/utils';
import { Site, SitesConfig } from '@/config/config';
import { motion } from 'framer-motion';
import dynamic from 'next/dynamic';
import { CSSProperties } from 'react';

const FlipCard = dynamic(
    async () => (await import('../cards/FlipCard')).FlipCard
);

export function Links({
                          staticSites,
                          modalSites,
                          primaryColor,
                          cardOpacity,
                          sitesConfig = {
                              hoverScale: true,
                              hoverBlur: true,
                              modal: true
                          },
                          motions = {}
                      }: {
    primaryColor?: string;
    cardOpacity?: number;
    staticSites: Array<Site>;
    modalSites: Array<Site>;
    sitesConfig?: SitesConfig;
    motions?: object;
}) {
    const { isVisible, openModal, closeModal } = useModal();

    const itemContent = (item: Site, outer: boolean = true) => {
        const style: CSSProperties = outer
            ? {
                backgroundColor: `rgba(var(--mio-main), ${cardOpacity})`
            }
            : {};

        if (sitesConfig.cardStyle == 'flip') {
            return (
                <FlipCard
                    data={item}
                    outer={outer}
                    style={style}
                    direction={sitesConfig?.direction}
                    hoverFlip={sitesConfig?.hoverFlip}
                />
            );
        }

        const className = clsx(
            'group/main relative shadow-mio-link z-[1] flex h-[90px] flex-row flex-nowrap items-center gap-[10px] overflow-hidden rounded-2xl bg-black/10 p-[10px_15px] duration-500 hover:z-10 hover:border-transparent hover:!blur-none',
            {
                'hover:!scale-110 backdrop-blur-[7px]': outer, // hover:bg-[#229fff]
                'group-hover/links:scale-90': sitesConfig.hoverScale,
                'group-hover/links:blur-[1px]': sitesConfig.hoverBlur
                // "mx-2": outer,
            }
        );

        return (
            <div style={style} className={className}>
                {/* {animate && (
          <div className="absolute left-[20px] right-0 top-24 z-[-1] h-[25rem] w-[25rem] rotate-[-36deg] rounded-full bg-[#3651cf26] duration-500 group-hover/main:left-[-20px] group-hover/main:top-[-20px]"></div>
        )} */}
                {item.icon && (
                    <div className="p-[5px]">
                        <Image
                            alt={item.title}
                            src={item.icon}
                            width={42}
                            height={42}
                            style={{
                                borderRadius: '50%'
                            }}
                        ></Image>
                    </div>
                )}
                <div className="p-[5px]">
                    {item.title && <p className="text-white">{item.title}</p>}
                    {item.desc && (
                        <p className="pt-[10px] text-[15px] text-white/70">{item.desc}</p>
                    )}
                </div>
                <span className="absolute bottom-[5px] right-[7px] text-white/70">
          {item?.url ? (
              <ExternalLink size={14} />
          ) : (
              <DotsHorizontal size={14} />
          )}
        </span>
            </div>
        );
    };

    const linkItem = (item: Site, key: number, outer: boolean = true) => {
        return (
            <div
                key={key}
                title={item.title}
                className={clsx(
                    'flex min-w-[215px] basis-11/12 cursor-pointer flex-col justify-center',
                    {
                        'sm:mio-col-2 md:mio-col-2 lg:mio-col-3 xl:mio-col-4': outer,
                        'sm:mio-col-2 basis-full': !outer
                    }
                )}
            >
                {item?.url ? (
                    <Link href={item.url} className="h-full w-full" target="_blank">
                        {itemContent(item, outer)}
                    </Link>
                ) : (
                    <div
                        onClick={() => sitesConfig?.modal && openModal()}
                        className="h-full w-full"
                    >
                        {itemContent(item)}
                    </div>
                )}
            </div>
        );
    };

    return (
        <motion.div
            {...motions}
            className="group/links z-[1] mt-3 flex w-[95vw] flex-wrap justify-evenly gap-x-4 gap-y-6 md:mt-8 md:w-[65vw]"
        >
            {staticSites.map((v, index) => linkItem(v, index))}
            {sitesConfig?.modal && modalSites?.length ? (
                <Modal
                    className="w-[650px]"
                    visible={isVisible}
                    title={sitesConfig?.modalTitle}
                    closeModal={closeModal}
                >
                    {sitesConfig?.modalTips && (
                        <div
                            className="relative pb-2 indent-5 text-sm text-white/90 before:absolute before:left-[7px] before:top-2 before:h-1 before:w-1 before:rounded-full before:bg-[#229fff] before:content-[''] after:absolute after:left-[5px] after:top-[6px] after:h-2 after:w-2 after:rounded-full after:border after:border-[#229fff]">
                            {sitesConfig.modalTips}
                        </div>
                    )}
                    <div className="flex flex-wrap justify-between gap-y-6">
                        {modalSites.map((v, index) => linkItem(v, index, false))}
                    </div>
                </Modal>
            ) : null}
        </motion.div>
    );
}
