/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

var app = angular.module('sentinelDashboardApp');

app.service('FlowServiceV1', ['$http', function ($http) {
    this.queryMachineRules = function (app, ip, port) {
        var param = {
            app: app,
            ip: ip,
            port: port
        };
        return $http({
            url: '/v1/flow/rules',
            params: param,
            method: 'GET'
        });
    };

    this.newRule = function (rule) {
        var param = {
            resource: rule.resource,
            limitApp: rule.limitApp,
            grade: rule.grade,
            count: rule.count,
            strategy: rule.strategy,
            refResource: rule.refResource,
            controlBehavior: rule.controlBehavior,
            warmUpPeriodSec: rule.warmUpPeriodSec,
            maxQueueingTimeMs: rule.maxQueueingTimeMs,
            app: rule.app,
            ip: rule.ip,
            port: rule.port
        };

        return $http({
            url: '/v1/flow/rule',
            data: rule,
            method: 'POST'
        });
    };

    this.saveRule = function (rule) {
        var param = {
            id: rule.id,
            resource: rule.resource,
            limitApp: rule.limitApp,
            grade: rule.grade,
            count: rule.count,
            strategy: rule.strategy,
            refResource: rule.refResource,
            controlBehavior: rule.controlBehavior,
            warmUpPeriodSec: rule.warmUpPeriodSec,
            maxQueueingTimeMs: rule.maxQueueingTimeMs,
        };

        return $http({
            url: '/v1/flow/save.json',
            params: param,
            method: 'PUT'
        });
    };

    this.deleteRule = function (rule) {
        var param = {
            id: rule.id,
            app: rule.app
        };

        return $http({
            url: '/v1/flow/delete.json',
            params: param,
            method: 'DELETE'
        });
    };

    function notNumberAtLeastZero(num) {
        return num === undefined || num === '' || isNaN(num) || num < 0;
    }

    function notNumberGreaterThanZero(num) {
        return num === undefined || num === '' || isNaN(num) || num <= 0;
    }

    this.checkRuleValid = function (rule) {
        if (rule.resource === undefined || rule.resource === '') {
            alert('资源名称不能为空');
            return false;
        }
        if (rule.count === undefined || rule.count < 0) {
            alert('限流阈值必须大于等于 0');
            return false;
        }
        if (rule.strategy === undefined || rule.strategy < 0) {
            alert('无效的流控模式');
            return false;
        }
        if (rule.strategy == 1 || rule.strategy == 2) {
            if (rule.refResource === undefined || rule.refResource == '') {
                alert('请填写关联资源或入口');
                return false;
            }
        }
        if (rule.controlBehavior === undefined || rule.controlBehavior < 0) {
            alert('无效的流控整形方式');
            return false;
        }
        if (rule.controlBehavior == 1 && notNumberGreaterThanZero(rule.warmUpPeriodSec)) {
            alert('预热时长必须大于 0');
            return false;
        }
        if (rule.controlBehavior == 2 && notNumberGreaterThanZero(rule.maxQueueingTimeMs)) {
            alert('排队超时时间必须大于 0');
            return false;
        }
        if (rule.clusterMode && (rule.clusterConfig === undefined || rule.clusterConfig.thresholdType === undefined)) {
            alert('集群限流配置不正确');
            return false;
        }
        return true;
    };
}]);
