'use strict';

angular.module('finderApp')
    .controller('MainController', function ($scope, Principal, $http, $timeout) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            // 是登录用户的场合
            if ($scope.isAuthenticated()) {
            	$http.get('api/home/articlesum').success(function (response) {
            		// 用户文章数量
            		$scope.articlesum = response;
            	});
            }
        });
        // 幻灯片 间隔5秒显示
       /* $('.carousel').carousel({
        	interval: 5000
        });*/
        // DOM加载完成时  绑定 SkitterSlideshow 幻灯片初始化
       /* $scope.loadInitSlide = function() {
        	// 延迟调用等待图片加载完成
        	setTimeout(function(){
        		 $('.box_skitter_large')
                 .css({width: 600, height: 300})
                 .skitter({
                   theme: 'clean',
                   numbers_align: 'center',
                   dots: true, 
                   preview: true
                 });
    	    },1000);
        	 
        };*/
        // 显示tooltip
       /* $(function () {
        	  $('[data-toggle="tooltip"]').tooltip();
        });*/
        
        // DOM加载完成时  绑定tooltip 页面用指令data-ng-init
       /* $scope.loadInitAuthor = function() {  
        	$('[data-toggle="tooltip"]').tooltip();
        };*/
        
        // DOM加载完成时  绑定tooltip 页面用指令data-ng-init
       /* $scope.loadInitArticle = function() {  
        	$('[data-toggle="tooltip"]').tooltip();
        };*/
       
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
        
        // 初始化页面时请求加载数据
        $scope.loadData = function() {
        	// 用$http.get发请求
            $http.get('api/home/all').success(function (data) {
            	// 幻灯片数据 
            	$scope.slides = data.slides;
            	// 创意数据
            	$scope.originalities = data.originalities;
            	// 文章分页数据  默认第一页显示10条
            	$scope.pageData = data.pageData;
            	// 活跃作者(文章数最多)
            	$scope.authors = data.authors;
            	// 热门文章(访问最多的数据)
            	$scope.hotArticles = data.hotArticles;
            	
            	// 延迟调用等待幻灯片图片加载完成
            	$timeout(function(){
            		 $('.box_skitter_normal')
            		 .css({width: 600, height: 300})
            		 .skitter({
            		        theme: 'clean',
            		        numbers_align: 'center',
            		        dots: true, 
            		        preview: true
            		 });
        	    });
            	
            	// 延迟调用等待数据加载完成
            	$timeout(function(){
            		$('[data-toggle="tooltip"]').tooltip();
        	    });
            	
            });
        };
        
        $scope.loadData();
    });
