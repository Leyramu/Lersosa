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

package leyramu.framework.lersosa.demo.controller;

import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.demo.domain.Document;
import leyramu.framework.lersosa.demo.esmapper.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搜索引擎 crud 演示案例.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
@RequiredArgsConstructor
@RestController
@RequestMapping("/es")
public class EsCrudController {

    private final DocumentMapper documentMapper;

    /**
     * 查询(指定).
     *
     * @param title 标题
     */
    @GetMapping("/select")
    public Document select(String title) {
        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.eq(Document::getTitle, title);
        return documentMapper.selectOne(wrapper);
    }

    /**
     * 搜索(模糊).
     *
     * @param key 搜索关键字
     */
    @GetMapping("/search")
    public List<Document> search(String key) {
        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.like(Document::getTitle, key);
        return documentMapper.selectList(wrapper);
    }

    /**
     * 插入.
     */
    @PostMapping("/insert")
    public Integer insert(@RequestBody Document document) {
        return documentMapper.insert(document);
    }

    /**
     * 更新.
     */
    @PutMapping("/update")
    public R<Void> update(@RequestBody Document document) {
        // 测试更新 更新有两种情况 分别演示如下:
        // case1: 已知id, 根据id更新 (为了演示方便,此id是从上一步查询中复制过来的,实际业务可以自行查询)
        documentMapper.updateById(document);

        /*
            case2: id未知, 根据条件更新
                LambdaEsUpdateWrapper<Document> wrapper = new LambdaEsUpdateWrapper<>();
                wrapper.like(Document::getTitle, document.getTitle());
                Document document2 = new Document();
                document2.setTitle(document.getTitle());
                document2.setContent(document.getContent());
                documentMapper.update(document2, wrapper);
*/
        return R.ok();
    }

    /**
     * 删除.
     *
     * @param id 主键
     */
    @DeleteMapping("/delete/{id}")
    public R<Integer> delete(@PathVariable String id) {
        // 测试删除数据 删除有两种情况:根据id删或根据条件删
        return R.ok(documentMapper.deleteById(id));
    }
}
