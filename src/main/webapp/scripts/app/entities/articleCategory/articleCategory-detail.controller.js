'use strict';

angular.module('finderApp')
    .controller('ArticleCategoryDetailController', function ($scope, $rootScope, $stateParams, entity, ArticleCategory, Article) {
        $scope.articleCategory = entity;
        $scope.load = function (id) {
            ArticleCategory.get({id: id}, function(result) {
                $scope.articleCategory = result;
            });
        };
        var unsubscribe = $rootScope.$on('finderApp:articleCategoryUpdate', function(event, result) {
            $scope.articleCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
