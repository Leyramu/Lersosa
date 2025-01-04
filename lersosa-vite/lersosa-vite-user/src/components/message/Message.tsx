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
 * @Date: 2024-06-15 13:46:08
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-15 16:46:19
 * @Description:
 */
'use client';
import { createRoot } from 'react-dom/client';
import {
    CheckCircle as Success,
    CrossCircle as Error,
    InfoCircle as Info,
    QuestionCircle as Warning
} from '@kasuie/icon';
import { motion } from 'framer-motion';

interface MessageProps {
    content: string;
    type: string;
    messages?: any;
    action?: {
        text: string;
        onClick: () => void;
    };
    onClose?: () => void;
}

export interface MessageItem {
    key: string;
    type: string;
    text: string;
}

const MessageItem = (props: MessageProps) => {
    return (
        <motion.div
            className="pointer-events-none fixed left-0 top-0 z-20 flex w-screen justify-center"
            initial={{ opacity: 0, y: -50 }}
            animate={{ opacity: 1, y: 50 * (props.messages?.length || 1) }}
            exit={{ opacity: 0, y: -50 }}
        >
            <div
                className="pointer-events-auto flex max-w-[80vw] items-center gap-1 break-all rounded-full border border-transparent bg-opacity-10 px-6 py-2 text-xs shadow-md"
                style={{
                    backgroundColor: `rgba(var(--mio-message-${props.type}), 0.1)`,
                    borderColor: `rgba(var(--mio-message-${props.type}), 0.6)`,
                    color: `rgb(var(--mio-message-${props.type}))`
                }}
            >
        <span className="flex items-center">
          {props.type &&
              (props.type === 'success' ? (
                  <Success size={14} color={'var(--mio-message-success)'} />
              ) : props.type === 'info' ? (
                  <Info size={14} color={'var(--mio-message-info)'} />
              ) : props.type === 'error' ? (
                  <Error size={14} color={'var(--mio-message-error)'} />
              ) : props.type === 'warning' ? (
                  <Warning size={14} color={'var(--mio-message-warning)'} />
              ) : null)}
        </span>
                <span>{props.content}</span>
                {props.action && (
                    <button
                        onClick={() => {
                            props.action?.onClick?.();
                            props.onClose?.();
                        }}
                        className="cursor-pointer border-0 bg-transparent pl-5 text-white opacity-80 hover:opacity-100"
                    >
                        {props.action.text}
                    </button>
                )}
            </div>
        </motion.div>
    );
};

export function Message(
    content: string,
    type: string = 'info',
    callback?: Function,
    messages: MessageItem[] = [],
    key?: string,
    delay = 3000,
    action?: MessageProps['action']
) {
    const div = document.createElement('div');
    type && div.classList.add(`mio-message-${type}`);
    document.body.appendChild(div);
    const root = createRoot(div);

    const close = () => {
        callback && callback(key);
        const childrenDiv = div.querySelector('div');
        childrenDiv?.classList.add('mio-hidden');
        setTimeout(() => {
            root.unmount();
            div.remove();
        }, 300);
    };

    setTimeout(() => {
        close();
    }, delay);

    if (messages?.length >= 5) {
        messages[0].key && callback?.(messages[0].key);
    }

    root.render(
        <MessageItem
            content={content}
            action={action}
            type={type}
            messages={messages}
            onClose={close}
        />
    );
}
