/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster;

import lombok.Data;

/**
 * 集群状态单 VO.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
@SuppressWarnings("unused")
public class ClusterStateSingleVO {

    private String address;
    private Integer mode;
    private String target;

    public ClusterStateSingleVO setAddress(String address) {
        this.address = address;
        return this;
    }

    public ClusterStateSingleVO setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    public ClusterStateSingleVO setTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public String toString() {
        return "ClusterStateSingleVO{" +
            "address='" + address + '\'' +
            ", mode=" + mode +
            ", target='" + target + '\'' +
            '}';
    }
}
