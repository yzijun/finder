'use strict';

angular.module('finderApp')
    .controller('TagController', function ($scope, $state, Tag, TagSearch) {

        $scope.tags = [];
        $scope.loadAll = function() {
            Tag.query(function(result) {
               $scope.tags = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            TagSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tags = result;
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
            $scope.tag = {
                name: null,
                id: null
            };
        };
    });
