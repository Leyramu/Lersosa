/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.api;

import leyramu.framework.lersosa.system.api.domain.bo.RemoteSocialBo;
import leyramu.framework.lersosa.system.api.domain.vo.RemoteSocialVo;

import java.util.List;

/**
 * 社会化关系服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface RemoteSocialService {

    /**
     * 根据 authId 查询用户授权信息.
     *
     * @param authId 认证id
     * @return 授权信息
     */
    List<RemoteSocialVo> selectByAuthId(String authId);

    /**
     * 查询列表.
     *
     * @param bo 社会化关系业务对象
     */
    List<RemoteSocialVo> queryList(RemoteSocialBo bo);

    /**
     * 保存社会化关系.
     *
     * @param bo 社会化关系业务对象
     */
    void insertByBo(RemoteSocialBo bo);

    /**
     * 更新社会化关系.
     *
     * @param bo 社会化关系业务对象
     */
    void updateByBo(RemoteSocialBo bo);

    /**
     * 删除社会化关系.
     *
     * @param socialId 社会化关系ID
     * @return 结果
     */
    Boolean deleteWithValidById(Long socialId);
}
