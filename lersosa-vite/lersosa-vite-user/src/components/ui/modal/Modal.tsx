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
 * @Date: 2024-05-23 10:54:46
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-28 22:44:46
 * @Description:
 */
import ReactDOM from 'react-dom';
import { clsx } from '@kasuie/utils';
import { Cross } from '@kasuie/icon';
import { AnimatePresence, motion } from 'framer-motion';

export function Modal({
                          children,
                          title,
                          visible,
                          closeModal,
                          className = '',
                          warpClass = '',
                          mainClass = ''
                      }: {
    children?: React.ReactNode;
    title?: string;
    visible: boolean;
    closeModal?: Function;
    className?: string;
    warpClass?: string;
    mainClass?: string;
}) {
    if (!visible) return null;

    // const modalVariants = {
    //   hidden: { opacity: 0, scale: 0.5 },
    //   visible: { opacity: 1, scale: 1 },
    //   exit: { opacity: 0, scale: 0.5 },
    // };

    return ReactDOM.createPortal(
        <AnimatePresence>
            {visible && (
                <motion.div
                    className={`fixed inset-0 z-30 flex items-center justify-center bg-black/10 backdrop-blur-lg backdrop-brightness-75 backdrop-saturate-150 ${warpClass}`}
                    onClick={() => {
                        closeModal?.();
                    }}
                    // initial="hidden"
                    // animate="visible"
                    // exit="exit"
                    // variants={modalVariants}
                    // transition={{ duration: 0 }}
                >
                    <motion.div
                        className={clsx(
                            `relative z-20 h-full max-h-[60%] min-w-[95vw] max-w-[95vw] overflow-hidden rounded-xl bg-[#16181aa8] p-[5px_10px_20px_10px] shadow-[0_12px_34px_6px_#0000002e] ease-in-out md:min-w-min md:max-w-[80%] md:p-[10px_20px_30px_20px] ${className}`,
                            {
                                'scale-100': visible,
                                '!pt-10': !title
                            }
                        )}
                        onClick={(e) => e?.stopPropagation()}
                        initial={{ scale: 0 }}
                        animate={{ scale: 1 }}
                        exit={{ scale: 0 }}
                        transition={{ duration: 0.2 }}
                    >
            <span
                className={
                    'absolute right-3 text-white/80 hover:text-white top-[10px] rotate-0 cursor-pointer duration-300 hover:rotate-[180deg]'
                }
                onClick={() => {
                    closeModal?.();
                }}
            >
              <Cross size={20} />
            </span>
                        {title ? (
                            <div className={'py-1 text-center text-2xl font-semibold'}>
                                {title}
                            </div>
                        ) : null}
                        <div
                            className={`mio-scroll max-h-full min-h-40 overflow-y-auto p-[5px] ${mainClass}`}
                        >
                            {children}
                        </div>
                    </motion.div>
                </motion.div>
            )}
        </AnimatePresence>,
        document.body
    );
}
