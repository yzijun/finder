'use strict';

angular.module('finderApp')
    .controller('ForbiddenWordController', function ($scope, $state, ForbiddenWord, ForbiddenWordSearch, ParseLinks) {

        $scope.forbiddenWords = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            ForbiddenWord.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.forbiddenWords.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.forbiddenWords = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ForbiddenWordSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.forbiddenWords = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.forbiddenWord = {
                word: null,
                id: null
            };
        };
    });
