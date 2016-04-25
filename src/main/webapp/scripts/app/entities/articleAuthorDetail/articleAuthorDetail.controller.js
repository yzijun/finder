'use strict';

angular.module('finderApp')
    .controller('ArticleAuthorDetailController', function ($scope, $state, $window, $http, $stateParams, WEBSITENAME, CommonTools) {
    	// 在controller中使用$stateParams中获取参数
//    	alert($stateParams.uid);
    	
    	 // 是否显示回到顶部按钮
        CommonTools.showToTop();
        // 点击回到顶部按钮
        $("#go-top-btn").click(function(){
        	// 回到顶部按钮实现函数
        	CommonTools.scrollTo();
        });
    	
    	// 取得数据类型有文章、评论、收获喜欢
    	$scope.type = "article";
    	// 文章分页数据
    	$scope.pageDataDTOs = [];
        $scope.loadArticleAuthor = function() {
        	$http.get('api/author/detail/' + $stateParams.uid).success(function(data) {
        		// 添加页面title
                $window.document.title =  data.user.nickName + " - " + WEBSITENAME;
                
                $scope.author = data;
                // 默认 文章分页数据
                $scope.pageDataDTOs = data.pageDataDTO;
                
                // 增加文章描述，去除html标签截取字符用
            	$.each(data.pageDataDTO,function(i,item){
            		item.describle = delHtmlTag(item.content);
            		// 去除空格,可能有空格的情况
            		item.describle = $.trim(item.describle);
    			});
            	// 分页相关
            	$scope.page = (data.pageData.number + 1);
                $scope.pageSize = 10;
                $scope.totalItems = data.pageData.totalElements;
        	});
        };
        $scope.loadArticleAuthor();
        
        // 分页
        $scope.loadAll = function() {
        	// 跳转页数
        	var page = $scope.page - 1;
        	// 每页多少条数据
        	var size = $scope.pageSize;
        	var url = 'api/author/detailpage/' + $stateParams.uid + '?page='+page+'&size='+size+'&detype='+$scope.type;
        	$http.get(url).success(function(data) {
                
                $scope.pageDataDTOs = data.pageDataDTO;
                // 增加文章描述，去除html标签截取字符用
            	$.each(data.pageDataDTO,function(i,item){
            		item.describle = delHtmlTag(item.content);
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
        
        
        // js去掉所有html标记的函数
        function delHtmlTag(str)
        {
        	// 去掉网页中的所有的html标记
        	return str.replace(/<[^>]+>/g,"").replace(/&nbsp;/g,"");//去掉所有的html标记
        }
        
    });
