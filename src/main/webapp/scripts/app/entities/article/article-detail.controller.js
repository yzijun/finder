'use strict';

angular.module('finderApp')
    .controller('ArticleDetailController', function ($scope, $rootScope, $stateParams, $sce, DataUtils, entity, Article, User, ArticleCategory, Tag) {
    	//异步请求时需要在回调函数中对文章内容html转义
//        $scope.article = entity;
    	Article.get({id : $stateParams.id}, function(result) {
            $scope.article = result;
            //angular对html转义,增加信任$sce.trustAsHtml
            $scope.article.content = $sce.trustAsHtml($scope.article.content);
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

    });
