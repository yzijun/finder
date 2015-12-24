'use strict';

angular.module('finderApp')
    .controller('ArticleReplyController', function ($scope, $state, ArticleReply, ArticleReplySearch, ParseLinks) {

        $scope.articleReplys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ArticleReply.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.articleReplys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ArticleReplySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.articleReplys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.articleReply = {
                content: null,
                published: null,
                createdDate: null,
                id: null
            };
        };
    });
