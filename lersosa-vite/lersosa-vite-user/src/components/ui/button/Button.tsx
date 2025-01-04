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
 * @Date: 2024-06-14 11:06:48
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-14 21:44:14
 * @Description:
 */
'use client';
import { ButtonHTMLAttributes } from 'react';
import { clsx } from '@kasuie/utils';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    loading?: boolean;
}

export const Button = ({
                           className,
                           children,
                           loading,
                           onClick,
                           ...props
                       }: ButtonProps) => {
    return (
        <button
            className={clsx(
                'flex min-w-24 items-center justify-center gap-[2px] border-none bg-[var(--primary-color)] px-3 py-2 tracking-[4px] outline-none duration-150 hover:opacity-85',
                {
                    [`${className}`]: className,
                    'rounded-sm': !className,
                    'opacity-80': loading
                }
            )}
            disabled={loading}
            onClick={(e) => {
                !loading && onClick && onClick(e);
            }}
            {...props}
        >
            <span>{children}</span>
            {loading && (
                <span className="h-4 w-4 animate-spin rounded-full border-b-2 border-t-2 ease-linear"></span>
            )}
        </button>
    );
};
