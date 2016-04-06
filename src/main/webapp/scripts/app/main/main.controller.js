'use strict';

angular.module('finderApp')
    .controller('MainController', function ($scope, Principal, $http, $timeout, $window, WEBSITENAME) {
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
        // 添加页面title
        $window.document.title = "科技改变生活-" + WEBSITENAME;
        
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
            	// 浏览数最多数据  slice 取得sublist
            	$scope.pageViewFirst = data.pageViewData.slice(0, 1);
            	$scope.pageViewOther = data.pageViewData.slice(1);
            	// 新技术文章数据
//            	$scope.techData = data.techData;
            	// 文章分页数据  默认第一页显示10条
            	$scope.pageDataDTO = data.pageDataDTO;
            	// 活跃作者(文章数最多)
            	$scope.authors = data.authors;
            	// 热门文章(访问最多的数据)
            	$scope.hotArticles = data.hotArticles;
            	
            	// 增加文章描述，去除html标签截取字符用
            	$.each(data.pageDataDTO,function(i,item){
            		item.describle = delHtmlTag(item.content);
    			});
            	
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
        
        // 去掉网页中的所有的html标记
        // js去掉所有html标记的函数
        function delHtmlTag(str)
        {
        	return str.replace(/<[^>]+>/g,"").replace(/&nbsp;/g,"");//去掉所有的html标记
        }
    });
