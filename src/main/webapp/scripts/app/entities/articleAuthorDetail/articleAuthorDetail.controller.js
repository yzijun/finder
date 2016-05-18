'use strict';

angular.module('finderApp')
    .controller('ArticleAuthorDetailController', function ($scope, $state, $window, $http, $stateParams, $timeout, WEBSITENAME, CommonTools) {
    	// 在controller中使用$stateParams中获取参数
//    	alert($stateParams.uid);
    	
    	 // 是否显示回到顶部按钮
        CommonTools.showToTop();
        // 点击回到顶部按钮
        $("#go-top-btn").click(function(){
        	// 回到顶部按钮实现函数
        	CommonTools.scrollTo();
        });
    	
    	// 文章分页数据
    	$scope.pageDataDTOs = [];
        $scope.loadArticleAuthor = function() {
        	removeTabCalss();
        	// 设定当前tab
        	$(".user-nav a:first").addClass("current");
        	// 取得数据类型有文章、评论、收获喜欢
        	$scope.detype = "article";
        	
        	getData();
        };
        // 取得初始数据
        function getData() {
        	$http.get('api/author/detail/' + $stateParams.uid + '?detype=' + $scope.detype).success(function(data) {
        		// 添加页面title
                $window.document.title =  data.user.nickName + " - " + WEBSITENAME;
                
                $scope.author = data;
                // 默认 文章分页数据
                $scope.pageDataDTOs = data.pageDataDTO;
                // 发表文章数  不使用总记录数totalElements
//                $scope.articleNum = data.pageData.totalElements;
                
                // 增加文章描述，去除html标签截取字符用  
            	/*$.each(data.pageDataDTO,function(i,item){
            		item.describle = CommonTools.delHtmlTag(item.content);
            		// 去除空格,可能有空格的情况
            		item.describle = $.trim(item.describle);
    			});*/
                // 改用angular的方式循环
                angular.forEach(data.pageDataDTO, function (item) {
                	item.describle = CommonTools.delHtmlTag(item.content);
            		// 去除空格,可能有空格的情况
            		item.describle = $.trim(item.describle);
                });
            	// 分页相关
            	$scope.page = (data.pageData.number + 1);
                $scope.pageSize = 10;
                $scope.totalItems = data.pageData.totalElements;
        	});
        }
        
        $scope.loadArticleAuthor();
        
        // 取得收获喜欢 初始数据
        $scope.loadFavoriteAuthor = function() {
        	removeTabCalss();
        	// 设定当前tab
        	$(".user-nav a:last").addClass("current");
        	
        	$scope.detype = "favorite";
        	
        	getData();
    	}
        
        // 分页
        $scope.loadAll = function() {
        	// 跳转页数
        	var page = $scope.page - 1;
        	// 每页多少条数据
        	var size = $scope.pageSize;
        	var url = 'api/author/detailpage/' + $stateParams.uid + '?page='+page+'&size='+size+'&detype='+$scope.detype;
        	$http.get(url).success(function(data) {
                
                $scope.pageDataDTOs = data.pageDataDTO;
                // 增加文章描述，去除html标签截取字符用
            	$.each(data.pageDataDTO,function(i,item){
            		item.describle = CommonTools.delHtmlTag(item.content);
            		// 去除空格,可能有空格的情况
            		item.describle = $.trim(item.describle);
    			});
            	// 分页相关
            	$scope.page = (data.pageData.number + 1);
                $scope.totalItems = data.pageData.totalElements;
        	});
        };
        // 分页指令默认调用
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        
        // 显示JHipster QQ群
        CommonTools.showQQGroup();
        // 延迟调用等待数据加载完成
    	$timeout(function(){
    		$('[data-toggle="tooltip"]').tooltip();
	    });
    	
    	// 查找class是current并移除
    	function removeTabCalss() {
    		$(".user-nav a").each(function(){
    			if($(this).hasClass("current")) {
    				$(this).removeClass("current");
    			}
    		});
    	}
    });
