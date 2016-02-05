'use strict';

angular.module('finderApp')
    .controller('ArticleDetailController', function ($scope, $rootScope, $stateParams, $sce, $http, DataUtils, entity, Article, User, ArticleCategory, Tag, Principal) {
    	 Principal.identity().then(function(account) {
             $scope.isAuthenticated = Principal.isAuthenticated;
         });
    	//异步请求时需要在回调函数中对文章内容html转义
//        $scope.article = entity;
    	Article.get({id : $stateParams.id}, function(result) {
            $scope.article = result;
            //angular对html转义,增加信任$sce.trustAsHtml
            $scope.article.content = $sce.trustAsHtml($scope.article.content);
            //文章作者
            $scope.account = $scope.article.user;
            // 右边栏 热门文章
            $scope.hotArticles = $scope.article.hotArticles;
        });
        $scope.load = function (id) {
            Article.get({id: id}, function(result) {
                $scope.article = result;
            });
        };
        var unsubscribe = $rootScope.$on('finderApp:articleUpdate', function(event, result) {
            $scope.article = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
        
        $scope.abbreviate = DataUtils.abbreviate;
        
        // 是否显示回到顶部按钮
        $(window).scroll(
		function() {
			$(this).scrollTop() > 400 ? $("#go-top-btn").css(
					"display", "block") : $("#go-top-btn").hide()
		});
        // 点击回到顶部按钮
        $("#go-top-btn").click(function(){
        	document.body.scrollTop=0;document.documentElement.scrollTop=0;
        });
        // 文章评论
        $scope.articleReply = {content:null};
        // 保存评论
        $scope.replySave = function () {
        	$scope.isSaving = true;
        	// 设置文章评论对应的文章ID(文章和评论的关联关系)
            $scope.articleReply.article = {id:$scope.article.id};
            // 目的是把json对象转换成字符串
//            JSON.stringify(data)
            // 用$http.post发请求
            $http.post('api/articleDetailsReplys', JSON.stringify($scope.articleReply)).success(function (response) {
                console.log(response);
            });
        };
        
        var onSaveSuccess = function (result) {
            $scope.$emit('评论保存成功！', result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };
    });
