'use strict';

angular.module('finderApp')
    .controller('MainController', function ($scope, Principal, $http) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            
            $http.get('api/home/articlesum').success(function (response) {
            	// 用户文章数量
                $scope.articlesum = response;
            });
        });
        // 幻灯片 间隔5秒显示
        $('.carousel').carousel({
        	interval: 5000
        })
    });
