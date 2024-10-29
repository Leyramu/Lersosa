/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

var app = angular.module('sentinelDashboardApp');

app.service('GatewayApiService', ['$http', function ($http) {
  this.queryApis = function (app, ip, port) {
    var param = {
      app: app,
      ip: ip,
      port: port
    };
    return $http({
      url: '/gateway/api/list.json',
      params: param,
      method: 'GET'
    });
  };

  this.newApi = function (api) {
    return $http({
      url: '/gateway/api/new.json',
      data: api,
      method: 'POST'
    });
  };

  this.saveApi = function (api) {
    return $http({
      url: '/gateway/api/save.json',
      data: api,
      method: 'POST'
    });
  };

  this.deleteApi = function (api) {
    var param = {
      id: api.id,
      app: api.app
    };
    return $http({
      url: '/gateway/api/delete.json',
      params: param,
      method: 'POST'
    });
  };

  this.checkApiValid = function (api, apiNames) {
    if (api.apiName === undefined || api.apiName === '') {
      alert('API名称不能为空');
      return false;
    }

    if (api.predicateItems == null || api.predicateItems.length === 0) {
      // Should never happen since no remove button will display when only one predicateItem.
      alert('至少有一个匹配规则');
      return false;
    }

    for (var i = 0; i < api.predicateItems.length; i++) {
      var predicateItem = api.predicateItems[i];
      var pattern = predicateItem.pattern;
      if (pattern === undefined || pattern === '') {
        alert('匹配串不能为空，请检查');
        return false;
      }
    }

    if (apiNames.indexOf(api.apiName) !== -1) {
      alert('API名称(' + api.apiName + ')已存在');
      return false;
    }

    return true;
  };
}]);
