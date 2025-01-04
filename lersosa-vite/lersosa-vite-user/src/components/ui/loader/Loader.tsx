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
 * @Date: 2024-05-20 19:57:07
 * @LastEditors: kasuie
 * @LastEditTime: 2024-05-22 11:42:24
 * @Description:
 */
import { Paw } from '@kasuie/icon';

export const Loader = ({
                           className = '',
                           warpClass = '',
                           children,
                           miao = false
                       }: {
    className?: string;
    warpClass?: string;
    children?: React.ReactNode | string;
    miao?: boolean;
}) => {
    return (
        <div
            className={`flex flex-col items-center justify-center gap-8 text-center ${warpClass}`}
        >
            {miao ? (
                <Paw
                    className={`h-full w-full ${className}`}
                    style={{
                        width: '300px',
                        height: '200px'
                    }}
                />
            ) : (
                <div className={`mio-loader relative h-10 w-10 ${className}`}></div>
            )}
            {miao && children && typeof children == 'string' ? (
                <p data-glitch={children} className="mio-glitch">
                    {children}
                </p>
            ) : (
                <span>{children}</span>
            )}
        </div>
    );
};
