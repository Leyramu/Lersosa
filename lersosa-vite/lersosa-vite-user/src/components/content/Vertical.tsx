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
 * @Date: 2024-05-31 13:22:52
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-28 09:34:48
 * @Description:
 */
import { HTMLAttributes } from 'react';
import { clsx } from '@kasuie/utils';
import { Avatar } from '../ui/image/Avatar';
import { getMotion } from '@/lib/motion';
import { AvatarConfig, Link, Site, SitesConfig, SlidersConfig, SocialConfig, SubTitleConfig } from '@/config/config';
import { TextEffect } from '../effect/TextEffect';
import { SocialIcons } from '../social-icons/SocialIcons';
import { Links } from '../links/Links';
import { Sliders } from '../sliders/Sliders';

interface VerticalProps extends HTMLAttributes<HTMLDivElement> {
    gapSize: string;
    name: string;
    avatarConfig?: AvatarConfig;
    subTitleConfig?: SubTitleConfig;
    sitesConfig?: SitesConfig;
    socialConfig?: SocialConfig;
    istTransition: boolean;
    links: Link[];
    staticSites: Site[];
    modalSites: Site[];
    primaryColor?: string;
    subTitle?: string;
    sliders?: SlidersConfig;
    cardOpacity?: number;
}

export function Vertical({
                             gapSize,
                             name,
                             avatarConfig,
                             subTitleConfig,
                             sitesConfig,
                             socialConfig,
                             istTransition,
                             links,
                             staticSites,
                             modalSites,
                             primaryColor,
                             subTitle,
                             sliders,
                             cardOpacity = 0.1,
                             className,
                             ...others
                         }: VerticalProps) {
    return (
        <div
            className={clsx(
                'relative z-[1] flex w-full flex-col items-center justify-center pb-16',
                {
                    'gap-8 pt-[20vh]': gapSize == 'sm',
                    'gap-10 pt-[18vh]': gapSize == 'md',
                    'gap-12 pt-[15vh]': gapSize == 'lg',
                    [`${className}`]: className
                }
            )}
            {...others}
        >
            {!avatarConfig?.hidden && (
                <Avatar
                    priority
                    isShowMotion
                    alt={name}
                    src={avatarConfig?.src || ''}
                    motions={getMotion(0.1, 0, 0, istTransition)}
                    animateStyle={avatarConfig?.style}
                    {...avatarConfig}
                    style={''}
                />
            )}
            <TextEffect
                {...subTitleConfig}
                motions={getMotion(0.1, 1, 0.2, istTransition)}
                text={subTitle}
            ></TextEffect>
            <SocialIcons
                {...socialConfig}
                motions={getMotion(0.1, 2, 0.2, istTransition)}
                links={links}
            />
            {!sitesConfig?.hidden && (
                <Links
                    sitesConfig={sitesConfig}
                    motions={getMotion(0.1, 3, 0.2, istTransition)}
                    primaryColor={primaryColor}
                    staticSites={staticSites}
                    modalSites={modalSites}
                    cardOpacity={cardOpacity}
                />
            )}
            {!sliders?.hidden && (
                <Sliders
                    motions={getMotion(0.1, 4, 0.2, istTransition)}
                    cardOpacity={cardOpacity}
                    {...sliders}
                />
            )}
        </div>
    );
}
