/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

/**
 * Parameter flow control service.
 *
 * @author Eric Zhao
 */
angular.module('sentinelDashboardApp').service('ParamFlowService', ['$http', function ($http) {
  this.queryMachineRules = function(app, ip, port) {
    var param = {
      app: app,
      ip: ip,
      port: port
    };
    return $http({
      url: '/paramFlow/rules',
      params: param,
      method: 'GET'
    });
  };

  this.addNewRule = function(rule) {
    return $http({
      url: '/paramFlow/rule',
      data: rule,
      method: 'POST'
    });
  };

  this.saveRule = function (entity) {
    return $http({
      url: '/paramFlow/rule/' + entity.id,
      data: entity,
      method: 'PUT'
    });
  };

  this.deleteRule = function (entity) {
    return $http({
      url: '/paramFlow/rule/' + entity.id,
      method: 'DELETE'
    });
  };

    function isNumberClass(classType) {
        return classType === 'int' || classType === 'double' ||
            classType === 'float' || classType === 'long' || classType === 'short';
    }

    function isByteClass(classType) {
        return classType === 'byte';
    }

    function notNumberAtLeastZero(num) {
        return num === undefined || num === '' || isNaN(num) || num < 0;
    }

    function notGoodNumber(num) {
        return num === undefined || num === '' || isNaN(num);
    }

    function notGoodNumberBetweenExclusive(num, l ,r) {
        return num === undefined || num === '' || isNaN(num) || num < l || num > r;
    }

    function notValidParamItem(curExItem) {
        if (isNumberClass(curExItem.classType) && notGoodNumber(curExItem.object)) {
            return true;
        }
        if (isByteClass(curExItem.classType) && notGoodNumberBetweenExclusive(curExItem.object, -128, 127)) {
            return true;
        }
        return curExItem.object === undefined || curExItem.classType === undefined ||
            notNumberAtLeastZero(curExItem.count);
    }

  this.checkRuleValid = function (rule) {
      if (!rule.resource || rule.resource === '') {
          alert('资源名称不能为空');
          return false;
      }
      if (rule.grade != 1) {
          alert('未知的限流模式');
          return false;
      }
      if (rule.count < 0) {
          alert('限流阈值必须大于等于 0');
          return false;
      }
      if (rule.paramIdx === undefined || rule.paramIdx === '' || isNaN(rule.paramIdx) || rule.paramIdx < 0) {
          alert('热点参数索引必须大于等于 0');
          return false;
      }
      if (rule.paramFlowItemList !== undefined) {
          for (var i = 0; i < rule.paramFlowItemList.length; i++) {
              var item = rule.paramFlowItemList[i];
              if (notValidParamItem(item)) {
                  alert('热点参数例外项不合法，请检查值和类型是否正确：参数为 ' + item.object + ', 类型为 ' +
                      item.classType + ', 限流阈值为 ' + item.count);
                  return false;
              }
          }
      }
      return true;
  };
}]);
