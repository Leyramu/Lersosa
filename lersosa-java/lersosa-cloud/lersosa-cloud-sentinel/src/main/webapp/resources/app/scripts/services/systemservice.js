/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

var app = angular.module('sentinelDashboardApp');

app.service('SystemService', ['$http', function ($http) {
  this.queryMachineRules = function (app, ip, port) {
    var param = {
      app: app,
      ip: ip,
      port: port
    };
    return $http({
      url: 'system/rules.json',
      params: param,
      method: 'GET'
    });
  };

  this.newRule = function (rule) {
    var param = {
      app: rule.app,
      ip: rule.ip,
      port: rule.port
    };
    if (rule.grade == 0) {// avgLoad
      param.highestSystemLoad = rule.highestSystemLoad;
    } else if (rule.grade == 1) {// avgRt
      param.avgRt = rule.avgRt;
    } else if (rule.grade == 2) {// maxThread
      param.maxThread = rule.maxThread;
    } else if (rule.grade == 3) {// qps
      param.qps = rule.qps;
    } else if (rule.grade == 4) {// cpu
      param.highestCpuUsage = rule.highestCpuUsage;
    }

    return $http({
      url: '/system/new.json',
      params: param,
      method: 'GET'
    });
  };

  this.saveRule = function (rule) {
    var param = {
      id: rule.id,
    };
    if (rule.grade == 0) {// avgLoad
      param.highestSystemLoad = rule.highestSystemLoad;
    } else if (rule.grade == 1) {// avgRt
      param.avgRt = rule.avgRt;
    } else if (rule.grade == 2) {// maxThread
      param.maxThread = rule.maxThread;
    } else if (rule.grade == 3) {// qps
      param.qps = rule.qps;
    } else if (rule.grade == 4) {// cpu
        param.highestCpuUsage = rule.highestCpuUsage;
    }

    return $http({
      url: '/system/save.json',
      params: param,
      method: 'GET'
    });
  };

  this.deleteRule = function (rule) {
    var param = {
      id: rule.id,
      app: rule.app
    };

    return $http({
      url: '/system/delete.json',
      params: param,
      method: 'GET'
    });
  };
}]);
