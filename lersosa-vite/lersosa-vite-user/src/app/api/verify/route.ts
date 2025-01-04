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
 * @Date: 2024-06-15 11:20:01
 * @LastEditors: kasuie
 * @LastEditTime: 2024-07-05 15:58:50
 * @Description:
 */
import { Decrypt } from '@/lib/utils';
import { NextRequest, NextResponse } from 'next/server';

export const POST = async (req: NextRequest) => {
    const { checkCode } = await req.json();
    if (!checkCode) {
        return NextResponse.json({
            data: null,
            message: 'checkCode is required',
            success: false
        });
    }
    const password = Decrypt(checkCode, process.env.PASSWORD);

    if (password === process.env.PASSWORD) {
        const response = NextResponse.json({
            data: true,
            message: 'success',
            success: true
        });

        response.cookies.set('accessToken', checkCode, {
            httpOnly: true,
            secure: false,
            path: '/',
            maxAge: 60 * 60 * 24 * 14
        });

        return response;
    } else {
        return NextResponse.json({
            data: null,
            message: 'fail',
            success: false
        });
    }
};
