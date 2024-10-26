/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {makeMap} from '@/utils/index'

// 参考https://github.com/vuejs/vue/blob/v2.6.10/src/platforms/web/server/util.js
const isAttr = makeMap(
    'accept,accept-charset,accesskey,action,align,alt,async,autocomplete,'
    + 'autofocus,autoplay,autosave,bgcolor,border,buffered,challenge,charset,'
    + 'checked,cite,class,code,codebase,color,cols,colspan,content,http-equiv,'
    + 'name,contenteditable,contextmenu,controls,coords,data,datetime,default,'
    + 'defer,dir,dirname,disabled,download,draggable,dropzone,enctype,method,for,'
    + 'form,formaction,headers,height,hidden,high,href,hreflang,http-equiv,'
    + 'icon,id,ismap,itemprop,keytype,kind,label,lang,language,list,loop,low,'
    + 'manifest,max,maxlength,media,method,GET,POST,min,multiple,email,file,'
    + 'muted,name,novalidate,open,optimum,pattern,ping,placeholder,poster,'
    + 'preload,radiogroup,readonly,rel,required,reversed,rows,rowspan,sandbox,'
    + 'scope,scoped,seamless,selected,shape,size,type,text,password,sizes,span,'
    + 'spellcheck,src,srcdoc,srclang,srcset,start,step,style,summary,tabindex,'
    + 'target,title,type,usemap,value,width,wrap'
)

function vModel(self, dataObject, defaultValue) {
    dataObject.props.value = defaultValue

    dataObject.on.input = val => {
        self.$emit('input', val)
    }
}

const componentChild = {
    'el-button': {
        default(h, conf, key) {
            return conf[key]
        },
    },
    'el-input': {
        prepend(h, conf, key) {
            return <template slot="prepend">{conf[key]}</template>
        },
        append(h, conf, key) {
            return <template slot="append">{conf[key]}</template>
        }
    },
    'el-select': {
        options(h, conf, key) {
            const list = []
            conf.options.forEach(item => {
                list.push(<el-option label={item.label} value={item.value} disabled={item.disabled}></el-option>)
            })
            return list
        }
    },
    'el-radio-group': {
        options(h, conf, key) {
            const list = []
            conf.options.forEach(item => {
                if (conf.optionType === 'button') list.push(<el-radio-button
                    label={item.value}>{item.label}</el-radio-button>)
                else list.push(<el-radio label={item.value} border={conf.border}>{item.label}</el-radio>)
            })
            return list
        }
    },
    'el-checkbox-group': {
        options(h, conf, key) {
            const list = []
            conf.options.forEach(item => {
                if (conf.optionType === 'button') {
                    list.push(<el-checkbox-button label={item.value}>{item.label}</el-checkbox-button>)
                } else {
                    list.push(<el-checkbox label={item.value} border={conf.border}>{item.label}</el-checkbox>)
                }
            })
            return list
        }
    },
    'el-upload': {
        'list-type': (h, conf, key) => {
            const list = []
            if (conf['list-type'] === 'picture-card') {
                list.push(<i class="el-icon-plus"></i>)
            } else {
                list.push(<el-button size="small" type="primary" icon="el-icon-upload">{conf.buttonText}</el-button>)
            }
            if (conf.showTip) {
                list.push(<div slot="tip"
                               class="el-upload__tip">只能上传不超过 {conf.fileSize}{conf.sizeUnit} 的{conf.accept}文件</div>)
            }
            return list
        }
    }
}

export default {
    render(h) {
        const dataObject = {
            attrs: {},
            props: {},
            on: {},
            style: {}
        }
        const confClone = JSON.parse(JSON.stringify(this.conf))
        const children = []

        const childObjs = componentChild[confClone.tag]
        if (childObjs) {
            Object.keys(childObjs).forEach(key => {
                const childFunc = childObjs[key]
                if (confClone[key]) {
                    children.push(childFunc(h, confClone, key))
                }
            })
        }

        Object.keys(confClone).forEach(key => {
            const val = confClone[key]
            if (key === 'vModel') {
                vModel(this, dataObject, confClone.defaultValue)
            } else if (dataObject[key]) {
                dataObject[key] = val
            } else if (!isAttr(key)) {
                dataObject.props[key] = val
            } else {
                dataObject.attrs[key] = val
            }
        })
        return h(this.conf.tag, dataObject, children)
    },
    props: ['conf']
}
