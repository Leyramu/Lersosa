/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

const useDictStore = defineStore(
    'dict',
    {
        state: () => ({
            dict: new Array()
        }),
        actions: {
            // 获取字典
            getDict(_key) {
                if (_key == null && _key == "") {
                    return null;
                }
                try {
                    for (let i = 0; i < this.dict.length; i++) {
                        if (this.dict[i].key == _key) {
                            return this.dict[i].value;
                        }
                    }
                } catch (e) {
                    return null;
                }
            },
            // 设置字典
            setDict(_key, value) {
                if (_key !== null && _key !== "") {
                    this.dict.push({
                        key: _key,
                        value: value
                    });
                }
            },
            // 删除字典
            removeDict(_key) {
                var bln = false;
                try {
                    for (let i = 0; i < this.dict.length; i++) {
                        if (this.dict[i].key == _key) {
                            this.dict.splice(i, 1);
                            return true;
                        }
                    }
                } catch (e) {
                    bln = false;
                }
                return bln;
            },
            // 清空字典
            cleanDict() {
                this.dict = new Array();
            },
            // 初始字典
            initDict() {
            }
        }
    })

export default useDictStore
