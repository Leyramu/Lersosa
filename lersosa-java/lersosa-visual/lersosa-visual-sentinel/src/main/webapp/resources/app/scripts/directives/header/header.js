/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description
 * # adminPosHeader
 */
angular.module('sentinelDashboardApp')
  .directive('header', ['VersionService', 'AuthService', function () {
    return {
      templateUrl: 'app/scripts/directives/header/header.html',
      restrict: 'E',
      replace: true,
      controller: function ($scope, $state, $window, VersionService, AuthService) {
        VersionService.version().success(function (data) {
          if (data.code == 0) {
            $scope.dashboardVersion = data.data;
          }
        });

        if (!$window.localStorage.getItem("session_sentinel_admin")) {
          AuthService.check().success(function (data) {
            if (data.code == 0) {
              $window.localStorage.setItem('session_sentinel_admin', JSON.stringify(data.data));
              handleLogout($scope, data.data.id)
            } else {
              $state.go('login');
            }
          });
        } else {
          try {
            var id = JSON.parse($window.localStorage.getItem("session_sentinel_admin")).id;
            handleLogout($scope, id);
          } catch (e) {
            // Historical version compatibility processing, fixes issue-1449
            // If error happens while parsing, remove item in localStorage and redirect to login page.
            $window.localStorage.removeItem("session_sentinel_admin");
            $state.go('login');
          }
        }

        function handleLogout($scope, id) {
          if (id == 'FAKE_EMP_ID') {
            $scope.showLogout = false;
          } else {
            $scope.showLogout = true;
          }
        }

        $scope.logout = function () {
          AuthService.logout().success(function (data) {
            if (data.code == 0) {
              $window.localStorage.removeItem("session_sentinel_admin");
              $state.go('login');
            } else {
              alert('logout error');
            }
          });
        }
      }
    }
  }]);
