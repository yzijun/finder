'use strict';

angular.module('finderApp')
    .controller('MainController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        // 幻灯片 间隔5秒显示
        $('.carousel').carousel({
        	interval: 5000
        })
    });
