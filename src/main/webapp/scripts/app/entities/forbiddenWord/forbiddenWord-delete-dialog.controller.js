'use strict';

angular.module('finderApp')
	.controller('ForbiddenWordDeleteController', function($scope, $uibModalInstance, entity, ForbiddenWord) {

        $scope.forbiddenWord = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ForbiddenWord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
