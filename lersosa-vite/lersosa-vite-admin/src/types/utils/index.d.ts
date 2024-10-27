/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

// @/utils/auth
declare module '@/utils/auth' {
    export {getToken} from '@/utils/auth'

    export {setToken,} from '@/utils/auth'

    export {removeToken} from '@/utils/auth'

    export {getExpiresIn} from '@/utils/auth'

    export {setExpiresIn} from '@/utils/auth'

    export {removeExpiresIn} from '@/utils/auth'
}

// @/utils/dynamicTitle
declare module '@/utils/dynamicTitle' {
    export {useDynamicTitle} from '@/utils/dynamicTitle'
}

// @/utils/errorCode
declare module '@/utils/errorCode' {
    const errorCode: {
        [key: string]: string;
    };
    export default errorCode;
}

// jsencrypt/bin/jsencrypt.min
declare module 'jsencrypt/bin/jsencrypt.min' {
    class JSEncrypt {
        setPublicKey(publicKey: string): void;

        setPrivateKey(privateKey: string): void;

        encrypt(data: string): string | null;

        decrypt(data: string): string | null;
    }

    export default JSEncrypt;
}

// @/utils/lersosa
declare module '@/utils/lersosa' {
    export {parseTime} from '@/utils/lersosa'

    export {resetForm} from '@/utils/lersosa'

    export {addDateRange} from '@/utils/lersosa'

    export {selectDictLabel} from '@/utils/lersosa'

    export {selectDictLabels} from '@/utils/lersosa'

    export {sprintf} from '@/utils/lersosa'

    export {parseStrEmpty} from '@/utils/lersosa'

    export {mergeRecursive} from '@/utils/lersosa'

    export {handleTree} from '@/utils/lersosa'

    export {tansParams} from '@/utils/lersosa'

    export {getNormalPath} from '@/utils/lersosa'

    export {blobValidate} from '@/utils/lersosa'
}

// @/utils/request
declare module '@/utils/request' {
    import {AxiosInstance} from 'axios';
    const request: AxiosInstance;
    export default request;

    export {isRelogin} from '@/utils/request'

    export {download} from '@/utils/request'
}

// @/utils/validate
declare module '@/utils/validate' {
    export {isHttp} from '@/utils/validate'
}

// @/utils/dict
declare module '@/utils/dict' {
    export {useDict} from '@/utils/dict'
}
