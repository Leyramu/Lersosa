/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.auth;

/**
 * Interface for authentication and authorization.
 *
 * @author Carpenter Lee
 * @since 1.5.0
 */
public interface AuthService<R> {

    /**
     * Get the authentication user.
     *
     * @param request the request contains the user information
     * @return the auth user represent the current user, when the user is illegal, a null value will return.
     */
    AuthUser getAuthUser(R request);

    /**
     * Privilege type.
     */
    enum PrivilegeType {
        /**
         * Read rule
         */
        READ_RULE,
        /**
         * Create or modify rule
         */
        WRITE_RULE,
        /**
         * Delete rule
         */
        DELETE_RULE,
        /**
         * Read metrics
         */
        READ_METRIC,
        /**
         * Add machine
         */
        ADD_MACHINE,
        /**
         * All privileges above are granted.
         */
        ALL
    }

    /**
     * Represents the current user.
     */
    interface AuthUser {

        /**
         * Query whether current user has the specific privilege to the target, the target
         * may be an app name or an ip address, or other destination.
         * <p>
         * This method will use return value to represent  whether user has the specific
         * privileges to the target, but to throw a RuntimeException to represent no auth
         * is also a good way.
         * </p>
         *
         * @param target        the target to check
         * @param privilegeType the privilege type to check
         * @return if current user has the specific privileges to the target, return true,
         * otherwise return false.
         */
        boolean authTarget(String target, PrivilegeType privilegeType);

        /**
         * Check whether current user is a super-user.
         *
         * @return if current user is super user return true, else return false.
         */
        boolean isSuperUser();

        /**
         * Get current user's nick name.
         *
         * @return current user's nick name.
         */
        String getNickName();

        /**
         * Get current user's login name.
         *
         * @return current user's login name.
         */
        String getLoginName();

        /**
         * Get current user's ID.
         *
         * @return ID of current user
         */
        String getId();
    }
}
