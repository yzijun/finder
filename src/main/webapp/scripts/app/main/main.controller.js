'use strict';

angular.module('finderApp')
    .controller('MainController', function ($scope, Principal, $http, $timeout, $window, WEBSITENAME, ParseLinks) {
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
        $window.document.title = "科技改变生活 - " + WEBSITENAME;
        
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
            $http.get('api/home/all').success(function (data, status, headers) {
            	// 浏览数最多数据  slice 取得sublist
            	$scope.pageViewFirst = data.pageViewData.slice(0, 1);
            	$scope.pageViewOther = data.pageViewData.slice(1);
            	// 新技术文章数据
//            	$scope.techData = data.techData;
            	// 文章分页数据  默认第一页显示10条
            	$scope.pageDataDTOs = data.pageDataDTO;
            	// 文章分页数据当前是第几页
//                $scope.pageNumber = parseInt(headers('X-Page-Number'));
                $scope.pageNumber = data.pageNumber;
                // 文章总页数  通过Headers传递的总页数
//                $scope.totalPages = parseInt(headers('X-Page-TotalPages'));
                $scope.totalPages = data.totalPages;
                // 文章是否有下一页
                $scope.nextPage = haveNextPage();
                // links是对象  Object { next=1,  last=1,  first=0}
//                $scope.links = ParseLinks.parse(headers('link'));
//                $scope.linkURL = ParseLinks.linkURL(headers('link'));
                // 请求下一页的URL 通过Headers传递的分页URL
                // <api/home/page?page=1&size=2>; rel="next",<api/home/page?page=1&size=2>; rel="last",<api/home/page?page=0&size=2>; rel="first"
//                $scope.linkURL = headers('link');
                
            	// 活跃作者(文章数最多)
            	$scope.authors = data.authors;
            	// 热门文章(访问最多的数据)
            	$scope.hotArticles = data.hotArticles;
            	
            	// 增加文章描述，去除html标签截取字符用
            	$.each(data.pageDataDTO,function(i,item){
            		item.describle = delHtmlTag(item.content);
    			});
            	
            	// 延迟调用等待幻灯片图片加载完成
            	/*$timeout(function(){
            		 $('.box_skitter_normal')
            		 .css({width: 600, height: 300})
            		 .skitter({
            		        theme: 'clean',
            		        numbers_align: 'center',
            		        dots: true, 
            		        preview: true
            		 });
        	    });*/
            	
            	// 延迟调用等待数据加载完成
            	$timeout(function(){
            		$('[data-toggle="tooltip"]').tooltip();
        	    });
            	
            });
        };
        
        $scope.loadData();
        
        
        // js去掉所有html标记的函数
        function delHtmlTag(str)
        {
        	// 去掉网页中的所有的html标记
        	return str.replace(/<[^>]+>/g,"").replace(/&nbsp;/g,"");//去掉所有的html标记
        }
        
        // 取得文章分页数据
        $scope.loadPageArticle = function() {
        	// 数据加载效果
        	var l = Ladda.create($('.ladda-button')[0]);
    	 	l.start();
        	// 用$http.get发请求
    	 	// 下一页页数
        	var page = $scope.pageNumber + 1;
        	// 每页多少条数据
        	var size = 10;
    	 	var linkURL = 'api/home/page?page='+page+'&size='+size;
            $http.get(linkURL).success(function (data) {
                // 文章当前是第几页
            	 $scope.pageNumber = data.pageNumber;
                // 文章总页数
            	 $scope.totalPages = data.totalPages;
                // 文章是否有下一页
                $scope.nextPage = haveNextPage();
                for (var i = 0; i < data.pageDataDTO.length; i++) {
                	// 下一页的数据加到原来的文章列表下
                	$scope.pageDataDTOs.push(data.pageDataDTO[i]);
				}
                
                // 停止数据加载效果
                l.stop();
            });
        }
        
        /*
         * 判断是否要显示加载更多 
         * 不显示条件是当前页等于总页数，总页数等于零
         */
        function haveNextPage() {
        	return $scope.totalPages == ($scope.pageNumber + 1) 
        		   || $scope.totalPages == 0;
        }
    });
