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
        });
        // 显示tooltip
        $(function () {
        	  $('[data-toggle="tooltip"]').tooltip()
        });
       /* $scope.page = 1;
        $scope.pageSize = 20;
        $scope.loadAll = function() {
            Article.query({page: $scope.page - 1, size: $scope.pageSize, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.articles = result;
                //angular对html转义,增加信任$sce.trustAsHtml
                angular.forEach($scope.articles, function (article) {
                	article.content = $sce.trustAsHtml(article.content);
                });
            });
        };
        
        $scope.loadAll();*/
        
        $scope.loadData = function() {
        	// 用$http.get发请求
            $http.get('api/home').success(function (data) {
            	console.log(data);
            });
        };
        
        $scope.loadData();
    });
