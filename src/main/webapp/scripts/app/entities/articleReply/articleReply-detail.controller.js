'use strict';

angular.module('finderApp')
    .controller('ArticleReplyDetailController', function ($scope, $rootScope, $stateParams, entity, ArticleReply, Article, User) {
        $scope.articleReply = entity;
        $scope.load = function (id) {
            ArticleReply.get({id: id}, function(result) {
                $scope.articleReply = result;
            });
        };
        var unsubscribe = $rootScope.$on('finderApp:articleReplyUpdate', function(event, result) {
            $scope.articleReply = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
