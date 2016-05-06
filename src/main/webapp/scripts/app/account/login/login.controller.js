'use strict';

angular.module('finderApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth, CommonTools) {
    	/*
    	 * 默认回到页面的顶部
    	 * 点击 喜欢和评论登录时，需要回到页面顶部
    	 */
    	CommonTools.scrollTo();
    	
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
