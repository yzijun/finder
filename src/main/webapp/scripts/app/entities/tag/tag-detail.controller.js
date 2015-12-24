'use strict';

angular.module('finderApp')
    .controller('TagDetailController', function ($scope, $rootScope, $stateParams, entity, Tag, Article) {
        $scope.tag = entity;
        $scope.load = function (id) {
            Tag.get({id: id}, function(result) {
                $scope.tag = result;
            });
        };
        var unsubscribe = $rootScope.$on('finderApp:tagUpdate', function(event, result) {
            $scope.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
