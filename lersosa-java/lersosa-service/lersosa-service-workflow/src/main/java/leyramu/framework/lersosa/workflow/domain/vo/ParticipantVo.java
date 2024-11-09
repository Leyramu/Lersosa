/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 参与者
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class ParticipantVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 组id（角色id）
     */
    private List<Long> groupIds;

    /**
     * 候选人id（用户id） 当组id不为空时，将组内人员查出放入candidate
     */
    private List<Long> candidate;

    /**
     * 候选人名称（用户名称） 当组id不为空时，将组内人员查出放入candidateName
     */
    private List<String> candidateName;

    /**
     * 是否认领标识
     * 当为空时默认当前任务不需要认领
     * 当为true时当前任务说明为候选模式并且有人已经认领了任务可以归还，
     * 当为false时当前任务说明为候选模式该任务未认领，
     */
    private Boolean claim;

}
