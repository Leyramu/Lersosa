/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

var app = angular.module('sentinelDashboardApp');

app.service('MetricService', ['$http', function ($http) {

  this.queryAppSortedIdentities = function (params) {
    return $http({
      url: '/metric/queryTopResourceMetric.json',
      params: params,
      method: 'GET'
    });
  };

  this.queryByAppAndIdentity = function (params) {
    return $http({
      url: '/metric/queryByAppAndResource.json',
      params: params,
      method: 'GET'
    });
  };

  this.queryByMachineAndIdentity = function (ip, port, identity, startTime, endTime) {
    var param = {
      ip: ip,
      port: port,
      identity: identity,
      startTime: startTime.getTime(),
      endTime: endTime.getTime()
    };

    return $http({
      url: '/metric/queryByAppAndResource.json',
      params: param,
      method: 'GET'
    });
  };
}]);
