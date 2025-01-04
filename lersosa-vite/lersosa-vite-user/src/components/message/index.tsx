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
 * @Date: 2024-06-15 13:47:52
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-15 13:48:02
 * @Description:
 */
'use client';
import { Message, MessageItem } from './Message';

interface MessageApi {
    info: (text: string) => void;
    success: (text: string) => void;
    warning: (text: string) => void;
    error: (text: string) => void;
}

let messages: MessageItem[] = [];

const callback = (key: string) => {
    messages = messages.filter((v: any) => v.key != key);
};

const MyMessage: MessageApi = {
    success: (text) => {
        const key = `${messages.length}:${text}`;
        messages.push({
            key: key,
            type: 'success',
            text: text
        });
        Message(text, 'success', callback, messages, key);
    },
    info: (text) => {
        const key = `${messages.length}:${text}`;
        messages.push({
            key: key,
            type: 'info',
            text: text
        });
        Message(text, 'info', callback, messages, key);
    },
    error: (text) => {
        const key = `${messages.length}:${text}`;
        messages.push({
            key: key,
            type: 'error',
            text: text
        });
        Message(text, 'error', callback, messages, key);
    },
    warning: (text) => {
        const key = `${messages.length}:${text}`;
        messages.push({
            key: key,
            type: 'warning',
            text: text
        });
        Message(text, 'warning', callback, messages, key);
    }
};

export default MyMessage;
