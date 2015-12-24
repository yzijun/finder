'use strict';

angular.module('finderApp')
    .controller('ArticleCategoryController', function ($scope, $state, ArticleCategory, ArticleCategorySearch) {

        $scope.articleCategorys = [];
        $scope.loadAll = function() {
            ArticleCategory.query(function(result) {
               $scope.articleCategorys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ArticleCategorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.articleCategorys = result;
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
            $scope.articleCategory = {
                name: null,
                id: null
            };
        };
    });
