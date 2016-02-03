'use strict';

angular.module('finderApp')
    .controller('ArticleDetailController', function ($scope, $rootScope, $stateParams, $sce, DataUtils, entity, Article, User, ArticleCategory, Tag, Principal, ArticleReply) {
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
        // 保存评论
        $scope.replySave = function () {
        	$scope.isSaving = true;
            ArticleReply.save($scope.articleReply, onSaveSuccess, onSaveError);
        };
        
        var onSaveSuccess = function (result) {
            $scope.$emit('finderApp:articleReplyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };
    });
