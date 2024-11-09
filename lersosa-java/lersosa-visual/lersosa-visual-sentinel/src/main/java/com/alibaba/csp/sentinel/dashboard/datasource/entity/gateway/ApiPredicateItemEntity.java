/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;

import java.util.Objects;

/**
 * Entity for {@link ApiPredicateItem}.
 *
 * @author cdfive
 * @since 1.7.0
 */
public class ApiPredicateItemEntity {

    private String pattern;

    private Integer matchStrategy;

    public ApiPredicateItemEntity() {
    }

    public ApiPredicateItemEntity(String pattern, int matchStrategy) {
        this.pattern = pattern;
        this.matchStrategy = matchStrategy;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getMatchStrategy() {
        return matchStrategy;
    }

    public void setMatchStrategy(Integer matchStrategy) {
        this.matchStrategy = matchStrategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiPredicateItemEntity that = (ApiPredicateItemEntity) o;
        return Objects.equals(pattern, that.pattern) &&
            Objects.equals(matchStrategy, that.matchStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, matchStrategy);
    }

    @Override
    public String toString() {
        return "ApiPredicateItemEntity{" +
            "pattern='" + pattern + '\'' +
            ", matchStrategy=" + matchStrategy +
            '}';
    }
}
