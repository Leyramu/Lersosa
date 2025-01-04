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
 * @Date: 2024-06-15 12:23:52
 * @LastEditors: kasuie
 * @LastEditTime: 2024-06-15 16:50:19
 * @Description:
 */
import CryptoJS from 'crypto-js';

export const Decrypt = (word: any, key: any) => {
    const encryptedHexStr = CryptoJS.enc.Base64.parse(word);
    const ivAndkey = CryptoJS.enc.Utf8.parse(toFixedLength(key));
    const srcs = CryptoJS.enc.Base64.stringify(encryptedHexStr);
    const decrypt = CryptoJS.AES.decrypt(srcs, ivAndkey, {
        iv: ivAndkey,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    const decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
    return decryptedStr.toString();
};

export const Encrypt = (word: any) => {
    const srcs = CryptoJS.enc.Utf8.parse(word);
    const ivAndkey = CryptoJS.enc.Utf8.parse(toFixedLength(word));
    const encrypted = CryptoJS.AES.encrypt(srcs, ivAndkey, {
        iv: ivAndkey,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
};

export const toFixedLength = (input: string, length = 16) => {
    if (input.length === length) {
        return input;
    }

    if (input.length < length) {
        return input.padEnd(length, 'i');
    } else {
        const hash = CryptoJS.MD5(input);
        return CryptoJS.enc.Hex.stringify(hash).slice(0, length);
    }
};
