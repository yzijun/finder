'use strict';

angular.module('finderApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            // 可以使用angular.element操作DOM,下面代码(示例用)
//            angular.element("#accountSubMenu").remove();
            $state.go('home');
        };
    });
