/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

/**
 * Cluster state control service.
 *
 * @author Eric Zhao
 */
angular.module('sentinelDashboardApp').service('ClusterStateService', ['$http', function ($http) {

    this.fetchClusterUniversalStateSingle = function(app, ip, port) {
        var param = {
            app: app,
            ip: ip,
            port: port
        };
        return $http({
            url: '/cluster/state_single',
            params: param,
            method: 'GET'
        });
    };

    this.fetchClusterUniversalStateOfApp = function(app) {
        return $http({
            url: '/cluster/state/' + app,
            method: 'GET'
        });
    };

    this.fetchClusterServerStateOfApp = function(app) {
        return $http({
            url: '/cluster/server_state/' + app,
            method: 'GET'
        });
    };

    this.fetchClusterClientStateOfApp = function(app) {
        return $http({
            url: '/cluster/client_state/' + app,
            method: 'GET'
        });
    };

    this.modifyClusterConfig = function(config) {
        return $http({
            url: '/cluster/config/modify_single',
            data: config,
            method: 'POST'
        });
    };

    this.applyClusterFullAssignOfApp = function(app, clusterMap) {
        return $http({
            url: '/cluster/assign/all_server/' + app,
            data: clusterMap,
            method: 'POST'
        });
    };

    this.applyClusterSingleServerAssignOfApp = function(app, request) {
        return $http({
            url: '/cluster/assign/single_server/' + app,
            data: request,
            method: 'POST'
        });
    };

    this.applyClusterServerBatchUnbind = function(app, machineSet) {
        return $http({
            url: '/cluster/assign/unbind_server/' + app,
            data: machineSet,
            method: 'POST'
        });
    };
}]);
