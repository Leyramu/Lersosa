/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

const styles: { [key: string]: string } = {
    'el-rate': '.el-rate{display: inline-block; vertical-align: text-top;}',
    'el-upload': '.el-upload__tip{line-height: 1.2;}'
};

interface Element {
    tag: string;
    children?: Element[];
}

function addCss(cssList: string[], el: Element): void {
    const css = styles[el.tag];
    if (css && cssList.indexOf(css) === -1) {
        cssList.push(css);
    }
    if (el.children) {
        el.children.forEach(el2 => addCss(cssList, el2));
    }
}

export function makeUpCss(conf: { fields: Element[] }): string {
    const cssList: string[] = [];
    conf.fields.forEach(el => addCss(cssList, el));
    return cssList.join('\n');
}

