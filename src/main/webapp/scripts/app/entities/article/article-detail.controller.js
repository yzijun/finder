'use strict';

angular.module('finderApp')
    .controller('ArticleDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Article, User, ArticleCategory, Tag) {
        $scope.article = entity;
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
