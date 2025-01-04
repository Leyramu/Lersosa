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
 * @Date: 2024-06-06 19:50:33
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-28 22:10:33
 * @Description:
 */
import { HTMLAttributes } from 'react';
import { clsx } from '@kasuie/utils';
import { Avatar } from '../ui/image/Avatar';
import { getMotion } from '@/lib/motion';
import { AvatarConfig, Link, Site, SitesConfig, SlidersConfig, SocialConfig, SubTitleConfig } from '@/config/config';
import { SocialIcons } from '../social-icons/SocialIcons';
import { Links } from '../links/Links';
import { Sliders } from '../sliders/Sliders';
import dynamic from 'next/dynamic';
import { DoubleArrow } from '@kasuie/icon';

const TextEffect = dynamic(
    async () => (await import('@/components/effect/TextEffect')).TextEffect
);

const HoriTextEffect = dynamic(
    async () =>
        (await import('@/components/effect/HoriTextEffect')).HoriTextEffect
);

interface HorizontalProps extends HTMLAttributes<HTMLDivElement> {
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

export function Horizontal({
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
                           }: HorizontalProps) {
    const renderSubTitle = ({ style, ...props }: any) => {
        switch (style) {
            case 'desc':
                return <HoriTextEffect {...props} />;
            default:
                return <TextEffect {...props} />;
        }
    };

    const renderAvatar = (warpClass = '', motionKey = 'toBottom') => {
        return (
            <Avatar
                priority
                isShowMotion
                alt={name}
                layoutStyle="horizontal"
                src={avatarConfig?.src || ''}
                motions={getMotion(0.1, 1, 0, istTransition, motionKey)}
                animateStyle={avatarConfig?.style}
                {...avatarConfig}
                style={''}
                warpClass={warpClass}
                className="[@media(max-width:768px)]:mx-auto"
            />
        );
    };

    return (
        <div
            className={clsx(
                'mx-auto flex z-[1] relative min-h-screen w-11/12 flex-wrap items-center justify-between pb-10 md:w-[68vw]',
                {
                    'gap-[30px]': gapSize == 'md',
                    'gap-8': gapSize == 'sm',
                    'gap-12': gapSize == 'lg',
                    [`${className}`]: className
                }
            )}
            {...others}
        >
            <div
                className="relative flex min-h-screen w-full flex-col-reverse flex-wrap items-center justify-center gap-10 md:flex-row md:justify-between md:gap-20">
                <div
                    className={clsx(
                        'flex flex-col items-start gap-8 md:flex-1 [@media(max-width:768px)]:items-center [@media(max-width:768px)]:text-center',
                        {
                            'md:gap-20': !avatarConfig?.hidden && avatarConfig?.aloneRight,
                            'md:gap-6': !avatarConfig?.hidden && !avatarConfig?.aloneRight
                        }
                    )}
                >
                    {!avatarConfig?.hidden &&
                        !avatarConfig?.aloneRight &&
                        renderAvatar('', 'toTop')}
                    {renderSubTitle(subTitleConfig)}
                    <SocialIcons
                        {...socialConfig}
                        initialDelay={
                            `${subTitleConfig?.content || ''}${subTitleConfig?.desc || ''}`
                                .length * (subTitleConfig?.gapDelay || 0.05)
                        }
                        warpClass={clsx('', {
                            'pt-12': !avatarConfig?.hidden && !avatarConfig?.aloneRight
                        })}
                        motions={getMotion(0.1, 2, 0.2, istTransition)}
                        links={links}
                    />
                </div>
                {!avatarConfig?.hidden && avatarConfig?.aloneRight && renderAvatar()}
                <p className="absolute bottom-6 left-0 right-0 z-10 flex animate-bounce justify-center text-white">
                    <DoubleArrow className="rotate-90" />
                </p>
            </div>
            {(!sitesConfig?.hidden || !sliders?.hidden) && (
                <div className="flex min-h-[calc(100vh-2.5rem)] w-full flex-col items-center justify-center gap-16">
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
            )}
        </div>
    );
}
