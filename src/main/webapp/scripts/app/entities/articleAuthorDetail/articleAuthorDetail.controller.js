'use strict';

angular.module('finderApp')
    .controller('ArticleAuthorDetailController', function ($scope, $state, $http, $stateParams) {
    	// 在controller中使用$stateParams中获取参数
//    	alert($stateParams.uid);
    	// 取得数据类型有文章、评论、收藏
    	$scope.type = "article";

    	$scope.articleAuthors = [];
        $scope.loadArticleAuthors = function() {
        	$http.get('/someUrl').success(function(data) {
        		$scope.articleAuthors = data;
        	});
        };
        $scope.loadArticleAuthors();
        
    });
