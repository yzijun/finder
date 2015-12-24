'use strict';

angular.module('finderApp')
    .controller('ForbiddenWordDetailController', function ($scope, $rootScope, $stateParams, entity, ForbiddenWord) {
        $scope.forbiddenWord = entity;
        $scope.load = function (id) {
            ForbiddenWord.get({id: id}, function(result) {
                $scope.forbiddenWord = result;
            });
        };
        var unsubscribe = $rootScope.$on('finderApp:forbiddenWordUpdate', function(event, result) {
            $scope.forbiddenWord = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
