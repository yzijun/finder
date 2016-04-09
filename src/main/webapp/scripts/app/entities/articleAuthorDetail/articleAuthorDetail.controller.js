'use strict';

angular.module('finderApp')
    .controller('ArticleAuthorDetailController', function ($scope, $state, $window, $http, $stateParams, WEBSITENAME) {
    	// 在controller中使用$stateParams中获取参数
//    	alert($stateParams.uid);
    	// 取得数据类型有文章、评论、收藏
    	$scope.type = "article";

    	$scope.articleAuthorPageData = [];
        $scope.loadArticleAuthor = function() {
        	$http.get('api/author/detail/' + $stateParams.uid).success(function(data) {
        		// 添加页面title
                $window.document.title =  data.user.nickName + " - " + WEBSITENAME;
                
                $scope.author = data;
        	});
        };
        $scope.loadArticleAuthor();
        
    });
