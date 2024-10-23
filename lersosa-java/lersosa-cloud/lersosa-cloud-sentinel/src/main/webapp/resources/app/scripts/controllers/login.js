/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

var app = angular.module('sentinelDashboardApp');

app.controller('LoginCtl', ['$scope', '$state', '$window', 'AuthService',
  function ($scope, $state, $window, AuthService) {
    // If auth passed, jump to the index page directly
    if ($window.localStorage.getItem('session_sentinel_admin')) {
      $state.go('dashboard');
    }

    $scope.login = function () {
      if (!$scope.username) {
        alert('请输入用户名');
        return;
      }

      if (!$scope.password) {
        alert('请输入密码');
        return;
      }

      var param = {"username": $scope.username, "password": $scope.password};

      AuthService.login(param).success(function (data) {
        if (data.code == 0) {
          $window.localStorage.setItem('session_sentinel_admin', JSON.stringify(data.data));
          $state.go('dashboard');
        } else {
          alert(data.msg);
        }
      });
    };
  }]
);
