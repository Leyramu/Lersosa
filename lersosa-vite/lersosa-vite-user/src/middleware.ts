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
 * @Date: 2024-06-14 22:32:09
 * @LastEditors: kasuie
 * @LastEditTime: 2024-08-05 20:19:09
 * @Description:
 */
import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';
import { Encrypt } from './lib/utils';

const PRIVATE = ['/config'];

export default async function middleware(req: NextRequest) {
    const { pathname } = req.nextUrl;

    if (pathname && PRIVATE.includes(pathname)) {
        const cookieKey: string = 'accessToken';
        if (req.cookies.has(cookieKey)) {
            const encrypt = Encrypt(process.env.PASSWORD);
            const accessToken = req.cookies.get(cookieKey);
            if (accessToken?.value === encrypt) {
                return NextResponse.next();
            }
        }

        if (!process.env.PASSWORD) {
            return NextResponse.redirect(new URL('/', req.url));
        }

        return NextResponse.rewrite(new URL('/config?verify', req.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: ['/((?!.+\\.[\\w]+$|_next).*)', '/config/:path*']
};
